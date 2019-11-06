package com.parasoft.parabank.domain.validator;

import javax.annotation.Resource;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.web.form.FindTransactionForm;

/**
 * Provides basic empty field validation for <code>BillPayForm</code> object
 */
public class FindTransactionFormValidator implements Validator {
    @SuppressWarnings("unused")
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminFormValidator.class);

    @Resource(name = "transactionCriteriaValidator")
    private Validator transactionCriteriaValidator;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the payeeValidator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @return the value of payeeValidator field
     */
    public Validator getTransactionCriteriaValidator() {
        return transactionCriteriaValidator;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the transactionCriteriaValidator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param aTransactionCriteriaValidator
     *            new value for the transactionCriteriaValidator property
     */
    public void setTransactionCriteriaValidator(final Validator aTransactionCriteriaValidator) {
        transactionCriteriaValidator = aTransactionCriteriaValidator;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return FindTransactionForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        final FindTransactionForm findTransactionForm = (FindTransactionForm) obj;

        final TransactionCriteria criteria = findTransactionForm.getCriteria();

        try {
            errors.pushNestedPath("criteria");
            getTransactionCriteriaValidator().validate(criteria, errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
