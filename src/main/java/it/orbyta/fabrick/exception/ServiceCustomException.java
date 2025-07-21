package it.orbyta.fabrick.exception;

import it.orbyta.fabrick.dto.response.FabricApiErrorResponse;

public class ServiceCustomException extends RuntimeException {

    public ServiceCustomException() {
        super();
    }

    public ServiceCustomException(String message) {
        super(message);
    }

    public ServiceCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceCustomException(Throwable cause) {
        super(cause);
    }

}
