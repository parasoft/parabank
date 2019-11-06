package com.parasoft.parabank.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Account.AccountType;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.BillPayResult;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Payee;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.BankManager;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.controller.exception.AuthenticationException;

/**
 * Controller that behaves like a proxy. Restful calls made to /services_proxy/*
 * are routed here where they are then processed by the
 * {@link AccessModeController} or the {@link BankManager}
 */
@RestController
public class RestServiceProxyController extends AbstractBankController implements ServletContextAware{

    private static final Logger log = LoggerFactory.getLogger(RestServiceProxyController.class);

    @Autowired
    private MessageSource messageSource;

    private ServletContext context;

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @Override
    public void setAccessModeController(final AccessModeController accessModeController) {
        this.accessModeController = accessModeController;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        context = servletContext;
    }

    @RequestMapping(value = "bank/customers/{id}/accounts", method = RequestMethod.GET, produces = "application/json")
    public List<Account> getAccounts(@PathVariable(value = "id") Integer id) throws Exception {
        authenticate();
        List<Account> accounts;// = new ArrayList<Account>();

        String accessMode = null;

        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        final Customer customer = bankManager.getCustomer(id);
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            accounts = accessModeController.doGetAccounts(customer);
        } else {
            accounts = bankManager.getAccountsForCustomer(customer);
            log.warn("Using regular JDBC connection");
        }
        Collections.sort(accounts, Comparator.comparingInt(Account::getId));
        return accounts;
    }

    @RequestMapping(value = "bank/accounts/{id}", method = RequestMethod.GET, produces = "application/json")
    public Account getAccount(@PathVariable(value = "id") Integer id) throws Exception {
        authenticate();
        String accessMode = null;
        Account account;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            account = accessModeController.doGetAccount(id);
        } else {
            // default JDBC
            account = bankManager.getAccount(id);
        }
        return account;
    }

    @RequestMapping(value = "bank/accounts/{id}/transactions", method = RequestMethod.GET, produces = "application/json")
    public List<Transaction> getTransactions(@PathVariable(value = "id") Integer id) throws Exception {
        authenticate();
        String accessMode = null;
        List<Transaction> transactions;
        Account account = bankManager.getAccount(id);
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            transactions = accessModeController.getTransactionsForAccount(account, null);
        } else {
            // default JDBC
            transactions = bankManager.getTransactionsForAccount(account);
        }
        return transactions;
    }

    @RequestMapping(value = "bank/accounts/{id}/transactions/month/{month}/type/{type}", method = RequestMethod.GET, produces = "application/json")
    public List<Transaction> getTransactionsByMonthAndType(@PathVariable(value = "id") Integer id,
            @PathVariable(value = "month") String month, @PathVariable(value = "type") String type) throws Exception {
        authenticate();
        String accessMode = null;
        List<Transaction> transactions;
        Account account = bankManager.getAccount(id);
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        TransactionCriteria criteria = new TransactionCriteria();
        criteria.setSearchType(SearchType.ACTIVITY);
        criteria.setTransactionType(type);
        criteria.setMonth(month);
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            transactions = accessModeController.getTransactionsForAccount(account, criteria);
        } else {
            // default JDBC
            transactions = bankManager.getTransactionsForAccount(id, criteria);
        }
        return transactions;
    }

    @RequestMapping(value = "bank/customers/{customerId}", method = RequestMethod.GET, produces = "application/json")
    public Customer getCustomer(@PathVariable(value = "customerId") Integer customerId) throws Exception {
        authenticate();
        String accessMode = null;
        Customer customer;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            customer = accessModeController.doGetCustomer(customerId);
        } else {
            // default JDBC
            customer = bankManager.getCustomer(customerId);
        }
        return customer;
    }

    @RequestMapping(value = "bank/customers/update/{customerId}", method = RequestMethod.POST, produces = "application/json")
    public String updateCustomer(@PathVariable(value = "customerId") Integer customerId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("street") String street,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("zipCode") String zipCode,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("ssn") String ssn,
            @RequestParam("username") String username,
            @RequestParam("password") String password) throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        Customer updatedCustomer = new Customer();
        Address customerAddress = new Address();
        customerAddress.setStreet(street);
        customerAddress.setCity(city);
        customerAddress.setState(state);
        customerAddress.setZipCode(zipCode);
        updatedCustomer.setAddress(customerAddress);
        updatedCustomer.setFirstName(firstName);
        updatedCustomer.setLastName(lastName);
        updatedCustomer.setId(customerId);
        updatedCustomer.setPhoneNumber(phoneNumber);
        updatedCustomer.setSsn(ssn);
        updatedCustomer.setUsername(username);
        updatedCustomer.setPassword(password);
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            accessModeController.updateCustomer(updatedCustomer);
        } else {
            bankManager.updateCustomer(updatedCustomer);
        }
        return "Successfully updated customer profile";
    }

    @RequestMapping(value = "bank/createAccount", method = RequestMethod.POST, produces = "application/json")
    public Account createAccount(@RequestParam("customerId") Integer customerId,
            @RequestParam("newAccountType") Integer newAccountType,
            @RequestParam("fromAccountId") Integer fromAccountId) throws Exception {
        authenticate();
        Account newAccount;
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            newAccount = accessModeController.createAccount(customerId, newAccountType, fromAccountId);
        } else {
            // default JDBC
            newAccount = new Account();
            newAccount.setCustomerId(customerId);
            newAccount.setType(AccountType.values()[newAccountType]);
            newAccount.setBalance(BigDecimal.ZERO);
            bankManager.createAccount(newAccount, fromAccountId);
        }
        return newAccount;
    }

    @RequestMapping(value = "bank/transfer", method = RequestMethod.POST, produces = "application/json")
    public String transfer(@RequestParam("fromAccountId") Integer fromAccountId,
            @RequestParam("toAccountId") Integer toAccountId, @RequestParam("amount") BigDecimal amount)
            throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            accessModeController.doTransfer(fromAccountId, toAccountId, amount);
        } else {
            // default JDBC
            bankManager.transfer(fromAccountId, toAccountId, amount);
        }
        return "Successfully transferred $" + amount + " from account #" + fromAccountId + " to account #"
                + toAccountId;
    }

    @RequestMapping(value = "bank/billpay", method = RequestMethod.POST, produces = "application/json")
    public BillPayResult billPay(@RequestParam("accountId") Integer accountId,
            @RequestParam("amount") BigDecimal amount, @RequestBody Payee payee)
            throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            accessModeController.doBillPay(accountId, amount, payee);
        } else {
            // default JDBC
            bankManager.withdraw(accountId, amount, String.format("Bill Payment to %s", payee.getName()));
        }
        return new BillPayResult(accountId, amount, payee.getName());
    }

    @RequestMapping(value = "bank/requestLoan", method = RequestMethod.POST, produces = "application/json")
    public LoanResponse requestLoan(@RequestParam("customerId") Integer customerId,
            @RequestParam("amount") BigDecimal amount, @RequestParam("downPayment") BigDecimal downPayment,
            @RequestParam("fromAccountId") Integer fromAccountId) throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        LoanResponse loanResponse;
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            loanResponse = accessModeController.doRequestLoan(customerId, amount, downPayment, fromAccountId);
        } else {
            // default JDBC
            loanResponse = bankManager.requestLoan(customerId, amount, downPayment, fromAccountId);
        }
        return loanResponse;
    }

    @RequestMapping(value = "bank/accounts/{accountId}/transactions/onDate/{onDate}", method = RequestMethod.GET, produces = "application/json")
    public List<Transaction> getTransactionsOnDate(@PathVariable(value = "accountId") Integer accountId,
            @PathVariable(value = "onDate") String onDate) throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        TransactionCriteria criteria = new TransactionCriteria();
        criteria.setOnDate(TransactionCriteria.DATE_FORMATTER.get().parse(onDate));
        criteria.setSearchType(SearchType.DATE);
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            Account account = bankManager.getAccount(accountId);
            return accessModeController.getTransactionsForAccount(account, criteria);
        } else {
            // default JDBC
            return bankManager.getTransactionsForAccount(accountId, criteria);
        }
    }

    @RequestMapping(value = "bank/accounts/{accountId}/transactions/fromDate/{fromDate}/toDate/{toDate}", method = RequestMethod.GET, produces = "application/json")
    public List<Transaction> getTransactionsByToFromDate(@PathVariable(value = "accountId") Integer accountId,
            @PathVariable(value = "fromDate") String fromDate, @PathVariable(value = "toDate") String toDate)
            throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        TransactionCriteria criteria = new TransactionCriteria();
        criteria.setToDate(TransactionCriteria.DATE_FORMATTER.get().parse(toDate));
        criteria.setFromDate(TransactionCriteria.DATE_FORMATTER.get().parse(fromDate));
        criteria.setSearchType(SearchType.DATE_RANGE);
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            Account account = bankManager.getAccount(accountId);
            return accessModeController.getTransactionsForAccount(account, criteria);
        } else {
            // default JDBC
            return bankManager.getTransactionsForAccount(accountId, criteria);
        }
    }

    @RequestMapping(value = "bank/accounts/{accountId}/transactions/amount/{amount}", method = RequestMethod.GET, produces = "application/json")
    public List<Transaction> getTransactionsByAmount(@PathVariable(value = "accountId") Integer accountId,
            @PathVariable(value = "amount") BigDecimal amount) throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        TransactionCriteria criteria = new TransactionCriteria();
        criteria.setAmount(amount);
        criteria.setSearchType(SearchType.AMOUNT);
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            Account account = bankManager.getAccount(accountId);
            return accessModeController.getTransactionsForAccount(account, criteria);
        } else {
            // default JDBC
            return bankManager.getTransactionsForAccount(accountId, criteria);
        }
    }

    @RequestMapping(value = "bank/swagger.yaml")
    public ResponseEntity<String> getSwagger(HttpServletRequest request) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResourceAsStream("/WEB-INF/swagger.yaml")))) {
            String host = request.getServerName() + ':' + request.getServerPort();
            String content = br.lines().collect(Collectors.joining(System.lineSeparator())).replaceFirst("replace-host-name", host);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/yaml");
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "bank/transactions/{transactionId}", method = RequestMethod.GET, produces = "application/json")
    public Transaction getTransaction(@PathVariable(value = "transactionId") Integer transactionId) throws Exception {
        authenticate();
        String accessMode = null;
        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            return accessModeController.doGetTransaction(transactionId);
        } else {
            // default JDBC
            return bankManager.getTransaction(transactionId);
        }
    }

    private void authenticate() throws Exception {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String authHeader = request.getHeader("Authorization");
        Locale locale = LocaleContextHolder.getLocale();
        if (authHeader == null) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                throw new AuthenticationException(messageSource.getMessage("user.login.required", null, locale));
            }
            Object result = session.getAttribute("userSession");
            if (result == null || !(result instanceof UserSession)) {
                throw new AuthenticationException(messageSource.getMessage("user.login.required", null, locale));
            }
            UserSession userSession = (UserSession) result;
            if (userSession.getCustomer() == null) {
                throw new AuthenticationException(messageSource.getMessage("user.login.required", null, locale));
            }
        } else {
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();
                if (basic.equalsIgnoreCase("Basic")) {
                    try {
                        String credentials = new String(Base64.getDecoder().decode(st.nextToken()), "UTF-8");
                        int p = credentials.indexOf(":");
                        if (p != -1) {
                            String username = credentials.substring(0, p).trim();
                            String password = credentials.substring(p + 1).trim();
                            Customer customer = accessModeController.login(username, password);
                            if (customer == null) {
                                throw new AuthenticationException(
                                        messageSource.getMessage("error.invalid.username.or.password", null, locale));
                            }
                        } else {
                            throw new AuthenticationException(
                                    messageSource.getMessage("error.invalid.username.or.password", null, locale));
                        }
                    } catch (IllegalArgumentException e) {
                        throw new AuthenticationException(
                                messageSource.getMessage("error.invalid.username.or.password", null, locale));
                    }
                }
            }
        }

    }
}
