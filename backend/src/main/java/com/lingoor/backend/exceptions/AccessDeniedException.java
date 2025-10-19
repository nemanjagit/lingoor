package com.lingoor.backend.exceptions;

import com.lingoor.backend.constants.Constants;

public class AccessDeniedException extends CustomException {
    public AccessDeniedException() {
        super(Constants.ACCESS_DENIED);
    }
}
