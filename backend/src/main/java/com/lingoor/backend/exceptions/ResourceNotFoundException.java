package com.lingoor.backend.exceptions;

import com.lingoor.backend.constants.Constants;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException() {
        super(Constants.RESOURCE_NOT_FOUND);
    }
}
