package it.orbyta.fabrick.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FabricMoneyTransferRequest {

    @NotNull
    public Creditor creditor;
    public String executionDate;
    public String uri;
    @NotNull
    public String description;
    @NotNull
    public BigDecimal amount;
    @NotNull
    public String currency;
    public boolean isUrgent;
    public boolean isInstant;
    public String feeType;
    public String feeAccountId;
    public TaxRelief taxRelief;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        @NotNull
        public String accountCode;
        public String bicCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        public Object address;
        public Object city;
        public Object countryCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Creditor {
        @NotNull
        public String name;
        @NotNull
        public Account account;

        public Address address;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LegalPersonBeneficiary {
        public Object fiscalCode;
        @NotNull
        public Object legalRepresentativeFiscalCode;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaturalPersonBeneficiary {
        @NotNull
        public String fiscalCode1;
        public Object fiscalCode2;
        public Object fiscalCode3;
        public Object fiscalCode4;
        public Object fiscalCode5;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxRelief {
        public String taxReliefId;
        @NotNull
        public boolean isCondoUpgrade;
        @NotNull
        public String creditorFiscalCode;
        @NotNull
        public String beneficiaryType;
        public NaturalPersonBeneficiary naturalPersonBeneficiary;
        public LegalPersonBeneficiary legalPersonBeneficiary;
    }


}
