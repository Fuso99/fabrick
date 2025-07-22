package it.orbyta.fabrick.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.orbyta.fabrick.dto.request.FabricMoneyTransferRequest;
import it.orbyta.fabrick.dto.response.FabricApiBaseResponse;
import it.orbyta.fabrick.dto.response.FabricApiErrorResponse;
import it.orbyta.fabrick.dto.response.FabricBalanceResponse;
import it.orbyta.fabrick.dto.response.FabricTransactionResponse;
import it.orbyta.fabrick.exception.TecnicalErrorException;
import it.orbyta.fabrick.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FabrickApiServiceTest {
    @InjectMocks
    private FabrickApiService fabrickApiService;

    @Mock
    private ObjectMapper om;
    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private TransactionRepository repository;


    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(fabrickApiService, "baseUri", "https://fake.api");
        ReflectionTestUtils.setField(fabrickApiService, "bankingAccountsUri", "/banking/v1/accounts/");
        ReflectionTestUtils.setField(fabrickApiService, "accountId", 123456789L);
        ReflectionTestUtils.setField(fabrickApiService, "apiKey", "fake-key");
        ReflectionTestUtils.setField(fabrickApiService, "authSchema", "S2S");
    }

    @Test
    void testGetBalanceOK() throws Exception {
        String testResponseBody = "{\"status\":\"OK\",\"payload\":{\"balance\":25.25}}";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(testResponseBody);
        when(om.readValue(eq(testResponseBody), any(TypeReference.class))).thenReturn(
                new FabricApiBaseResponse<>("OK", Collections.emptyList(), new FabricBalanceResponse(LocalDate.now(), new BigDecimal(25.25), new BigDecimal(100), "EUR"))
        );

        FabricApiBaseResponse<FabricBalanceResponse> result = fabrickApiService.getBalance();

        assertNotNull(result);
        assertNotNull(result.getPayload());
        assertNotNull(result.getPayload().getBalance());
        verify(httpClient).send(any(), any());
    }

    @Test
    void testGetTransactionListOK() throws Exception {
        String testResponseBody = getTransactionResponseJson();

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(testResponseBody);
        FabricTransactionResponse transactionResponsePayload = new FabricTransactionResponse(
                List.of(new FabricTransactionResponse.TransactionPayload("417436013700", "25000926012179",
                        LocalDate.of(2025, 6, 6), LocalDate.of(2025, 6, 6),
                        null, new BigDecimal("-0.1"), "EUR", "BA TERRIBILE LUCA REC 94748B390EF241F7ABFADAF8588D9CEE TEST CUTOFF"
                ))
        );

        when(om.readValue(eq(testResponseBody), any(TypeReference.class))).thenReturn(
                new FabricApiBaseResponse<>("OK", Collections.emptyList(), transactionResponsePayload)
        );

        FabricApiBaseResponse<FabricTransactionResponse> result = fabrickApiService.getTransactionList(LocalDate.now().minusMonths(4), LocalDate.now());

        assertNotNull(result);
        assertNotNull(result.getPayload());
        assertFalse(result.getPayload().getList().isEmpty());
        verify(repository).saveAll(anyList());
        verify(httpClient).send(any(), any());
    }


    @Test
    void testMoneyTransferKO() throws Exception {
        String errorJson = """
                    "status": "KO",
                        "errors": [
                    {
                        "code": "API000",
                        "description": "Errore tecnico  La condizione BP049 non e' prevista per il conto id 14537780",
                        "params": ""
                    }
                    ]
                    ,
                        "payload": {}
                    }
                """;
        String errorMessage = "Errore tecnico  La condizione BP049 non e' prevista per il conto id 14537780";
        FabricMoneyTransferRequest transferRequest = createFabricMoneyTransferRequest();

        when(om.writeValueAsString(eq(transferRequest))).thenReturn(getExpectedMoneyTransferRequestJson());


        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpResponse.body()).thenReturn(errorJson);
        when(om.readValue(eq(errorJson), any(TypeReference.class)))
                .thenReturn(
                        new FabricApiBaseResponse<>("OK",
                                List.of(new FabricApiErrorResponse("API000", "Errore tecnico  La condizione BP049 non e' prevista per il conto id 14537780", "")), null)
                );

        TecnicalErrorException thrown = assertThrows(TecnicalErrorException.class, () -> fabrickApiService.moneyTransfer(createFabricMoneyTransferRequest()));

        assertEquals(errorMessage, thrown.getMessage());

        verify(httpClient).send(any(), any());
    }


    private FabricMoneyTransferRequest createFabricMoneyTransferRequest() {
        FabricMoneyTransferRequest request = new FabricMoneyTransferRequest();

        FabricMoneyTransferRequest.Creditor creditor = new FabricMoneyTransferRequest.Creditor();
        creditor.setName("John Doe");

        FabricMoneyTransferRequest.Address address = new FabricMoneyTransferRequest.Address();
        address.setAddress(null);
        address.setCountryCode(null);
        creditor.setAddress(address);

        FabricMoneyTransferRequest.Account account = new FabricMoneyTransferRequest.Account();
        account.setAccountCode("IT23A0336844430152923804660");
        account.setBicCode("SELBIT2BXXX");
        creditor.setAccount(account);

        request.setCreditor(creditor);
        request.setExecutionDate(LocalDate.of(2019, 4, 1));
        request.setUri("REMITTANCE_INFORMATION");
        request.setDescription("Payment invoice 75/2017");
        request.setAmount(new BigDecimal("800.00"));
        request.setCurrency("EUR");
        request.setIsUrgent(false);
        request.setIsInstant(false);
        request.setFeeType("SHA");
        request.setFeeAccountId("45685475");

        FabricMoneyTransferRequest.TaxRelief taxRelief = new FabricMoneyTransferRequest.TaxRelief();
        taxRelief.setTaxReliefId("L449");
        taxRelief.setIsCondoUpgrade(false);
        taxRelief.setCreditorFiscalCode("56258745832");
        taxRelief.setBeneficiaryType("NATURAL_PERSON");

        FabricMoneyTransferRequest.NaturalPersonBeneficiary npb = new FabricMoneyTransferRequest.NaturalPersonBeneficiary();
        npb.setFiscalCode1("MRLFNC81L04A859L");
        taxRelief.setNaturalPersonBeneficiary(npb);

        FabricMoneyTransferRequest.LegalPersonBeneficiary pb = new FabricMoneyTransferRequest.LegalPersonBeneficiary();
        pb.setFiscalCode(null);
        pb.setLegalRepresentativeFiscalCode(null);
        taxRelief.setLegalPersonBeneficiary(pb);


        request.setTaxRelief(taxRelief);

        return request;
    }

    private String getExpectedMoneyTransferRequestJson() throws JsonProcessingException {
        ObjectMapper tempMapper = new ObjectMapper();
        tempMapper.registerModule(new JavaTimeModule());
        return tempMapper.writeValueAsString(createFabricMoneyTransferRequest());
    }

    private String getTransactionResponseJson() {
        return """
                {
                	"status": "OK",
                	"error": [],
                	"payload": {
                		"list": [
                			{
                				"transactionId": "417436013700",
                				"operationId": "25000926012179",
                				"accountingDate": "2025-06-06",
                				"valueDate": "2025-06-06",
                				"type": {
                					"enumeration": "GBS_TRANSACTION_TYPE",
                					"value": "GBS_ACCOUNT_TRANSACTION_TYPE_0009"
                				},
                				"amount": -0.1,
                				"currency": "EUR",
                				"description": "BA TERRIBILE LUCA        REC 94748B390EF241F7ABFADAF8588D9CEE TEST CUTOFF"
                			}
                		]
                	}
                }""";
    }
}
