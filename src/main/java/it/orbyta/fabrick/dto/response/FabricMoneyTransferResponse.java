package it.orbyta.fabrick.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FabricMoneyTransferResponse {

    String moneyTransferId;
    String status;
    String direction;
    Creditor creditor;
    Debtor debtor;
    String cro;
    String uri;
    String trn;
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    OffsetDateTime createdDatetime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    OffsetDateTime accountedDatetime;
    String debtorValueDate;
    String creditorValueDate;
    Amount amount;
    boolean isUrgent;
    boolean isInstant;
    String feeType;
    String feeAccountId;
    List<Fee> fees;
    boolean hasTaxRelief;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        String accountCode;
        String bicCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        Object address;
        Object city;
        Object countryCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        int debtorAmount;
        String debtorCurrency;
        int creditorAmount;
        String creditorCurrency;
        String creditorCurrencyDate;
        int exchangeRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Creditor {
        String name;
        Account account;
        Address address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Debtor {
        String name;
        Account account;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fee {
        String feeCode;
        String description;
        double amount;
        String currency;
    }


}
