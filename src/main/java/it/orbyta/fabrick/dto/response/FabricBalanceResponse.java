package it.orbyta.fabrick.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FabricBalanceResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    LocalDate date;

    BigDecimal balance;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    BigDecimal availableBalance;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String currency;

}
