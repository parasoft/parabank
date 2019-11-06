package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.ParameterizedType;

import javax.annotation.Resource;

import org.junit.Test;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.util.LoanRequestFactory;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

public abstract class AbstractLoanProcessorTest<T extends AbstractLoanProcessor>
        extends AbstractParaBankDataSourceTest {
    private final Class<T> processorClass;

    protected T processor;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

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
    }

    public final void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Test
    public final void testProcessLoan() {
        LoanRequest loanRequest = LoanRequestFactory.create(1000, 2000, 10000);
        final LoanResponse response = processor.requestLoan(loanRequest);
        assertFalse(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals("error.insufficient.funds.for.down.payment", response.getMessage());
        assertProcessor();
    }
}
