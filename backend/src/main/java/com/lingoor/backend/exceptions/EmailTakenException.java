package com.lingoor.backend.exceptions;

import com.lingoor.backend.constants.Constants;

public class EmailTakenException extends CustomException{
    public EmailTakenException() {
        super(Constants.EMAIL_TAKEN);
    }
}
