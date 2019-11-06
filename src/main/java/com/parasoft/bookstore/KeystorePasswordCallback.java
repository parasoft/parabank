package com.parasoft.bookstore;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

public class KeystorePasswordCallback implements CallbackHandler{
    private final String NL_USERNAME = "soatest";
    private final String NL_PASSWORD = "security";

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

        if (pc != null) {
            if (NL_USERNAME.equals(pc.getIdentifier())) {
                pc.setPassword(NL_PASSWORD);
            }
        }
    }
}