package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;

import javax.annotation.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.mock.web.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.support.*;
import org.springframework.test.context.transaction.*;
import org.springframework.transaction.annotation.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.util.*;
import com.parasoft.parabank.web.*;
import com.parasoft.parabank.web.form.*;

@SuppressWarnings({ "unchecked" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/**/test-context.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@Transactional
public class BillPayControllerTest {

    @Autowired
    protected BillPayController controller;

    @Autowired
    private ApplicationContext applicationContext;

    protected MockHttpServletRequest request;

    protected MockHttpServletResponse response;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private String path;

    private String formName;

    private String resultClassName = BindingResult.class.getName();

    @Resource(name = "bankManager")
    protected BankManager bankManager;

    @Resource(name = "accessModeController")
    protected AccessModeController amc;

    private void assertReferenceData(final ModelAndView mav) {
        final List<Account> accounts = (List<Account>) mav.getModel().get("accounts");
        assertEquals(11, accounts.size());
    }

    private BillPayForm getBillPayForm() {
        final BillPayForm form = new BillPayForm();
        final Payee payee = new Payee();
        payee.setName("payee name");
        final Address address = new Address();
        address.setStreet("payee street");
        address.setCity("payee city");
        address.setState("payee state");
        address.setZipCode("payee zipcode");
        payee.setAddress(address);
        payee.setPhoneNumber("payee phone number");
        payee.setAccountNumber(100);
        form.setPayee(payee);
        form.setVerifyAccount(100);
        form.setAmount(new BigDecimal("100.0"));
        form.setFromAccountId(12345);
        return form;
    }

    @Before
    public void onSetUp() throws Exception {
        controller.setMessageSource(applicationContext);
        path = "/billpay.htm";
        formName = Constants.BILLPAYFORM;
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        registerSession(request);
    }

    protected final MockHttpServletRequest registerSession(final MockHttpServletRequest aRequest) {
        return registerSession(aRequest, 12212);
    }

    protected final MockHttpServletRequest registerSession(final MockHttpServletRequest aRequest, final int custId) {
        final MockHttpSession session = new MockHttpSession();
        final Customer customer = new Customer();
        customer.setId(custId);
        session.setAttribute(Constants.USERSESSION, new UserSession(customer));
        aRequest.setSession(session);
        return aRequest;
    }

    protected ModelAndView processPostRequest(final Object form, final MockHttpServletRequest aRequest, final MockHttpServletResponse aResponse)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
        Object handler;
        ModelAndView mav;
        aRequest.setMethod("POST");
        aRequest.setServletPath(path);
        if (form != null) {
            aRequest.getSession().setAttribute(formName, form);
        }
        // aRequest.setParameters(BeanUtils.describe(form));
        handler = handlerMapping.getHandler(aRequest).getHandler();
        mav = handlerAdapter.handle(aRequest, aResponse, handler);
        return mav;
    }

    protected ModelAndView processGetRequest(final MockHttpServletRequest aRequest, final MockHttpServletResponse aResponse)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
        Object handler;
        ModelAndView mav;
        aRequest.setMethod("GET");
        aRequest.setServletPath(path);
        final HandlerExecutionChain chain = handlerMapping.getHandler(aRequest);
        handler = chain.getHandler();
        mav = handlerAdapter.handle(aRequest, aResponse, handler);
        return mav;
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final BillPayForm form = (BillPayForm) mav.getModel().get(formName);
        assertNotNull("BillPayForm was not returned by a get request ", form);
        //final ModelAndView mav = controller.handleRequest(request, response);
        assertReferenceData(mav);
    }

    protected final Object getModelValue(final ModelAndView mav, final String name) {
        final ModelMap model = mav.getModelMap();
        final Map<String, Object> map = (Map<String, Object>) model.get("model");
        return map.get(name);
    }

    @Test
    @Transactional
    @Rollback
    public void testOnSubmit() throws Exception {
        final BillPayForm form = getBillPayForm();
        //final BindException errors = new BindException(form, Constants.BILLPAYFORM);
        //final ModelAndView mav = controller.onSubmit(request, response, form, errors);
        final ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertEquals("billpayConfirm", mav.getViewName());
        assertEquals("payee name", getModelValue(mav, "payeeName"));
        assertEquals(new BigDecimal("100.0"), getModelValue(mav, "amount"));
        assertEquals(12345, getModelValue(mav, "fromAccountId"));
    }

    @Test
    @Transactional
    @Rollback
    public void testValidate() throws Exception {
        BillPayForm form = getBillPayForm();
        form.getPayee().setPhoneNumber(null);
        ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "payee.phoneNumber", "error.phone.number.required");

        form = getBillPayForm();
        form.setAmount(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "amount", "error.amount.empty");

        form = getBillPayForm();
        form.getPayee().setAccountNumber(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "payee.accountNumber", "error.account.number.required");

        form = getBillPayForm();
        form.setVerifyAccount(200);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "verifyAccount", "error.account.number.mismatch");
    }

    protected void assertError(final ModelAndView mav, final int errorCount, final Map<String, String> fieldErrors) {
        final String errorName = String.format("%1$s.%2$s", resultClassName, formName);
        final BindingResult errors = (BindingResult) mav.getModel().get(errorName);
        assertEquals(errorCount, errors.getErrorCount());
        for (final String fieldName : fieldErrors.keySet()) {
            final FieldError fe = errors.getFieldError(fieldName);
            assertNotNull("missing FieldError for " + fieldName, fe);
            assertEquals(fieldName, fe.getField());
            assertEquals(fieldErrors.get(fieldName), fe.getCode());
        }
        // return errorName;
    }

    protected void assertError(final ModelAndView mav, final String fieldName, final String errorCode) {
        final Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put(fieldName, errorCode);
        assertError(mav, 1, fieldErrors);
    }
}
