package it.orbyta.fabrick.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FabricMoneyTransferRequest {

    @NotNull
    @Valid
    public Creditor creditor;
    public String executionDate;
    public String uri;
    @NotBlank
    public String description;
    @NotNull
    public BigDecimal amount;
    @NotBlank
    public String currency;
    public Boolean isUrgent;
    public Boolean isInstant;
    public String feeType;
    public String feeAccountId;
    @Valid
    public TaxRelief taxRelief;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        @NotBlank
        public String accountCode;
        public String bicCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        public String address;
        public String city;
        public String countryCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Creditor {
        @NotBlank
        public String name;
        @NotNull
        @Valid
        public Account account;

        public Address address;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LegalPersonBeneficiary {
        public String fiscalCode;
        @NotBlank
        public String legalRepresentativeFiscalCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaturalPersonBeneficiary {
        @NotBlank
        public String fiscalCode1;
        public String fiscalCode2;
        public String fiscalCode3;
        public String fiscalCode4;
        public String fiscalCode5;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxRelief {
        public String taxReliefId;
        @NotNull
        public Boolean isCondoUpgrade;
        @NotBlank
        public String creditorFiscalCode;
        @NotBlank
        public String beneficiaryType;
        public NaturalPersonBeneficiary naturalPersonBeneficiary;
        public LegalPersonBeneficiary legalPersonBeneficiary;
    }


}
