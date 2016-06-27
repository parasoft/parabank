package com.parasoft.parabank.dao;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAdminDao implements AdminDao {
    private Map<String, String> parameters;
    
    public InMemoryAdminDao() {
        this(new HashMap<String, String>());
    }
    
    public InMemoryAdminDao(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void initializeDB() { }
    
    public void cleanDB() { }
    
    public String getParameter(String name) {
        return parameters.get(name);
    }
    
    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }
    
    public Map<String, String> getParameters() {
        return parameters;
    }
}
