package com.parasoft.bookstore;

import java.io.*;

import javax.security.auth.callback.*;

import org.apache.wss4j.common.ext.*;

public class UsernameTokenPasswordCallback implements CallbackHandler {
    private final String NL_USERNAME = "soatest";
    private final String NL_PASSWORD = "soatest";
    private final String NL_USERNAME2 = "soaptest";
    private final String NL_PASSWORD2 = "soaptest";
    private final String NL_USERNAME3 = "admin";
    private final String NL_PASSWORD3 = "admin";

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
