package it.orbyta.fabrick.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Data
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class FabricTransactionResponse {

    @JsonProperty("list")
    List<TransactionPayload> transactionList;

    @Data
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TransactionPayload {

        String transactionId;
        String operationId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String accountingDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String valueDate;
        Type type;
        BigDecimal amount;
        String currency;
        String description;
    }

    @Data
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Type {
        String enumeration;
        String value;
    }
}
