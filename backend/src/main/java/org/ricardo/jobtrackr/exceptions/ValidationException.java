package org.ricardo.jobtrackr.exceptions;

public class ValidationException extends RuntimeException {
    public static final int STATUS_CODE = 400;

    public ValidationException(String message) {
        super(message);
    }
}
