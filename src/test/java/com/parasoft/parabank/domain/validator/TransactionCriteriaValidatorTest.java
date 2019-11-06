package com.parasoft.parabank.domain.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;
import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-37
 *
 */
public class TransactionCriteriaValidatorTest extends AbstractParaBankTest {
    private Validator validator;

    private TransactionCriteria criteria;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        validator = new TransactionCriteriaValidator();
        criteria = new TransactionCriteria();
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(TransactionCriteria.class));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void testValidateId() throws Exception {
        criteria.setSearchType(SearchType.ID);
        AbstractValidatorTest.assertValidate(validator, criteria, new String[] { "transactionId" });
    }

    @Test
    public void testValidateDate() throws Exception {
        criteria.setSearchType(SearchType.DATE);
        AbstractValidatorTest.assertValidate(validator, criteria, new String[] { "onDate" });
    }

    @Test
    public void testValidateDateRange() throws Exception {
        criteria.setSearchType(SearchType.DATE_RANGE);
        AbstractValidatorTest.assertValidate(validator, criteria, new String[] { "toDate", "fromDate" });
    }

    @Test
    public void testValidateAmount() throws Exception {
        criteria.setSearchType(SearchType.AMOUNT);
        AbstractValidatorTest.assertValidate(validator, criteria, new String[] { "amount" });
    }
}
