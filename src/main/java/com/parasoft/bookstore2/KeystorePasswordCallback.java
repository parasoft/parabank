package com.parasoft.bookstore2;

import java.io.*;

import javax.security.auth.callback.*;

import org.apache.ws.security.*;

public class KeystorePasswordCallback implements CallbackHandler{
    private final String NL_USERNAME = "soatest";
    private final String NL_PASSWORD = "security";
    
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

        if (pc != null) {
            if (NL_USERNAME.equals(pc.getIdentifier())) {
                pc.setPassword(NL_PASSWORD);
            }
        }
    }
}