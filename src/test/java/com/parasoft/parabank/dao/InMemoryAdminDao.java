package com.parasoft.parabank.dao;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAdminDao implements AdminDao {
    private final Map<String, String> parameters;

    public InMemoryAdminDao() {
        this(new HashMap<String, String>());
    }

    public InMemoryAdminDao(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void initializeDB() { }

    @Override
    public void cleanDB() { }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }
}
