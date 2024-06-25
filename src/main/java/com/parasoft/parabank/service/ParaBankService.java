package com.parasoft.parabank.service;

import java.math.BigDecimal;
import java.util.List;

import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.xml.ElementClass;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Position;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.util.Constants;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Java interface for ParaBank web service
 */
@Path("/")
@OpenAPIDefinition( //
servers = @Server(url = "/parabank/services/bank"),
info = @Info( //
description = "This API provides access to various ParaBank internal operations", //
version = "3.0.0", //
title = "The ParaBank REST API", //
termsOfService = "/parabank/about.htm", //
contact = @Contact( //
name = "ParaBank Web Administrator", //
email = "webadmin@parabank.parasoft.com", //
url = "http://www.parasoft.com") , //
license = @License(name = "Apache 2.0", //
url = "http://www.apache.org/licenses/LICENSE-2.0") //
) , //
tags = { //
    @Tag(name = ParaBankServiceConstants.ACCOUNTS, description = "account centric operations"),
    @Tag(name = ParaBankServiceConstants.CUSTOMERS, description = "customer centric operations"),
    @Tag(name = ParaBankServiceConstants.DATABASE, description = "data management operations"),
    @Tag(name = ParaBankServiceConstants.JMS, description = "message service operations"),
    @Tag(name = ParaBankServiceConstants.LOANS, description = "request loan operations"),
    @Tag(name = ParaBankServiceConstants.MISC, description = "miscelaneous operations"),
    @Tag(name = ParaBankServiceConstants.POSITIONS, description = "stock centric operations"),
    @Tag(name = ParaBankServiceConstants.TRANSACTIONS, description = "banking transactions centric operations") //
})
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@CrossOriginResourceSharing(allowAllOrigins = true)
@WebService(targetNamespace = ParaBankServiceConstants.TNS)
public interface ParaBankService extends IBillPayService {

    /**
     * Buy a position
     *
     * @param customerId
     *            the customer id to purchase the position
     * @param accountId
     *            the account from which to withdraw funds
     * @param name
     *            the name of the stock position company
     * @param symbol
     *            the symbol of the stock position company
     * @param shares
     *            the number of shares to purchase
     * @param pricePerShare
     *            the price per share of the stock position
     * @return a list of positions for the customer
     * @throws ParaBankServiceException
     */
    @POST
    @Path("/customers/{customerId}/buyPosition")
    @Operation(summary = "Buy a Position", tags = {
        ParaBankServiceConstants.POSITIONS })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @WebResult(name = "position", targetNamespace = ParaBankServiceConstants.TNS)
    List<Position> buyPosition(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @PathParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId,
        @Parameter(description = CUSTOMER_ACCOUNT_DESC, required = true) @QueryParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = INSTRUMENT_NAME, required = true) @QueryParam("name") @WebParam(name = "name", targetNamespace = ParaBankServiceConstants.TNS) String name,
        @Parameter(description = INSTRUMENT_SYMBOL, required = true) @QueryParam("symbol") @WebParam(name = "symbol", targetNamespace = ParaBankServiceConstants.TNS) String symbol,
        @Parameter(description = NUMBER_OF_SHARES_DESC, required = true) @QueryParam("shares") @WebParam(name = "shares", targetNamespace = ParaBankServiceConstants.TNS) int shares,
        @Parameter(description = PRICE_PER_SHARE_DESC, required = true) @QueryParam("pricePerShare") @WebParam(name = "pricePerShare", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal pricePerShare)
                throws ParaBankServiceException;

    /**
     * Reset database contents to a minimal state
     */
    @POST
    @Operation(summary = "Clean the Database", tags = { ParaBankServiceConstants.DATABASE })
    @Path("/cleanDB")
    void cleanDB();

