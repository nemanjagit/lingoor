package com.lingoor.backend.exceptions;

import com.lingoor.backend.constants.Constants;

public class UsernameTakenException extends CustomException{
    public UsernameTakenException() {
        super(Constants.USERNAME_TAKEN);
    }
}
