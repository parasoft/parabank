package com.parasoft.parabank.web.controller.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1068780730577472806L;

    private final String errorMessage;

    public AuthenticationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getMessageBody() {
        return new MessageBody(errorMessage);
    }

    protected static final class MessageBody {

        private final String message;

        public MessageBody(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return HttpStatus.UNAUTHORIZED.value();
        }
    }
}
