package it.orbyta.fabrick.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FabricMoneyTransferRequest {

    @NotNull
    @Valid
    private Creditor creditor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate executionDate;
    private String uri;
    @NotBlank
    private String description;
    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String currency;
    private Boolean isUrgent;
    private Boolean isInstant;
    private String feeType;
    private String feeAccountId;
    @Valid
    private TaxRelief taxRelief;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        @NotBlank
        private String accountCode;
        private String bicCode;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        private String address;
        private String city;
        private String countryCode;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Creditor {
        @NotBlank
        private String name;
        @NotNull
        @Valid
        private Account account;

        private Address address;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LegalPersonBeneficiary {
        private String fiscalCode;
        @NotBlank
        private String legalRepresentativeFiscalCode;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaturalPersonBeneficiary {
        @NotBlank
        private String fiscalCode1;
        private String fiscalCode2;
        private String fiscalCode3;
        private String fiscalCode4;
        private String fiscalCode5;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxRelief {
        private String taxReliefId;
        @NotNull
        private Boolean isCondoUpgrade;
        @NotBlank
        private String creditorFiscalCode;
        @NotBlank
        private String beneficiaryType;
        private NaturalPersonBeneficiary naturalPersonBeneficiary;
        private LegalPersonBeneficiary legalPersonBeneficiary;
    }


}
