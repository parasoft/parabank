package com.parasoft.parabank.web.controller;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

public abstract class AbstractControllerTest<T> extends AbstractParaBankDataSourceTest {
    private final static Logger log = LoggerFactory.getLogger(AbstractControllerTest.class);

    private final Class<T> controllerClass;

    protected T controller;

    @SuppressWarnings("unchecked")
    protected AbstractControllerTest() {
        controllerClass =
            (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        log.trace("in AbstractControllerTest.onSetUp()");

        controller = controllerClass.newInstance();

    }

}
