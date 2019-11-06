// parasoft-begin-suppress PB.RE.RCODE "Reviewed and found appropriate for this class only"
package com.parasoft.parabank.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Payee;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.BankManager;
import com.parasoft.parabank.service.CustomerConstants;
import com.parasoft.parabank.service.ParaBankService;
import com.parasoft.parabank.service.ParaBankServiceException;

// This class delegates all the function calls according to the access mode Viz.
// SOAP, REST XML , REST JSON, JDBC(Default)
/**
 * @author deepakv
 *
 *
 *
 */
@Component("accessModeController")
public class AccessModeController {

    private static final Logger LOG = LoggerFactory.getLogger(AccessModeController.class);

    private static final String GET = "GET";

    private static final String POST = "POST";

    private static int getCatalinaPort() {
        final int port = Util.getPort();
        return port > 0 ? port : Util.DEFAULT_CATALINA_PORT;
    }

    private Customer customer = null;

    private final int catalinaPort;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @Resource(name = "bankManager")
    private BankManager bankManager;

    public AccessModeController() {
        catalinaPort = getCatalinaPort();
        LOG.info("Getting Tomcat HTTP port = " + catalinaPort);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add appendQueryParam description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @param customer
     * @param sb
     * @throws UnsupportedEncodingException
     */
    public StringBuilder appendQueryParam(final StringBuilder sb, final String name, final String value)
            throws UnsupportedEncodingException {
        return appendQueryParam(sb, name, value, false);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add appendQueryParam description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @param customer
     * @param sb
     * @throws UnsupportedEncodingException
     */
    public StringBuilder appendQueryParam(final StringBuilder sb, final String name, final String value,
        final boolean last) throws UnsupportedEncodingException {
        sb.append(name).append("=").append(URLEncoder.encode(value, "UTF-8"));
        return last ? sb : sb.append("&");
    }

    public Account createAccount(final int customerId, final int newAccountType, final int fromAccountId)
            throws ParaBankServiceException, IOException, JAXBException {

        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");
        Account createdAccount = new Account();

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode.equalsIgnoreCase("SOAP")) {

            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            LOG.info("Using SOAP Web Service: ParaBank");
            createdAccount = parabankService.createAccount(customerId, newAccountType, fromAccountId);
        }

        else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/createAccount?customerId=" + customerId + "&newAccountType="
                + newAccountType + "&fromAccountId=" + fromAccountId);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, POST);

            final JAXBContext jc = JAXBContext.newInstance(Account.class);

            final InputStream xml = conn.getInputStream();
            createdAccount = (Account) jc.createUnmarshaller().unmarshal(xml);

            conn.disconnect();

