package com.parasoft.parabank.messaging;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;

public class MockConnectionFactory implements ConnectionFactory {
    public MockConnectionFactory() throws Exception {
        createConnection();
        createConnection(null, null);
    }

    @Override
    public Connection createConnection() throws JMSException {
        return null;
    }

    @Override
    public Connection createConnection(String userName, String password) throws JMSException {
        return null;
    }

    @Override
    public JMSContext createContext() {
        return null;
    }

    @Override
    public JMSContext createContext(int sessionMode) {
        return null;
    }

    @Override
    public JMSContext createContext(String userName, String password) {
        return null;
    }

    @Override
    public JMSContext createContext(String userName, String password, int sessionMode) {
        return null;
    }
}
