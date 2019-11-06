package com.parasoft.bookstore2;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

public class UsernameTokenPasswordCallback implements CallbackHandler {
    private final String NL_USERNAME = "soatest";
    private final String NL_PASSWORD = "soatest";
    private final String NL_USERNAME2 = "soaptest";
    private final String NL_PASSWORD2 = "soaptest";
    private final String NL_USERNAME3 = "admin";
    private final String NL_PASSWORD3 = "admin";

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

        if (pc != null) {
            if (NL_USERNAME.equals(pc.getIdentifier())) {
                pc.setPassword(NL_PASSWORD);
            } else if (NL_USERNAME2.equals(pc.getIdentifier())) {
                pc.setPassword(NL_PASSWORD2);
            } else if (NL_USERNAME3.equals(pc.getIdentifier())) {
                pc.setPassword(NL_PASSWORD3);
            }
        }
    }
}
