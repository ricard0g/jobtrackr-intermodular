package org.ricardo.jobtrackr.exceptions;

public class NotFoundException extends RuntimeException {
    public static final int STATUS_CODE = 404;

    public NotFoundException(String message) {
        super(message);
    }
}
