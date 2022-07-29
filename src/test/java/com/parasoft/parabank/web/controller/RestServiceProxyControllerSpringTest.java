
package com.parasoft.parabank.web.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.logic.BankManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/**/service-proxy-test-context.xml"})
@WebAppConfiguration
@Transactional
public class RestServiceProxyControllerSpringTest
{

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Resource(name = "bankManager")
    protected BankManager bankManager;

    private final int customerId = 12212;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    public final void setBankManager(final BankManager bankManager)
    {
        this.bankManager = bankManager;
    }

    @Test
    public void testGetAccounts()
        throws Exception
    {
        Customer customer = bankManager.getCustomer(customerId);
        List<Account> accounts = bankManager.getAccountsForCustomer(customer);
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        mockMvc.perform(get("/bank/customers/" + customerId + "/accounts").with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(accounts.size())));
    }

    @Test
    public void testGetAccount()
        throws Exception
    {
        mockMvc
            .perform(get("/bank/accounts/12456").with(createUserToken()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.balance", is(10.45))).andExpect(jsonPath("$.customerId", is(customerId)))
            .andExpect(jsonPath("$.type", is("CHECKING")));
    }

    @Test
    public void testGetTransactions()
        throws Exception
    {
        Account account = bankManager.getAccount(12456);
        assertNotNull(account);
        List<Transaction> transactions = bankManager.getTransactionsForAccount(account);
        assertNotNull(transactions);
        mockMvc.perform(get("/bank/accounts/12456/transactions").with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(transactions.size())));
    }

    @Test
    public void testTransferAndGetTransactionByMonthAndType()
        throws Exception
    {
        // perform a transaction (i.e. transfer 10 from account 12567 to 12456)
        mockMvc.perform(post("/bank/transfer").with(createUserToken()).contentType(MediaType.APPLICATION_JSON).param("fromAccountId", "12567")
            .param("toAccountId", "12456").param("amount", "10.00").content("").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        // GET for transactions should return the transfer from the POST
        mockMvc
            .perform(get("/bank/accounts/12567/transactions/month/All/type/All").with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].amount", is(10.0)))
            .andExpect(jsonPath("$[0].description", is("Funds Transfer Sent"))).andExpect(jsonPath("$[0].type", is("Debit")));

        // GET for transactions of type 'Credit' should return empty since
        // transfer is a 'Debit' transaction
        mockMvc.perform(get("/bank/accounts/12567/transactions/month/All/type/Credit").with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testBillPay()
        throws Exception
    {
        mockMvc
            .perform(get("/bank/accounts/12345").with(createUserToken()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.balance", is(-2300.0)));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode address = objectMapper.createObjectNode();
        address.put("street", "Myrtle Ave");
        address.put("city", "Monrovia");
        address.put("state", "CA");
        address.put("zipCode", "91016");
        ObjectNode json = objectMapper.createObjectNode();
        json.put("name", "Jim");
        json.set("address", address);
        json.put("phoneNumber", "6262626262");
        json.put("accountNumber", 12345);
        String responseStr = mockMvc
            .perform(post("/bank/billpay").with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
            .param("accountId", "12345")
            .param("amount", "10")
            .content(json.toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("{\"payeeName\":\"Jim\",\"amount\":10,\"accountId\":12345}", responseStr);

        mockMvc
            .perform(get("/bank/accounts/12345").with(createUserToken()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.balance", is(-2310.0)));
    }

    @Test
    public void testUpdateCustomer()
        throws Exception
    {
        Customer customer = bankManager.getCustomer(customerId);
        Address addr = customer.getAddress();
        mockMvc
            .perform(post("/bank/customers/update/" + customerId).with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
            .param("customerId", "" + customerId)
            .param("firstName", customer.getFirstName())
            .param("lastName", customer.getLastName())
            .param("street", addr.getStreet())
            .param("city", addr.getCity())
            .param("state", addr.getState())
            .param("zipCode", addr.getZipCode())
            .param("phoneNumber", customer.getPhoneNumber())
            .param("ssn", "888888888")
            .param("username", customer.getUsername())
            .param("password", customer.getPassword())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        Customer updatedCustomer = bankManager.getCustomer(customerId);
        assertEquals("888888888", updatedCustomer.getSsn());
    }

    @Test
    public void testGetCustomer()
        throws Exception
    {
        Customer customer = bankManager.getCustomer(customerId);
        Address addr = customer.getAddress();
        mockMvc
            .perform(get("/bank/customers/" + customerId).with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(customer.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(customer.getLastName())))
            .andExpect(jsonPath("$.address.street", is(addr.getStreet())))
            .andExpect(jsonPath("$.address.city", is(addr.getCity())))
            .andExpect(jsonPath("$.address.state", is(addr.getState())))
            .andExpect(jsonPath("$.address.zipCode", is(addr.getZipCode())))
            .andExpect(jsonPath("$.phoneNumber", is(customer.getPhoneNumber())))
            .andExpect(jsonPath("$.ssn", is(customer.getSsn())));
    }

    @Test
    public void testCreateAccount()
        throws Exception
    {
        mockMvc
            .perform(post("/bank/createAccount").with(createUserToken()).contentType(MediaType.APPLICATION_JSON).param("customerId", "" + customerId)
                .param("newAccountType", "0").param("fromAccountId", "13122").content("").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.balance", is(0))).andExpect(jsonPath("$.customerId", is(customerId)))
            .andExpect(jsonPath("$.type", is("CHECKING")));
    }

    @Test
    public void testRequestLoan()
        throws Exception
    {
        mockMvc
            .perform(post("/bank/requestLoan").with(createUserToken()).contentType(MediaType.APPLICATION_JSON).param("customerId", "" + customerId)
                .param("amount", "1000").param("downPayment", "200").param("fromAccountId", "13122").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.approved", is(true)));
    }

    @Test
    public void testGetTransaction()
        throws Exception
    {
        int accountId = 12567;
        Transaction deposit = bankManager.deposit(accountId, BigDecimal.valueOf(1000.00), "Funds Transfer Received");
        mockMvc.perform(get("/bank/transactions/" + deposit.getId()).with(createUserToken()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(deposit.getId())));
    }

    @Test
    public void testGetTransactionsByAmount()
        throws Exception
    {
        int accountId = 12567;
        BigDecimal amount = new BigDecimal(2000);
        Transaction deposit = bankManager.deposit(accountId, amount, "Funds Transfer Received");
        mockMvc
            .perform(get("/bank/accounts/" + accountId + "/transactions/amount/" + amount).with(createUserToken())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.[?(@.id == '" + deposit.getId() + "')]", hasSize(1)));
    }

    @Test
    public void testGetTransactionsOnDate()
        throws Exception
    {
        int accountId = 12567;
        BigDecimal amount = new BigDecimal(2000);
        String date = TransactionCriteria.DATE_FORMATTER.get().format(new Date());
        Transaction deposit = bankManager.deposit(accountId, amount, "Funds Transfer Received");
        mockMvc
            .perform(get("/bank/accounts/" + accountId + "/transactions/onDate/" + date).with(createUserToken())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$.[?(@.id == '" + deposit.getId() + "')]", hasSize(1)));
    }

    private static RequestPostProcessor createUserToken()
    {
        org.apache.xml.security.Init.init();
        return request -> {
            try {
                String username = "john";
                String password = "demo";
                String credentials = new String(Base64.getEncoder().encode((username + ":" + password).getBytes("UTF-8")), "UTF-8");
                request.addHeader("Authorization", "Basic " + credentials);
                return request;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
