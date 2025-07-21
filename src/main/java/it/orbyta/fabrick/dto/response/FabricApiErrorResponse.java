package it.orbyta.fabrick.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

@Data
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class FabricApiErrorResponse {
    @JsonProperty
    String code;
    @JsonProperty
    String description;
    @JsonProperty
    String params;
}
