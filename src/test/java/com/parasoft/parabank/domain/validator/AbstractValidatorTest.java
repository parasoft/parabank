package com.parasoft.parabank.domain.validator;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.*;
import org.springframework.beans.*;
import org.springframework.validation.*;

import com.parasoft.parabank.test.util.*;

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
