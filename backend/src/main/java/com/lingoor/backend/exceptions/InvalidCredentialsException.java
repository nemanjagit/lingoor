package com.lingoor.backend.exceptions;

import com.lingoor.backend.constants.Constants;

public class InvalidCredentialsException extends CustomException{
    public InvalidCredentialsException() {
        super(Constants.INVALID_CREDENTIALS);
    }
}
