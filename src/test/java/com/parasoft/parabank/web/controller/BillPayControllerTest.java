package com.parasoft.parabank.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.Payee;
import com.parasoft.parabank.domain.validator.AddressValidator;
import com.parasoft.parabank.domain.validator.PayeeValidator;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.BillPayForm;

/**
 * @req PAR-11
 * @req PAR-12
 * @req PAR-15
 *
 */
@SuppressWarnings({ "unchecked" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/**/test-context.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@Transactional
public class BillPayControllerTest {

    private static final String BILLPAY_CONFIRM = "billpayConfirm";

    private static final String PATH = "/billpay.htm";

    @Autowired
    protected BillPayController controller;

    @Autowired
    private ApplicationContext applicationContext;

    MockMvc mockMvc;

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
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setCustomArgumentResolvers(new SessionParamArgumentResolver()).build();
    }

    /**
     * This test should verify that a new customer who doesn't have an ID
     */
    @Test
    public void testGetForm()
        throws Throwable
    {
        mockMvc.perform(get(PATH)
            .sessionAttr(Constants.USERSESSION, new UserSession(new Customer())))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.BILLPAY))
                .andExpect(model().attributeExists(Constants.BILLPAYFORM))
                .andExpect(model().hasNoErrors());
    }

    /**
     * This test should verify that a customer who has an ID
     */
    @Test
    public void testHandleGetRequest() throws Exception
    {
        Customer customer = new Customer();
        customer.setId(12212);
        mockMvc.perform(get(PATH)
            .sessionAttr(Constants.USERSESSION, new UserSession(customer)))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.BILLPAY))
                .andExpect(model().attributeExists(Constants.BILLPAYFORM))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("accounts", iterableWithSize(11)));
    }

    /**
     * This test should verify that a user successfully submits a form
     */
    @Test
    @Transactional
    @Rollback
    public void testOnSubmit()
        throws Throwable
    {
        mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .sessionAttr(Constants.BILLPAYFORM, getBillPayForm())
            .sessionAttr(Constants.USERSESSION, new UserSession(new Customer())))
                .andExpect(status().isOk())
                .andExpect(view().name(BILLPAY_CONFIRM))
                .andExpect(model().attribute("payeeName", "payee name"))
                .andExpect(model().attribute("fromAccountId", 12345))
                .andExpect(model().attribute("amount", new BigDecimal("100.0")));
    }

    /**
     * This test should verify that a user fails to submit an incomplete form
     */
    @Test
    @Transactional
    @Rollback
    public void testValidate() throws Exception
    {
        BillPayForm form = getBillPayForm();
        form.getPayee().setPhoneNumber(null);
        mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .sessionAttr(Constants.BILLPAYFORM, form)
            .sessionAttr(Constants.USERSESSION, new UserSession(new Customer())))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode(Constants.BILLPAYFORM, "payee.phoneNumber", "error.phone.number.required"));

        form = getBillPayForm();
        form.setAmount(null);
        mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .sessionAttr(Constants.BILLPAYFORM, form)
            .sessionAttr(Constants.USERSESSION, new UserSession(new Customer())))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode(Constants.BILLPAYFORM, "amount", "error.amount.empty"));

        form = getBillPayForm();
        form.getPayee().setAccountNumber(null);
        mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .sessionAttr(Constants.BILLPAYFORM, form)
            .sessionAttr(Constants.USERSESSION, new UserSession(new Customer())))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode(Constants.BILLPAYFORM, "payee.accountNumber", "error.account.number.required"));

        form = getBillPayForm();
        form.setVerifyAccount(200);
        mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .sessionAttr(Constants.BILLPAYFORM, form)
            .sessionAttr(Constants.USERSESSION, new UserSession(new Customer())))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode(Constants.BILLPAYFORM, "verifyAccount", "error.account.number.mismatch"));
    }
}

//"name", "error.payee.name.required"
//"phoneNumber", "error.phone.number.required"
//"accountNumber", "error.account.number.required"
//"amount", "error.amount.empty"
//"verifyAccount", "error.account.number.mismatch"