    @POST
    @Path("/createAccount")
    @Operation(summary = "Create a new account", tags = { ParaBankServiceConstants.CUSTOMERS,
        ParaBankServiceConstants.ACCOUNTS })
    @WebResult(name = "account", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Account createAccount(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @QueryParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId,
        @Parameter(description = ACCOUNT_TYPE_DESC, required = true) @QueryParam("newAccountType") @WebParam(name = "newAccountType", targetNamespace = ParaBankServiceConstants.TNS) int newAccountType,
        @Parameter(description = CUSTOMER_ACCOUNT_DESC, required = true) @QueryParam("fromAccountId") @WebParam(name = "fromAccountId", targetNamespace = ParaBankServiceConstants.TNS) int fromAccountId)
                throws ParaBankServiceException;

    /**
     * Deposit funds into the given account
     *
     * @param accountId
     *            the account to which to deposit funds
     * @param amount
     *            the amount to deposit
     * @return status message of result
     * @throws ParaBankServiceException
     */
    @POST
    @Path("/deposit")
    @Operation(summary = "Deposit funds", tags = { ParaBankServiceConstants.ACCOUNTS })
    @WebResult(name = "depositReturn", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    String deposit(
        @Parameter(description = CUSTOMER_ACCOUNT_DEP_DESC, required = true) @QueryParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = AMOUNT_DESC, required = true) @QueryParam("amount") @WebParam(name = "amount", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal amount)
                throws ParaBankServiceException;

    /**
     * Return account information for a given account number
     *
     * @param accountId
     *            the account id to lookup
     * @return the account
     * @throws ParaBankServiceException
     */
    @GET
    @Path("/accounts/{accountId}")
    @Operation(summary = "Get Account by Id", tags = { ParaBankServiceConstants.ACCOUNTS })
    @WebResult(name = "account", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Account getAccount(
        @Parameter(description = CUSTOMER_ACCOUNT_FETCH_DESC, required = true) @PathParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId)
                throws ParaBankServiceException;

    /**
     * Return a list of accounts for a given customer
     *
     * @param customerId
     *            the customer id to lookup
     * @return list of customer accounts
     * @throws ParaBankServiceException
     */
    @GET
    @Operation(summary = "Get Customer Accounts", tags = {
        ParaBankServiceConstants.CUSTOMERS, ParaBankServiceConstants.ACCOUNTS })
    @Path("/customers/{customerId}/accounts")
    @WebResult(name = "account", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Account> getAccounts(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @PathParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId)
                throws ParaBankServiceException;

    /**
     * Return customer information for the given customer number
     *
     * @param customerId
     *            the customer id to lookup
     * @return the customer
     * @throws ParaBankServiceException
     */
    @GET
    @Path("/customers/{customerId}")
    @Operation(summary = "Get Customer Details", tags = {
        ParaBankServiceConstants.CUSTOMERS })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @WebResult(name = "customer", targetNamespace = ParaBankServiceConstants.TNS)
    Customer getCustomer(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @PathParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId)
                throws ParaBankServiceException;

    /**
     * Return a position for a given position number
     *
     * @param positionId
     *            the position id to lookup
     * @return the position
     * @throws ParaBankServiceException
     */
    @GET
    @Operation(summary = "Get Position by id", tags = {
        ParaBankServiceConstants.POSITIONS })
    @Path("/positions/{positionId}")
    @WebResult(name = "position", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Position getPosition(
        @Parameter(description = POSITION_ID_DESC, required = true) @PathParam("positionId") @WebParam(name = "positionId", targetNamespace = TNS) int positionId)
                throws ParaBankServiceException;

    /**
     * Return position history for a given position id and date range
     *
     * @param positionId
     *            the position id
     * @param startDate
     *            the start date in the date range
     * @param endDate
     *            the end date in the date range
     * @return a list of history points
     * @throws ParaBankServiceException
     */
    @GET
    @Path("/positions/{positionId}/{startDate}/{endDate}")
    @Operation(summary = "Get Position history by id within a date range", tags = {
        ParaBankServiceConstants.POSITIONS })
    @WebResult(name = "historyPoint", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<HistoryPoint> getPositionHistory(
        @Parameter(description = POSITION_ID_DESC, required = true) @PathParam("positionId") @WebParam(name = "positionId", targetNamespace = ParaBankServiceConstants.TNS) int positionId,
        @Parameter(description = START_DATE_DESC, required = true) @PathParam("startDate") @WebParam(name = "startDate", targetNamespace = ParaBankServiceConstants.TNS) String startDate,
        @Parameter(description = END_DATE_DESC, required = true) @PathParam("endDate") @WebParam(name = "endDate", targetNamespace = ParaBankServiceConstants.TNS) String endDate)
                throws ParaBankServiceException;

    /**
     * Return a list of positions for a given customer
     *
     * @param customerId
     *            the customer id to lookup
     * @return list of positions
     * @throws ParaBankServiceException
     */
    @GET
    @Path("/customers/{customerId}/positions")
    @Operation(summary = "Get Positions for Customer", tags = {
        ParaBankServiceConstants.CUSTOMERS, ParaBankServiceConstants.POSITIONS })
    @WebResult(name = "position", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Position> getPositions(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @PathParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId)
                throws ParaBankServiceException;

    /**
     * Return transaction information for a given transaction id
     *
     * @param transactionId
     *            the transaction id to lookup
     * @return the transaction
     * @throws ParaBankServiceException
     */
    @GET
    @Operation(summary = "Get the transaction for the id", tags = {
        ParaBankServiceConstants.TRANSACTIONS })
    @Path("/transactions/{transactionId}")
    @WebResult(name = Constants.TRANSACTION, targetNamespace = ParaBankServiceConstants.TNS)
    @ElementClass(response = Transaction.class)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Transaction getTransaction(
        @Parameter(description = TRANSACTION_ID_DESC, required = true) @PathParam("transactionId") @WebParam(name = "transactionId", targetNamespace = ParaBankServiceConstants.TNS) int transactionId)
                throws ParaBankServiceException;

    /**
     * Return a list of transactions for a given account
     *
     * @param accountId
     *            the account id to lookup
     * @return list of account transactions
     * @throws ParaBankServiceException
     */
    @GET
    @Path("/accounts/{accountId}/transactions")
    @Operation(summary = "Get the list of Transactions for the account", tags = {
        ParaBankServiceConstants.ACCOUNTS, ParaBankServiceConstants.TRANSACTIONS })
    @WebResult(name = Constants.TRANSACTION, targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Transaction> getTransactions(
        @Parameter(description = CUSTOMER_ACCOUNT_FETCH_DESC, required = true) @PathParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId)
                throws ParaBankServiceException;

    @GET
    @Path("/accounts/{accountId}/transactions/amount/{amount}")
    @Operation(summary = "Create transactions by amount for account", tags = {
        ParaBankServiceConstants.ACCOUNTS, ParaBankServiceConstants.TRANSACTIONS })
    @WebResult(name = Constants.TRANSACTION, targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Transaction> getTransactionsByAmount(
        @Parameter(description = CUSTOMER_ACCOUNT_FETCH_DESC, required = true) @PathParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = AMOUNT_DESC, required = true) @PathParam("amount") @WebParam(name = "amount", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal amount)
                throws ParaBankServiceException;

    @GET
    @Path("/accounts/{accountId}/transactions/month/{month}/type/{type}")
    @Operation(summary = "Fetch transactions by month and type for account", tags = {
        ParaBankServiceConstants.ACCOUNTS, ParaBankServiceConstants.TRANSACTIONS })
    @WebResult(name = Constants.TRANSACTION, targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Transaction> getTransactionsByMonthAndType(
        @Parameter(description = CUSTOMER_ACCOUNT_FETCH_DESC, required = true) @PathParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = MONTH_DESC, required = true) @PathParam("month") @WebParam(name = "month", targetNamespace = ParaBankServiceConstants.TNS) String month,
        @Parameter(description = TRANSACTION_TYPE_DESC, required = true) @PathParam("type") @WebParam(name = "type", targetNamespace = ParaBankServiceConstants.TNS) String type)
                throws ParaBankServiceException;

    @GET
    @Path("/accounts/{accountId}/transactions/fromDate/{fromDate}/toDate/{toDate}")
    @Operation(summary = "Fetch transactions for date range for account", tags = {
        ParaBankServiceConstants.ACCOUNTS, ParaBankServiceConstants.TRANSACTIONS })
    @WebResult(name = Constants.TRANSACTION, targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Transaction> getTransactionsByToFromDate(
        @Parameter(description = CUSTOMER_ACCOUNT_FETCH_DESC, required = true) @PathParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = START_DATE_DESC, required = true) @PathParam("fromDate") @WebParam(name = "fromDate", targetNamespace = ParaBankServiceConstants.TNS) String fromDate,
        @Parameter(description = END_DATE_DESC, required = true) @PathParam("toDate") @WebParam(name = "toDate", targetNamespace = ParaBankServiceConstants.TNS) String toDate)
                throws ParaBankServiceException;

    @GET
    @Path("/accounts/{accountId}/transactions/onDate/{onDate}")
    @Operation(summary = "Fetch transactions for a specific date for account", tags = {
        ParaBankServiceConstants.ACCOUNTS, ParaBankServiceConstants.TRANSACTIONS })
    @WebResult(name = Constants.TRANSACTION, targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Transaction> getTransactionsOnDate(
        @Parameter(description = CUSTOMER_ACCOUNT_FETCH_DESC, required = true) @PathParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = DATE_DESC, required = true) @PathParam("onDate") @WebParam(name = "onDate", targetNamespace = ParaBankServiceConstants.TNS) String onDate)
                throws ParaBankServiceException;

    /**
     * Reset database contents to a populated state
     */
    @POST
    @Operation(summary = "Initialize the Database", tags = { ParaBankServiceConstants.DATABASE })
    @Path("/initializeDB")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void initializeDB();

    /**
     * Return the customer id for the given username and password
     *
     * @param username
     *            the username to lookup
     * @param password
     *            the password for the customer
     * @return the customer id
     * @throws ParaBankServiceException
     */
    @GET
    @Operation(summary = "Login (john/demo)", tags = { ParaBankServiceConstants.MISC })
    @Path("/login/{username}/{password}")
    @WebResult(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS)
    @ElementClass(response = Customer.class)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Customer login(
        @Parameter(description = CUSTOMERS_USER_NAME_DESC, required = true) @PathParam(USERNAME) @WebParam(name = USERNAME, targetNamespace = ParaBankServiceConstants.TNS) String username,
        @Parameter(description = CUSTOMERS_PASSWORD_DESC, required = true) @PathParam(PASSWORD) @WebParam(name = PASSWORD, targetNamespace = ParaBankServiceConstants.TNS) String password)
                throws ParaBankServiceException;

    /**
     * Request a loan
     *
     * @param customerId
     *            the customer requesting a loan
     * @param amount
     *            the amount of the loan
     * @param downPayment
     *            the down payment for the loan
     * @param fromAccountId
     *            the account from which to deduct the down payment
     * @return response the result of the loan request
     */
    @POST
    @Path("/requestLoan")
    @Operation(summary = "Request a loan", tags = { ParaBankServiceConstants.LOANS })
    @WebResult(name = "loanResponse", targetNamespace = ParaBankServiceConstants.TNS)
    @ElementClass(response = LoanResponse.class)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    LoanResponse requestLoan(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @QueryParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId,
        @Parameter(description = AMOUNT_DESC, required = true) @QueryParam("amount") @WebParam(name = "amount", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal amount,
        @Parameter(description = DOWNPAYMENT_DESC, required = true) @QueryParam("downPayment") @WebParam(name = "downPayment", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal downPayment,
        @Parameter(description = CUSTOMER_ACCOUNT_DESC, required = true) @QueryParam("fromAccountId") @WebParam(name = "fromAccountId", targetNamespace = ParaBankServiceConstants.TNS) int fromAccountId)
                throws ParaBankServiceException;

    /**
     * Sell a position
     *
     * @param customerId
     *            the customer selling the position
     * @param accountId
     *            the account in which to deposit funds
     * @param positionId
     *            the position being sold
     * @param shares
     *            the number of shares to sell
     * @param pricePerShare
     *            the price per share of the stock position
     * @return a list of positions for the customer
     * @throws ParaBankServiceException
     */
    @POST
    @Path("/customers/{customerId}/sellPosition")
    @Operation(summary = "Sell a Position", tags = {
        ParaBankServiceConstants.POSITIONS })
    @WebResult(name = "position", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Position> sellPosition(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @PathParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId,
        @Parameter(description = CUSTOMER_ACCOUNT_DEP_DESC, required = true) @QueryParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = POSITION_ID_DESC, required = true) @QueryParam("positionId") @WebParam(name = "positionId", targetNamespace = ParaBankServiceConstants.TNS) int positionId,
        @Parameter(description = NUMBER_OF_SHARES_DESC, required = true) @QueryParam("shares") @WebParam(name = "shares", targetNamespace = ParaBankServiceConstants.TNS) int shares,
        @Parameter(description = PRICE_PER_SHARE_DESC, required = true) @QueryParam("pricePerShare") @WebParam(name = "pricePerShare", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal pricePerShare)
                throws ParaBankServiceException;

    /**
     * Sets the value of a given configuration parameter
     *
     * @param name
     *            the name of the parameter
     * @param value
     *            the value to set
     */
    @POST
    @Operation(summary = "Set Parameters", tags = { ParaBankServiceConstants.MISC })
    @Path("/setParameter/{name}/{value}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void setParameter(
        @Parameter(description = PARAMETER_NAME, required = true) @PathParam("name") @WebParam(name = "name", targetNamespace = ParaBankServiceConstants.TNS) String name,
        @Parameter(description = PARAMETER_VALUE, required = true) @PathParam("value") @WebParam(name = "value", targetNamespace = ParaBankServiceConstants.TNS) String value);

    /**
     * Disable JMS message listener
     */
    @POST
    @Operation(summary = "Stop JMS Listener", tags = { ParaBankServiceConstants.JMS })
    @Path("/shutdownJmsListener")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void shutdownJmsListener();

    /**
     * Enable JMS message listener
     */
    @POST
    @Operation(summary = "Start JMS Listener", tags = { ParaBankServiceConstants.JMS })
    @Path("/startupJmsListener")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void startupJmsListener();

    /**
     * Transfer funds between two accounts
     *
     * @param fromAccountId
     *            the account from which to withdraw funds
     * @param toAccountId
     *            the account to which to deposit funds
     * @param amount
     *            the amount to transfer
     * @return status message of result
     * @throws ParaBankServiceException
     */
    @POST
    @Path("/transfer")
    @Operation(summary = "Transfer funds", tags = { ParaBankServiceConstants.ACCOUNTS })
    @WebResult(name = "transferReturn", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    String transfer(
        @Parameter(description = CUSTOMER_ACCOUNT_DESC, required = true) @QueryParam("fromAccountId") @WebParam(name = "fromAccountId", targetNamespace = ParaBankServiceConstants.TNS) int fromAccountId,
        @Parameter(description = CUSTOMER_ACCOUNT_DEP_DESC, required = true) @QueryParam("toAccountId") @WebParam(name = "toAccountId", targetNamespace = ParaBankServiceConstants.TNS) int toAccountId,
        @Parameter(description = AMOUNT_DESC, required = true) @QueryParam("amount") @WebParam(name = "amount", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal amount)
                throws ParaBankServiceException;

    @POST
    @Path("/customers/update/{customerId}")
    @Operation(summary = "Update customer information", tags = {
        ParaBankServiceConstants.CUSTOMERS })
    @WebResult(name = "customerUpdateResult", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    String updateCustomer(
        @Parameter(description = CUSTOMER_ID_DESC, required = true) @PathParam(CUSTOMER_ID) @WebParam(name = CUSTOMER_ID, targetNamespace = ParaBankServiceConstants.TNS) int customerId,
        @Parameter(description = CUSTOMER_FIRST_NAME_DESC, required = true) @QueryParam(FIRST_NAME) @WebParam(name = FIRST_NAME, targetNamespace = ParaBankServiceConstants.TNS) String firstName,
        @Parameter(description = CUSTOMER_LAST_NAME_DESC, required = true) @QueryParam(LAST_NAME) @WebParam(name = LAST_NAME, targetNamespace = ParaBankServiceConstants.TNS) String lastName,
        @Parameter(description = STREET_ADDRESS_DESC, required = true) @QueryParam(STREET) @WebParam(name = STREET, targetNamespace = ParaBankServiceConstants.TNS) String street,
        @Parameter(description = CITY_DESC, required = true) @QueryParam(CITY) @WebParam(name = CITY, targetNamespace = ParaBankServiceConstants.TNS) String city,
        @Parameter(description = STATE_DESC, required = true) @QueryParam(STATE) @WebParam(name = STATE, targetNamespace = ParaBankServiceConstants.TNS) String state,
        @Parameter(description = ZIP_CODE_DESC, required = true) @QueryParam(ZIP_CODE) @WebParam(name = ZIP_CODE, targetNamespace = ParaBankServiceConstants.TNS) String zipCode,
        @Parameter(description = PHONE_NUMBER_DESC, required = true) @QueryParam(PHONE_NUMBER) @WebParam(name = PHONE_NUMBER, targetNamespace = ParaBankServiceConstants.TNS) String phoneNumber,
        @Parameter(description = SSN_DESC, required = true) @QueryParam(SSN) @WebParam(name = SSN, targetNamespace = ParaBankServiceConstants.TNS) String ssn,
        @Parameter(description = CUSTOMERS_USER_NAME_DESC, required = true) @QueryParam(USERNAME) @WebParam(name = USERNAME, targetNamespace = ParaBankServiceConstants.TNS) String username,
        @Parameter(description = CUSTOMERS_PASSWORD_DESC, required = true) @QueryParam(PASSWORD) @WebParam(name = PASSWORD, targetNamespace = ParaBankServiceConstants.TNS) String password)
                throws ParaBankServiceException;

    /**
     * Withdraw funds out of the given account
     *
     * @param accountId
     *            the account from which to withdraw funds
     * @param amount
     *            the amount to withdraw
     * @return status message of result
     * @throws ParaBankServiceException
     */
    @POST
    @Path("/withdraw")
    @Operation(summary = "Withdraw funds", tags = { ParaBankServiceConstants.ACCOUNTS })
    @WebResult(name = "withdrawReturn", targetNamespace = ParaBankServiceConstants.TNS)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    String withdraw(
        @Parameter(description = CUSTOMER_ACCOUNT_DESC, required = true) @QueryParam(ACCOUNT_ID) @WebParam(name = ACCOUNT_ID, targetNamespace = ParaBankServiceConstants.TNS) int accountId,
        @Parameter(description = AMOUNT_DESC, required = true) @QueryParam("amount") @WebParam(name = "amount", targetNamespace = ParaBankServiceConstants.TNS) BigDecimal amount)
                throws ParaBankServiceException;
}
