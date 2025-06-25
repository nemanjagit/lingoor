package com.lingoor.backend.exceptions;

import com.lingoor.backend.constants.Constants;

public class AlreadyFollowingException extends CustomException {
    public AlreadyFollowingException(Long id) {
        super(Constants.ALREADY_FOLLOWING + id);
    }
}
