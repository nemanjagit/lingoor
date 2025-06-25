package com.lingoor.backend.exceptions;

import com.lingoor.backend.constants.Constants;

public class UnauthorizedException extends CustomException{
    public UnauthorizedException() {
        super(Constants.USER_NOT_AUTHORIZED);
    }
}
