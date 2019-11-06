package com.parasoft.parabank.messaging;

import javax.jms.JMSException;

import org.springframework.jms.JmsException;
import org.springframework.jms.listener.AbstractJmsListeningContainer;

public class MockJmsListeningContainer extends AbstractJmsListeningContainer {
    private boolean running;
    private boolean initialized;

    public MockJmsListeningContainer() throws Exception {
        doInitialize();
        doShutdown();
        sharedConnectionEnabled();
    }

    @Override
    public void start() throws JmsException {
        running = true;
    }

    @Override
    public void initialize() throws JmsException {
        initialized = true;
    }

    @Override
    public void stop() throws JmsException {
        running = false;
    }

    @Override
    public void shutdown() throws JmsException {
        initialized = false;
    }

    public boolean isListenerRunning() {
        return running;
    }

    public void setListenerRunning(boolean running) {
        this.running = running;
    }

    public boolean isListenerInitialized() {
        return initialized;
    }

    public void setListenerInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    protected void doInitialize() throws JMSException { }

    @Override
    protected void doShutdown() throws JMSException { }

    @Override
    protected boolean sharedConnectionEnabled() {
        return false;
    }
}
