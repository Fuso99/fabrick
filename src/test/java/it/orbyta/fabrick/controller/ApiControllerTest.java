package it.orbyta.fabrick.controller;

import it.orbyta.fabrick.dto.response.FabricApiBaseResponse;
import it.orbyta.fabrick.dto.response.FabricBalanceResponse;
import it.orbyta.fabrick.dto.response.FabricTransactionResponse;
import it.orbyta.fabrick.service.FabrickApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FabrickApiService fabrickApiService;


    @Test
    void getBalanceOk() throws Exception {
        FabricBalanceResponse mockBalance = new FabricBalanceResponse(
                LocalDate.of(2025, 7, 22),
                new BigDecimal("1000.50"),
                new BigDecimal("950.00"),
                "EUR"
        );
        FabricApiBaseResponse<FabricBalanceResponse> mockApiResponse =
                new FabricApiBaseResponse<>("OK", Collections.emptyList(), mockBalance);

        when(fabrickApiService.getBalance()).thenReturn(mockApiResponse);

        mockMvc.perform(get("/api/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.payload.balance").value(1000.50));
    }


    @Test
    void getTransactionOk() throws Exception {
        LocalDate fromDate = LocalDate.of(2025, 1, 1);
        LocalDate toDate = LocalDate.of(2025, 3, 31);

        FabricTransactionResponse.TransactionPayload t1 = new FabricTransactionResponse.TransactionPayload(
                "417436013700", "25000926012179",
                LocalDate.of(2025, 6, 6),
                LocalDate.of(2025, 6, 6),
                null, BigDecimal.valueOf(-0.1), "EUR", "BA TERRIBILE LUCA");

        FabricTransactionResponse.TransactionPayload t2 = new FabricTransactionResponse.TransactionPayload(
                "412240318900", "25000895419259",
                LocalDate.of(2025, 5, 7),
                LocalDate.of(2025, 5, 7),
                null, BigDecimal.valueOf(12.25), "EUR", "BA TERRIBILE LUCA        REC 94748B390EF241F7ABFADAF8588D9CEE TEST CUTOFF");

        FabricTransactionResponse fabricTransactionResponse = new FabricTransactionResponse(List.of(t1, t2));

        FabricApiBaseResponse<FabricTransactionResponse> response = new FabricApiBaseResponse<>("OK", Collections.emptyList(), fabricTransactionResponse);

        when(fabrickApiService.getTransactionList(any(LocalDate.class), any(LocalDate.class))).thenReturn(response);

        mockMvc.perform(get("/api/transaction")
                        .param("from", fromDate.toString())
                        .param("to", toDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.payload.list[0].transactionId").value("417436013700"))
                .andExpect(jsonPath("$.payload.list[1].amount").value(12.25));
    }

}
