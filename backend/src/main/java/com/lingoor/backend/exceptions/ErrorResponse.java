package com.lingoor.backend.exceptions;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

}
