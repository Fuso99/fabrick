package it.orbyta.fabrick.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FabricApiBaseResponse<T> {

    String status;
    List<FabricApiErrorResponse> errors;
    T payload;

}

