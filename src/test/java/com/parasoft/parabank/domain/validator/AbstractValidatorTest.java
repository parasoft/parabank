package com.parasoft.parabank.domain.validator;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

import com.parasoft.parabank.test.util.AbstractBeanTestCase;
import com.parasoft.parabank.test.util.AbstractParaBankTest;

public abstract class AbstractValidatorTest extends AbstractParaBankTest {
    private Class<?> beanClass;

    private String[] requiredFields;

    public AbstractValidatorTest(Class<?> beanClass, String[] requiredFields) {
        this.beanClass = beanClass;
        this.requiredFields = requiredFields;
    }

    protected abstract Validator getValidator();

    @Test
    public void testSupports() {
        assertTrue(getValidator().supports(beanClass));
        assertFalse(getValidator().supports(Object.class));
    }

    @Test
    public void testValidate() throws Exception {
        BeanWrapper wrapper = new BeanWrapperImpl(beanClass);
        assertValidate(getValidator(), wrapper, requiredFields);
    }

    static final void assertValidate(Validator validator, Object target, String[] requiredFields) throws Exception {
        Set<String> requiredFieldSet = new HashSet<String>(Arrays.asList(requiredFields));
        Field[] fields = target.getClass().getDeclaredFields();
        int expectedErrorCount = fields.length;

        for (Field field : fields) {
            if (!requiredFieldSet.contains(field.getName())) {
                expectedErrorCount--;
            }
            if (!field.getType().isEnum() && field.getType().getPackage() != null
                && !field.getType().getPackage().getName().startsWith("java")) {
                expectedErrorCount += field.getType().getDeclaredFields().length;
            }
        }

        for (Field field : fields) {
            if (!requiredFieldSet.contains(field.getName())) {
                continue;
            }

            field.setAccessible(true);

            BindException errors = new BindException(target, "target");

            validator.validate(target, errors);
            assertNull(field.get(target));
            assertNotNull("expected error for empty field: " + field.getName(), errors.getFieldError(field.getName()));
            assertEquals(expectedErrorCount, errors.getErrorCount());

            AbstractBeanTestCase.toggleField(field, target, true);

            errors = new BindException(target, "target");
            validator.validate(target, errors);
            assertNotNull(field.get(target));
            assertNull("expected error for non-empty field: " + field.getName(), errors.getFieldError(field.getName()));
            assertEquals(--expectedErrorCount, errors.getErrorCount());
        }
    }
}
