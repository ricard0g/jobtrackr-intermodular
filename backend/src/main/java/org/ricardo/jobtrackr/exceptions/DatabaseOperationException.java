package org.ricardo.jobtrackr.exceptions;

public class DatabaseOperationException extends RuntimeException {
    public static final int STATUS_CODE = 500;

    public DatabaseOperationException(String message) {
        super(message);
    }
}
