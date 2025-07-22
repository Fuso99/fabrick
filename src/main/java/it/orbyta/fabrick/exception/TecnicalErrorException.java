package it.orbyta.fabrick.exception;

public class TecnicalErrorException extends RuntimeException {

    public TecnicalErrorException() {
        super();
    }

    public TecnicalErrorException(String message) {
        super(message);
    }

    public TecnicalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TecnicalErrorException(Throwable cause) {
        super(cause);
    }

}