            LOG.info("Using REST xml Web Service: Bank");

        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/createAccount?customerId=" + customerId + "&newAccountType="
                + newAccountType + "&fromAccountId=" + fromAccountId);

            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, POST);
            final InputStream inputStream = conn.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final String message = reader.readLine();
            final JsonElement rootElement = JsonParser.parseString(message);
            final JsonObject actObject = rootElement.getAsJsonObject();
            createdAccount = Account.readFrom(actObject);

            conn.disconnect();
            LOG.info("Using REST json Web Service: Bank");
        }

        return createdAccount;
    }

    private URL createGetTransactionsRestUrl(final int accountId, final TransactionCriteria criteria,
        final String restEndpoint) throws MalformedURLException {

        final String urlString = restEndpoint + "/accounts/" + accountId + "/transactions";

        if (criteria != null) {
            if (criteria.getAmount() != null) {
                return new URL(urlString + "/amount/" + criteria.getAmount());
            } else if (criteria.getOnDate() != null) {
                return new URL(
                    urlString + "/onDate/" + TransactionCriteria.DATE_FORMATTER.get().format(criteria.getOnDate()));
            } else if (criteria.getFromDate() != null && criteria.getToDate() != null) {
                return new URL(
                    urlString + "/fromDate/" + TransactionCriteria.DATE_FORMATTER.get().format(criteria.getFromDate())
                        + "/toDate/" + TransactionCriteria.DATE_FORMATTER.get().format(criteria.getToDate()));
            } else if (criteria.getMonth() != null && criteria.getTransactionType() != null) {
                return new URL(urlString + "/month/" + criteria.getMonth() + "/type/" + criteria.getTransactionType());
            }
        }

        return new URL(urlString);
    }

    private URL createUpdateCustomerUrl(final Customer customer, final Address address, final String restEndpoint)
            throws MalformedURLException, UnsupportedEncodingException {
        StringBuilder sb =
            new StringBuilder(restEndpoint).append("/customers/update/").append(customer.getId()).append("?");
        sb = appendQueryParam(sb, CustomerConstants.FIRST_NAME, customer.getFirstName());
        sb = appendQueryParam(sb, CustomerConstants.LAST_NAME, customer.getLastName());
        sb = appendQueryParam(sb, CustomerConstants.STREET, address.getStreet());
        sb = appendQueryParam(sb, CustomerConstants.CITY, address.getCity());
        sb = appendQueryParam(sb, CustomerConstants.STATE, address.getState());
        sb = appendQueryParam(sb, CustomerConstants.ZIP_CODE, address.getZipCode());
        sb = appendQueryParam(sb, CustomerConstants.PHONE_NUMBER, customer.getPhoneNumber());
        sb = appendQueryParam(sb, CustomerConstants.SSN, customer.getSsn());
        sb = appendQueryParam(sb, CustomerConstants.USERNAME, customer.getUsername());
        sb = appendQueryParam(sb, CustomerConstants.PASSWORD, customer.getPassword(), true);
        return new URL(sb.toString());
        //        return new URL(restEndpoint + "/customers/update/" + customer.getId() + "/"
        //                + URLEncoder.encode(customer.getFirstName(), "UTF-8") + "/"
        //                + URLEncoder.encode(customer.getLastName(), "UTF-8") + "/" + URLEncoder.encode(address.getStreet(), "UTF-8")
        //                + "/" + URLEncoder.encode(address.getCity(), "UTF-8") + "/" + address.getState() + "/"
        //                + URLEncoder.encode(address.getZipCode(), "UTF-8") + "/"
        //                + URLEncoder.encode(customer.getPhoneNumber(), "UTF-8") + "/" + customer.getSsn() + "/"
        //                + customer.getUsername() + "/" + customer.getPassword());
    }

    /**
     * @throws java.io.IOException
     *
     */
    public void doDBClean() throws IOException {
        final String accessMode = adminManager.getParameter("accessmode");

        if (accessMode.equalsIgnoreCase("SOAP")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);
            parabankService.cleanDB();
            LOG.info("Using SOAP Web Service: ParaBank");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/bank/cleanDB");

            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/xml");
            // InputStream xml = conn.getInputStream();
            conn.disconnect();
            LOG.info("Using REST xml Web Service: Bank");

        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/bank/cleanDB");
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            // BufferedReader br = new BufferedReader(new InputStreamReader(
            // (conn.getInputStream())));
            conn.disconnect();
            LOG.info("Using REST json Web Service: Bank");
        } else {
            adminManager.cleanDB();
            LOG.info("Using regular JDBC connection");
        }
    }

    /**
     * @throws java.io.IOException
     *
     */
    public void doDBinit() throws IOException {
        final String accessMode = adminManager.getParameter("accessmode");

        if (accessMode.equalsIgnoreCase("SOAP")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);
            parabankService.initializeDB();
            LOG.info("Using SOAP Web Service: ParaBank");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/bank/initializeDB");

            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/xml");
            // InputStream xml = conn.getInputStream();
            conn.disconnect();

            LOG.info("Using REST xml Web Service: Bank");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/bank/initializeDB");

            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");

            // BufferedReader br = new BufferedReader(new
            // InputStreamReader((conn.getInputStream())));

            LOG.info("Using REST json Web Service: Bank");
        } else {
            adminManager.initializeDB();
            LOG.info("Using regular JDBC connection");
        }
    }

    /**
     * @param id
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     * @return
     *
     */
    public Account doGetAccount(final int id) throws ParaBankServiceException, IOException, JAXBException {
        Account account = new Account();
        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode.equalsIgnoreCase("SOAP")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            account = parabankService.getAccount(id);
            LOG.info("Using SOAP Web Service: ParaBank");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/accounts/" + id);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, GET);

            final JAXBContext jc = JAXBContext.newInstance(Account.class);

            final InputStream xml = conn.getInputStream();
            account = (Account) jc.createUnmarshaller().unmarshal(xml);

            conn.disconnect();

            LOG.info("Using REST xml Web Service");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/accounts/" + id);

            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, GET);
            final InputStream inputStream = conn.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final String message = reader.readLine();
            final JsonElement rootElement = JsonParser.parseString(message);
            final JsonObject obj = rootElement.getAsJsonObject();
            account = Account.readFrom(obj);
            conn.disconnect();
            LOG.info("Using REST json Web Service");
        }

        return account;
    }

    /**
     * @param customer
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     * @return
     *
     */
    public List<Account> doGetAccounts(final Customer customer)
            throws ParaBankServiceException, IOException, JAXBException {

        List<Account> accounts = new ArrayList<>();
        Accounts acs = new Accounts();
        // Map <String, List<Account>> Accs = new HashMap<String,
        // List<Account>>();
        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode.equalsIgnoreCase("SOAP")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            accounts = parabankService.getAccounts(customer.getId());
            LOG.info("Using SOAP Web Service: ParaBank");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/customers/" + customer.getId() + "/accounts");

            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, GET);

            final JAXBContext jc = JAXBContext.newInstance(Accounts.class);
            final Unmarshaller um = jc.createUnmarshaller();
            final InputStream inputStream = conn.getInputStream();

            acs = (Accounts) um.unmarshal(inputStream);

            accounts = acs.getAccs();

            conn.disconnect();

            LOG.info("Using REST xml Web Service: Bank");

        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/customers/" + customer.getId() + "/accounts");

            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, GET);
            final InputStream inputStream = conn.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final String message = reader.readLine();
            final JsonElement rootElement = JsonParser.parseString(message);
            final JsonArray arr = rootElement.getAsJsonArray();

            for (int i = 0; i < arr.size(); i++) {
                final Account acct = Account.readFrom(arr.get(i).getAsJsonObject());
                LOG.info("Account read: " + acct.getId());
                accounts.add(acct);
            }

            conn.disconnect();
            LOG.info("Using REST json Web Service: Bank");
        }

        return accounts;
    }

    /**
     * @param custId
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     * @return
     *
     */
    public Customer doGetCustomer(final int custId) throws ParaBankServiceException, IOException, JAXBException {
        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode.equalsIgnoreCase("SOAP")) {

            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            customer = parabankService.getCustomer(custId);
            LOG.info("Using SOAP Web Service: ParaBank");
        }

        else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/customers/" + custId);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, GET);

            final JAXBContext jc = JAXBContext.newInstance(Customer.class);

            final InputStream xml = conn.getInputStream();
            customer = (Customer) jc.createUnmarshaller().unmarshal(xml);

            conn.disconnect();

            LOG.info("Using REST xml Web Service: Bank");

        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/customers/" + custId);

            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, GET);

            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;

            while ((output = br.readLine()) != null) {
                customer = new Gson().fromJson(output, Customer.class);
            }

            conn.disconnect();
            LOG.info("Using REST json Web Service: Bank");
        }

        return customer;
    }

    /**
     * @param id
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws javax.xml.bind.JAXBException
     * @throws java.io.IOException
     * @throws java.text.ParseException
     * @return
     *
     */
    public Transaction doGetTransaction(final int id)
            throws ParaBankServiceException, JAXBException, IOException, ParseException {
        Transaction transaction = null;
        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode.equalsIgnoreCase("SOAP")) {

            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            transaction = parabankService.getTransaction(id);

            LOG.info("Using SOAP Web Service: ParaBank");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/transactions/" + id);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, GET);

            final JAXBContext jc = JAXBContext.newInstance(Transaction.class);

            final InputStream xml = conn.getInputStream();
            transaction = (Transaction) jc.createUnmarshaller().unmarshal(xml);

            conn.disconnect();

            LOG.info("Using REST xml Web Service");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/transactions/" + id);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, GET);

            final InputStream inputStream = conn.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final String message = reader.readLine();
            final JsonElement rootElement = JsonParser.parseString(message);

            LOG.info("class=" + rootElement.getClass());
            final JsonObject obj = rootElement.getAsJsonObject();

            LOG.info("obj:" + obj);
            transaction = Transaction.readFrom(obj);
            conn.disconnect();

            LOG.info("Using REST JSON Web Service");
        }

        return transaction;
    }

    /**
     * @param accountId
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws javax.xml.bind.JAXBException
     * @throws java.io.IOException
     * @throws java.text.ParseException
     * @return
     *
     */
    public List<Transaction> doGetTransactions(final int accountId)
            throws ParaBankServiceException, JAXBException, IOException, ParseException {
        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        List<Transaction> transactions = new ArrayList<>();
        Transactions ts = new Transactions();

        if (accessMode.equalsIgnoreCase("SOAP")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            transactions = parabankService.getTransactions(accountId);
            LOG.info("Using SOAP Web Service: ParaBank");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final HttpURLConnection conn = getConnection(createGetTransactionsRestUrl(accountId, null, restEndpoint),
                MediaType.APPLICATION_XML, GET);

            final JAXBContext jc = JAXBContext.newInstance(Transactions.class);
            final Unmarshaller um = jc.createUnmarshaller();
            final InputStream xml = conn.getInputStream();
            ts = (Transactions) um.unmarshal(xml);

            transactions = ts.getTranss();

            conn.disconnect();

            LOG.info("Using REST xml Web Service");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final HttpURLConnection conn = getConnection(createGetTransactionsRestUrl(accountId, null, restEndpoint),
                MediaType.APPLICATION_JSON, GET);

            final InputStream inputStream = conn.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final String message = reader.readLine();
            final JsonElement rootElement = JsonParser.parseString(message);
            final JsonArray arr = rootElement.getAsJsonArray();

            for (int i = 0; i < arr.size(); i++) {
                final Transaction trans = Transaction.readFrom(arr.get(i).getAsJsonObject());
                // Account acct =
                // Account.readFrom(arr.get(i).getAsJsonObject());
                LOG.info("Account read: " + trans.getId());
                transactions.add(trans);
            }
            conn.disconnect();

            LOG.info("Using REST JSON Web Service");
        }

        return transactions;
    }

    /**
     * @param custid
     * @param amt
     * @param dwnpay
     * @param frmaccid
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     * @throws java.text.ParseException
     * @return
     *
     */
    public LoanResponse doRequestLoan(final int custid, final BigDecimal amt, final BigDecimal dwnpay,
        final int frmaccid) throws ParaBankServiceException, IOException, JAXBException, ParseException {

        LoanResponse loanResponse = null;

        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode.equalsIgnoreCase("SOAP")) {
            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            loanResponse = parabankService.requestLoan(custid, amt, dwnpay, frmaccid);

            LOG.info("Using SOAP Web Service: ParaBank");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/requestLoan?" + "downPayment=" + dwnpay + "&amount=" + amt
                + "&fromAccountId=" + frmaccid + "&customerId=" + custid);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, POST);

            final JAXBContext jc = JAXBContext.newInstance(LoanResponse.class);
            final Unmarshaller um = jc.createUnmarshaller();
            final InputStream xml = conn.getInputStream();
            loanResponse = (LoanResponse) um.unmarshal(xml);

            conn.disconnect();

            LOG.info("Using REST xml Web Service");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/requestLoan?" + "downPayment=" + dwnpay + "&amount=" + amt
                + "&fromAccountId=" + frmaccid + "&customerId=" + custid);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, POST);

            final InputStream inputStream = conn.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final String message = reader.readLine();
            final JsonElement rootElement = JsonParser.parseString(message);

            LOG.info("class=" + rootElement.getClass());
            final JsonObject obj = rootElement.getAsJsonObject();

            loanResponse = LoanResponse.readFrom(obj);
            conn.disconnect();
            LOG.info("Using REST JSON Web Service");
        }

        return loanResponse;
    }

    /**
     * @param fromAccountId
     * @param toAccountId
     * @param amount
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     *
     */
    public void doTransfer(final int fromAccountId, final int toAccountId, final BigDecimal amount)
            throws ParaBankServiceException, IOException, JAXBException {
        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");
        LOG.info("endpoint = " + restEndpoint);

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode.equalsIgnoreCase("SOAP")) {

            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            parabankService.transfer(fromAccountId, toAccountId, amount);
            LOG.info("Using SOAP Web Service");
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/transfer?" + "toAccountId=" + toAccountId + "&" + "amount="
                + amount + "&" + "fromAccountId=" + fromAccountId);

            LOG.info(url.toExternalForm());
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, POST);

            // this is mandatory for the function to work though we do not use
            // response anywhere
            conn.getResponseMessage();

            conn.disconnect();
            LOG.info("Using REST xml Web Service");

        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/transfer?" + "toAccountId=" + toAccountId + "&" + "amount="
                + amount + "&" + "fromAccountId=" + fromAccountId);

            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, POST);
            conn.getResponseMessage();
            conn.disconnect();
            LOG.info("Using REST json Web Service");
        }
    }

    /**
     * @param accountId
     * @param amount
     * @param payee
     * @throws MalformedURLException
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     *
     */
    public void doBillPay(final int accountId, final BigDecimal amount, final Payee payee)
            throws IOException, JAXBException {
        final String accessMode = adminManager.getParameter("accessmode");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }
        if (accessMode.equalsIgnoreCase("RESTXML")) {
            final URL url = new URL(restEndpoint + "/billpay?" + "accountId=" + accountId + "&" + "amount="
                    + amount);

            LOG.info(url.toExternalForm());
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, POST);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
            JAXBContext jContext = JAXBContext.newInstance(Payee.class);
            Marshaller marshaller = jContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(payee, conn.getOutputStream());

            // this is mandatory for the function to work though we do not use
            // response anywhere
            conn.getResponseMessage();
            conn.disconnect();
            LOG.info("Using REST xml Web Service");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {
            final URL url = new URL(restEndpoint + "/billpay?" + "accountId=" + accountId + "&" + "amount="
                    + amount);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, POST);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            JsonFactory factory = new JsonFactory();
            JsonGenerator generator = factory.createGenerator(writer);
            generator.setCodec(new ObjectMapper());
            generator.writeObject(payee);
            generator.close();
            conn.getResponseMessage();
            conn.disconnect();
            LOG.info("Using REST json Web Service");
        }
    }

    private HttpURLConnection getConnection(final URL url, final String mediaType, final String method)
            throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept", mediaType);
        return conn;
    }

    private String getDefaultRestEndpoint() {
        return "http://localhost:" + catalinaPort + "/parabank/services/bank";
    }

    public List<Transaction> getTransactionsForAccount(final Account account, final TransactionCriteria criteria)
            throws ParaBankServiceException, IOException, JAXBException, ParseException {

        List<Transaction> transactions = new ArrayList<>();

        if (criteria != null && criteria.getTransactionId() != null) {
            transactions.add(doGetTransaction(criteria.getTransactionId()));
            return transactions;
        }

        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        Transactions xmlTransactions = new Transactions();

        if (accessMode.equalsIgnoreCase("SOAP")) {

            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            LOG.info("Using SOAP Web Service: ParaBank");
            transactions = getTransactionsSoap(account, criteria, parabankService);
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {
            final HttpURLConnection connection = getConnection(
                createGetTransactionsRestUrl(account.getId(), criteria, restEndpoint), MediaType.APPLICATION_XML, GET);
            final JAXBContext jc = JAXBContext.newInstance(Transactions.class);
            final Unmarshaller um = jc.createUnmarshaller();
            final InputStream xml = connection.getInputStream();
            xmlTransactions = (Transactions) um.unmarshal(xml);

            transactions = xmlTransactions.getTranss();

            connection.disconnect();

            LOG.info("Using REST xml Web Service");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {
            final HttpURLConnection connection = getConnection(
                createGetTransactionsRestUrl(account.getId(), criteria, restEndpoint), MediaType.APPLICATION_JSON, GET);
            final InputStream inputStream = connection.getInputStream();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final String message = reader.readLine();
            final JsonElement rootElement = JsonParser.parseString(message);
            final JsonArray arr = rootElement.getAsJsonArray();

            for (int i = 0; i < arr.size(); i++) {
                final Transaction trans = Transaction.readFrom(arr.get(i).getAsJsonObject());
                // Account acct =
                // Account.readFrom(arr.get(i).getAsJsonObject());
                LOG.info("Account read: " + trans.getId());
                transactions.add(trans);
            }

            connection.disconnect();

            LOG.info("Using REST JSON Web Service");
        }

        return transactions;
    }

    private List<Transaction> getTransactionsSoap(final Account account, final TransactionCriteria criteria,
        final ParaBankService parabankService)
                throws ParaBankServiceException, JAXBException, IOException, ParseException {
        if (criteria != null) {
            if (criteria.getAmount() != null) {
                return parabankService.getTransactionsByAmount(account.getId(), criteria.getAmount());
            } else if (criteria.getOnDate() != null) {
                return parabankService.getTransactionsOnDate(account.getId(),
                    TransactionCriteria.DATE_FORMATTER.get().format(criteria.getOnDate()));
            } else if (criteria.getFromDate() != null && criteria.getToDate() != null) {
                return parabankService.getTransactionsByToFromDate(account.getId(),
                    TransactionCriteria.DATE_FORMATTER.get().format(criteria.getFromDate()),
                    TransactionCriteria.DATE_FORMATTER.get().format(criteria.getToDate()));
            } else if (criteria.getMonth() != null && criteria.getTransactionType() != null) {
                return parabankService.getTransactionsByMonthAndType(account.getId(), criteria.getMonth(),
                    criteria.getTransactionType());
            }
        }

        return doGetTransactions(account.getId());
    }

    /**
     * @param username
     * @param password
     * @throws com.parasoft.parabank.service.ParaBankServiceException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     * @return
     *
     */
    public Customer login(final String username, final String password)
            throws ParaBankServiceException, IOException, JAXBException {
        String accessMode = null, soapEndpoint = null, restEndpoint = null;

        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
            soapEndpoint = adminManager.getParameter("soap_endpoint");
            restEndpoint = adminManager.getParameter("rest_endpoint");
        }

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        if (accessMode != null && accessMode.equalsIgnoreCase("SOAP")) {

            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            try {
                customer = parabankService.login(username, password);
            } catch (final ParaBankServiceException e) {
            }

            LOG.info("Using SOAP Web Service: ParaBank");
        }

        else if (accessMode != null && accessMode.equalsIgnoreCase("RESTXML")) {

            final URL url = new URL(restEndpoint + "/login/" + username + "/" + password);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_XML, GET);
            LOG.info(url.toExternalForm());

            final JAXBContext jc = JAXBContext.newInstance(Customer.class);
            InputStream xml = null;

            try {
                xml = conn.getInputStream();
            } catch (final IOException e) {
            }

            if (xml != null) {
                customer = (Customer) jc.createUnmarshaller().unmarshal(xml);
            }

            conn.disconnect();

            LOG.info("Using REST xml Web Service: Bank");
        } else if (accessMode != null && accessMode.equalsIgnoreCase("RESTJSON")) {

            final URL url = new URL(restEndpoint + "/login/" + username + "/" + password);
            final HttpURLConnection conn = getConnection(url, MediaType.APPLICATION_JSON, GET);
            BufferedReader br = null;

            try {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } catch (final IOException e) {
            }

            String output;

            if (br != null) {
                while ((output = br.readLine()) != null) {
                    customer = new Gson().fromJson(output, Customer.class);
                }
            }

            conn.disconnect();
            LOG.info("Using REST json Web Service: Bank");
        } else {

            customer = bankManager.getCustomer(username, password);
            LOG.warn("Using regular JDBC connection");
            LOG.info("cutomer={}", customer);
        }

        return customer;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    public void setBankManager(final BankManager bankManager) {
        this.bankManager = bankManager;
    }

    public void updateCustomer(final Customer updatedCustomer)
            throws ParaBankServiceException, IOException, JAXBException {
        final String accessMode = adminManager.getParameter("accessmode");
        final String soapEndpoint = adminManager.getParameter("soap_endpoint");
        String restEndpoint = adminManager.getParameter("rest_endpoint");

        if (Util.isEmpty(restEndpoint)) {
            restEndpoint = getDefaultRestEndpoint();
        }

        final Address address = updatedCustomer.getAddress();

        if (accessMode.equalsIgnoreCase("SOAP")) {

            final URL url = new URL("http://localhost:" + catalinaPort + "/parabank/services/ParaBank?wsdl");
            final QName qname = new QName("http://service.parabank.parasoft.com/", "ParaBank");
            final Service service = Service.create(url, qname);
            final ParaBankService parabankService = service.getPort(ParaBankService.class);

            if (!Util.isEmpty(soapEndpoint) && parabankService instanceof BindingProvider) {
                ((BindingProvider) parabankService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapEndpoint);
            }

            LOG.info("Using SOAP Web Service: ParaBank");

            parabankService.updateCustomer(updatedCustomer.getId(), updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(), address.getStreet(), address.getCity(), address.getState(),
                address.getZipCode(), updatedCustomer.getPhoneNumber(), updatedCustomer.getSsn(),
                updatedCustomer.getUsername(), updatedCustomer.getPassword());
        } else if (accessMode.equalsIgnoreCase("RESTXML")) {
            final URL url = createUpdateCustomerUrl(updatedCustomer, address, restEndpoint);
            final HttpURLConnection connection = getConnection(url, MediaType.APPLICATION_XML, POST);
            connection.getResponseMessage();
            connection.disconnect();
            LOG.info("Using REST xml Web Service: Bank");
        } else if (accessMode.equalsIgnoreCase("RESTJSON")) {
            final URL url = createUpdateCustomerUrl(updatedCustomer, address, restEndpoint);
            final HttpURLConnection connection = getConnection(url, MediaType.APPLICATION_JSON, POST);
            connection.getResponseMessage();
            connection.disconnect();
            LOG.info("Using REST JSON Web Service: Bank");
        }
    }
}
