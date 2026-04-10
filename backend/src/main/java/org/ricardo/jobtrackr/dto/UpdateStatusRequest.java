package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.exceptions.ValidationException;

public class UpdateStatusRequest {
    private String estatus;

    public UpdateStatusRequest() {
    }

    public String getEstatus() {
        return estatus;
    }
}
