package com.parasoft.parabank.web.controller;

// import static org.junit.Assert.*;

public abstract class AbstractValidatingBankControllerTest<T extends AbstractValidatingBankController>
        extends AbstractBankControllerTest<T> {

    //    protected final void assertError(final Object form, final String... fields) throws Exception {
    //        //final BindException errors = new BindException(form, "");
    //        request.setAttribute(getFormName(), form);
    //        final Object handler = getHandlerMapping().getHandler(request).getHandler();
    //        ModelAndView mav = getHandlerAdapter().handle(request, response, handler);
    //        assertEquals(fields.length, errors.getErrorCount());
    //        for (final String field : fields) {
    //            assertNotNull("Did not get expected error for field: " + field, errors.getFieldError(field));
    //        }
    //    }

}
