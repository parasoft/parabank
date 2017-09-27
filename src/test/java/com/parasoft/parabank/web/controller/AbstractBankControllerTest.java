package com.parasoft.parabank.web.controller;

import javax.annotation.*;

import org.slf4j.*;

import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.util.*;

abstract class AbstractBankControllerTest<T extends AbstractBankController> extends AbstractControllerTest<T> {
    private final static Logger log = LoggerFactory.getLogger(AbstractControllerTest.class);

    @Resource(name = "bankManager")
    protected BankManager bankManager;

    @Resource(name = "accessModeController")
    protected AccessModeController amc;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        log.trace("in AbstractBankControllerTest.onSetUp()");
        controller.setBankManager(bankManager);
    }

    public final void setAccessModeController(final AccessModeController amc) {
        this.amc = amc;
    }

    public final void setBankManager(final BankManager bankManager) {
        this.bankManager = bankManager;
    }
}
