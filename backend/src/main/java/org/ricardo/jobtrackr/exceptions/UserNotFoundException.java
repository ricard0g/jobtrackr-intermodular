package org.ricardo.jobtrackr.exceptions;

public class UserNotFoundException extends RuntimeException {
    public static final int STATUS_CODE = 404;

    public UserNotFoundException(String message) {
        super(message);
    }
}
