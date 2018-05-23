package com.parasoft.parabank.web.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Account.AccountType;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.BankManager;
import com.parasoft.parabank.util.AccessModeController;

/**
 * Controller that behaves like a proxy. Restful calls made to /services_proxy/* are
 * routed here where they are then processed by the {@link AccessModeController} or the
 * {@link BankManager}
 */
@RestController
public class RestServiceProxyController extends AbstractBankController{
	
	private static final Logger log = LoggerFactory.getLogger(RestServiceProxyController.class);
	
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
	
	@RequestMapping(value="bank/customers/{id}/accounts", method = RequestMethod.GET, produces = "application/json")
	public List<Account> getAccounts(@PathVariable(value = "id") Integer id) throws Exception {
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
		return accounts;
	}
	
	@RequestMapping(value="bank/accounts/{id}",method = RequestMethod.GET, produces = "application/json")
	public Account getAccount(@PathVariable(value = "id") Integer id) throws Exception {
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
	
	@RequestMapping(value="bank/accounts/{id}/transactions",method = RequestMethod.GET, produces = "application/json")
	public List<Transaction> getTransactions(@PathVariable(value = "id") Integer id) throws Exception {
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
	
	@RequestMapping(value="bank/accounts/{id}/transactions/month/{month}/type/{type}", method = RequestMethod.GET, produces = "application/json")
	public List<Transaction> getTransactionsByMonthAndType(
			@PathVariable(value = "id") Integer id, 
			@PathVariable(value = "month") String month,
			@PathVariable(value = "type") String type) throws Exception {
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
	
	@RequestMapping(value="bank/createAccount", method = RequestMethod.POST, produces = "application/json")
	public Account createAccount(
			@RequestParam("customerId") Integer customerId,
			@RequestParam("newAccountType") Integer newAccountType,
			@RequestParam("fromAccountId") Integer fromAccountId) throws Exception {
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
	
	@RequestMapping(value="bank/transfer", method = RequestMethod.POST, produces = "application/json")
	public String transfer(
			@RequestParam("fromAccountId") Integer fromAccountId,
			@RequestParam("toAccountId") Integer toAccountId,
			@RequestParam("amount") BigDecimal amount) throws Exception {
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
	
	@RequestMapping(value ="bank/requestLoan", method = RequestMethod.POST, produces = "application/json")
	public LoanResponse requestLoan(
			@RequestParam("customerId") Integer customerId,
			@RequestParam("amount") BigDecimal amount,
			@RequestParam("downPayment") BigDecimal downPayment,
			@RequestParam("fromAccountId") Integer fromAccountId) throws Exception
	{
		String accessMode = null;
		if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
		if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
			return accessModeController.doRequestLoan(customerId, amount, downPayment, fromAccountId);
		} else {
			// default JDBC
			return bankManager.requestLoan(customerId, amount, downPayment, fromAccountId);
		}
	}

}
