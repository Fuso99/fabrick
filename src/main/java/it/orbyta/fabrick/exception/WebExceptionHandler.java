package it.orbyta.fabrick.exception;


import it.orbyta.fabrick.dto.response.FabricApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ServiceCustomException.class)
    public ResponseEntity<FabricApiErrorResponse> handleConstraintViolation(ServiceCustomException ex) {
        FabricApiErrorResponse error = new FabricApiErrorResponse("API000", "Errore tecnico  La condizione BP049 non e' prevista per il conto id 14537780", "");
        return ResponseEntity.badRequest().body(error);
    }

}
