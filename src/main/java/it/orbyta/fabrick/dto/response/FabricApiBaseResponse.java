package it.orbyta.fabrick.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Value;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Data
public class FabricApiBaseResponse<T> {

    String status;
    List<FabricApiErrorResponse> error;
    T payload;

}

