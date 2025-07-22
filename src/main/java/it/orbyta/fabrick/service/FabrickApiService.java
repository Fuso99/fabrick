package it.orbyta.fabrick.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.orbyta.fabrick.dto.request.FabricMoneyTransferRequest;
import it.orbyta.fabrick.dto.response.FabricApiBaseResponse;
import it.orbyta.fabrick.dto.response.FabricBalanceResponse;
import it.orbyta.fabrick.dto.response.FabricMoneyTransferResponse;
import it.orbyta.fabrick.dto.response.FabricTransactionResponse;
import it.orbyta.fabrick.entity.TransactionEntity;
import it.orbyta.fabrick.exception.TecnicalErrorException;
import it.orbyta.fabrick.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;

@Service
@Slf4j
public class FabrickApiService {

    @Value("${fabrick.base-uri}")
    private String baseUri;
    @Value("${fabrick.banking-accounts-uri}")
    private String bankingAccountsUri;
    @Value("${fabrick.api-key}")
    private String apiKey;
    @Value("${fabrick.auth-schema}")
    private String authSchema;
    @Value("${fabrick.account-id}")
    private Long accountId;

    @Autowired
    @Qualifier("myObjectMapper")
    private ObjectMapper om;
    @Autowired
    private TransactionRepository repository;


    private static final String HEADER_AUTH_SCHEMA = "Auth-Schema";
    private static final String HEADER_API_KEY = "Api-Key";
    private static final String HEADER_TIME_ZONE = "X-Time-Zone";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json";

    private static final String BALANCE_PATH = "/balance";
    private static final String TRANSACTIONS_PATH = "/transactions";
    private static final String MONEY_TRANSFER_PATH = "/payments/money-transfers";

    private HttpClient httpClient = HttpClient.newHttpClient();


    public FabricApiBaseResponse<FabricBalanceResponse> getBalance() throws Exception {
        URI uri = new URI(baseUri + bankingAccountsUri + accountId + BALANCE_PATH);
        HttpRequest request = buildGetRequest(uri);
        return sendRequest(request, new TypeReference<>() {
        });
    }

    public FabricApiBaseResponse<FabricTransactionResponse> getTransactionList(LocalDate fromDate, LocalDate toDate) throws Exception {
        String queryParams = String.format("?fromAccountingDate=%s&toAccountingDate=%s", fromDate, toDate);
        URI uri = new URI(baseUri + bankingAccountsUri + accountId + TRANSACTIONS_PATH + queryParams);

        HttpRequest request = buildGetRequest(uri);
        FabricApiBaseResponse<FabricTransactionResponse> response = sendRequest(request, new TypeReference<>() {
        });
        saveTransactionList(response);
        return response;
    }

    private void saveTransactionList(FabricApiBaseResponse<FabricTransactionResponse> response) {
        List<TransactionEntity> entities = response.getPayload().getList().stream().map(trans -> {
            TransactionEntity t = new TransactionEntity();
            t.setOperationId(trans.getOperationId());
            t.setAccountingDate(trans.getAccountingDate());
            t.setValueDate(trans.getValueDate());
            t.setDescription(trans.getDescription());
            t.setAmount(trans.getAmount());
            t.setCurrency(trans.getCurrency());
            return t;
        }).toList();
        repository.saveAll(entities);
    }

    public FabricApiBaseResponse<FabricMoneyTransferResponse> moneyTransfer(FabricMoneyTransferRequest requestData) throws Exception {
        URI uri = new URI(baseUri + bankingAccountsUri + accountId + MONEY_TRANSFER_PATH);
        String body = om.writeValueAsString(requestData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .headers(
                        HEADER_AUTH_SCHEMA, authSchema,
                        HEADER_API_KEY, apiKey,
                        HEADER_TIME_ZONE, TimeZone.getDefault().getID(),
                        CONTENT_TYPE, JSON_CONTENT_TYPE
                )
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return sendRequest(request, new TypeReference<>() {
        });
    }

    private HttpRequest buildGetRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .headers(
                        HEADER_AUTH_SCHEMA, authSchema,
                        HEADER_API_KEY, apiKey
                )
                .GET()
                .build();
    }

    private <T> T sendRequest(HttpRequest request, TypeReference<T> typeReference) throws Exception {
        log.debug("Sending request to URI: {}", request.uri());

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response != null && HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
            T parsedResponse = om.readValue(response.body(), typeReference);
            log.debug("Success -> {}", parsedResponse);
            return parsedResponse;
        } else if (response != null) {
            log.warn("Request failed with status {} and body {}", response.statusCode(), response.body());
            try {
                FabricApiBaseResponse<T> error = om.readValue(response.body(), new TypeReference<>() {
                });
                log.warn("Error response: {}", error);
                throw new TecnicalErrorException("Errore tecnico  La condizione BP049 non e' prevista per il conto id 14537780");
            } catch (Exception e) {
                if (e instanceof TecnicalErrorException) {
                    throw e;
                }
                log.error("Error while parsing error response", e);
                throw new ServiceException("Error while parsing error response");
            }
        } else {
            log.error("Null response received from API.");
            throw new ServiceUnavailableException("Null response received from API.");
        }


    }
}

