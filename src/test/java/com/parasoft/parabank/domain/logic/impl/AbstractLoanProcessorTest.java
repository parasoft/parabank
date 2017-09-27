package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.math.*;

import javax.annotation.*;

import org.junit.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.test.util.*;

public abstract class AbstractLoanProcessorTest<T extends AbstractLoanProcessor>
        extends AbstractParaBankDataSourceTest {
    private final Class<T> processorClass;

    protected T processor;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    protected LoanRequest loanRequest;

    @SuppressWarnings("unchecked")
    protected AbstractLoanProcessorTest() {
        processorClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected abstract void assertProcessor();

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();

        processor = processorClass.newInstance();
        processor.setAdminManager(adminManager);

        loanRequest = new LoanRequest();
        loanRequest.setAvailableFunds(new BigDecimal("1000.00"));
    }

    public final void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Test
    public final void testProcessLoan() {
        loanRequest.setLoanAmount(new BigDecimal("10000.00"));
        loanRequest.setDownPayment(new BigDecimal("2000.00"));
        final LoanResponse response = processor.requestLoan(loanRequest);
        assertFalse(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals("error.insufficient.funds.for.down.payment", response.getMessage());
        assertProcessor();
    }
}
