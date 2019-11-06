package com.parasoft.parabank.domain.validator;

import javax.annotation.Resource;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.Payee;
import com.parasoft.parabank.web.form.BillPayForm;

/**
 * Provides basic empty field validation for <code>BillPayForm</code> object
 */
public class BillPayFormValidator implements Validator {
    @SuppressWarnings("unused")
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminFormValidator.class);

    @Resource(name = "payeeValidator")
    private Validator payeeValidator;

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
    public Validator getPayeeValidator() {
        return payeeValidator;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the payeeValidator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param aPayeeValidator
     *            new value for the payeeValidator property
     */
    public void setPayeeValidator(final Validator aPayeeValidator) {
        payeeValidator = aPayeeValidator;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return BillPayForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        final BillPayForm billPayForm = (BillPayForm) obj;
        ValidationUtils.rejectIfEmpty(errors, "verifyAccount", "error.account.number.required");
        if (billPayForm.getFromAccountId() <= 0) {
            errors.rejectValue("fromAccountId", "from.account.number.required");
        }
        ValidationUtils.rejectIfEmpty(errors, "amount", "error.amount.empty");

        final Payee payee = billPayForm.getPayee();
        try {
            errors.pushNestedPath("payee");
            ValidationUtils.invokeValidator(payeeValidator, payee, errors);
        } finally {
            errors.popNestedPath();
        }

        if (payee.getAccountNumber() != null && !payee.getAccountNumber().equals(billPayForm.getVerifyAccount())) {
            errors.rejectValue("verifyAccount", "error.account.number.mismatch");
        }
    }
}
