package com.parasoft.parabank.web.controller.exception;

public class AuthenticationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1068780730577472806L;
    
    private String errorMessage;

    public AuthenticationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getMessageBody() {
        return new MessageBody(errorMessage);
    }

    protected static final class MessageBody {
        
        private String message;
        
        public MessageBody(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
