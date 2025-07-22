package it.orbyta.fabrick.exception;


import it.orbyta.fabrick.dto.response.FabricApiErrorResponse;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(TecnicalErrorException.class)
    public ResponseEntity<FabricApiErrorResponse> handleConstraintViolation(TecnicalErrorException ex) {
        FabricApiErrorResponse error = new FabricApiErrorResponse("API000", ex.getMessage(), "");
        return ResponseEntity.badRequest().body(error);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<FabricApiErrorResponse> handleConstraintViolation(ServiceUnavailableException ex) {
        FabricApiErrorResponse error = new FabricApiErrorResponse("API503", ex.getMessage(), "");
        return ResponseEntity.badRequest().body(error);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<FabricApiErrorResponse> handleConstraintViolation(ServiceException ex) {
        FabricApiErrorResponse error = new FabricApiErrorResponse("API500", ex.getMessage(), "");
        return ResponseEntity.badRequest().body(error);
    }

}
