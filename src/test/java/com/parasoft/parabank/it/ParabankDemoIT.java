package com.parasoft.parabank.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.parasoft.parabank.it.page.AccountActivityPage;
import com.parasoft.parabank.it.page.AccountServicesComponent;
import com.parasoft.parabank.it.page.AccountsOverviewPage;
import com.parasoft.parabank.it.page.BillPayCompletePage;
import com.parasoft.parabank.it.page.BillPayPage;
import com.parasoft.parabank.it.page.LandingPage;
import com.parasoft.parabank.it.page.LoginComponent;
import com.parasoft.parabank.it.page.RequestLoanPage;
import com.parasoft.parabank.it.page.TransferCompletePage;
import com.parasoft.parabank.it.page.TransferFundsPage;
import com.parasoft.parabank.it.util.DriverFactory;

public class ParabankDemoIT {
    private static final String BROWSER = ParabankDemoIT.class.getName() + ".BROWSER";
    private static final String ENDPOINT = ParabankDemoIT.class.getName() + ".ENDPOINT";
    private static final String FIND_ELEMENTS = ParabankDemoIT.class.getName() + ".FIND.ELEMENTS";

    protected WebDriver _driver;

    @Before
    public void beforeTest() {
        _driver = DriverFactory.getDriver(System.getProperty(BROWSER, "Chrome"));
        _driver.manage().window().maximize();
    }

    @Test
    public void testSmartAPIDemo() throws Throwable {
        _driver.get(System.getProperty(ENDPOINT, "http://localhost:8080/parabank/"));
        LandingPage landingPage = new LandingPage(_driver);

        LoginComponent loginComponent = landingPage.getLogin();
        loginComponent.setUsername("john");
        loginComponent.setPassword("demo");
        loginComponent.clickLogin();

        AccountsOverviewPage accountsOverviewPage = new AccountsOverviewPage(_driver);
        List<String> accounts = accountsOverviewPage.getAccounts();
        accountsOverviewPage.clickAccount(accounts.get(0));

        AccountActivityPage accountActivityPage = new AccountActivityPage(_driver);
        List<String> transactions = accountActivityPage.getTransactions();
        accountActivityPage.clickTransaction(transactions.get(0));

        AccountServicesComponent accountServicesComponent = accountActivityPage.getAccountServices();
        accountServicesComponent.clickTransferFunds();

        TransferFundsPage transferFundsPage = new TransferFundsPage(_driver);
        transferFundsPage.selectFromAccount(accounts.get(0));
        transferFundsPage.selectToAccount(accounts.get(1));
        transferFundsPage.setAmount("1.0");
        transferFundsPage.clickTransfer();

        TransferCompletePage transferCompletePage = new TransferCompletePage(_driver);
        String transferredAmount = transferCompletePage.getTransferredAmount();
        assertEquals("$1.00", transferredAmount);

        new AccountServicesComponent(_driver).clickAccountsOverview();

        accountsOverviewPage = new AccountsOverviewPage(_driver);
        accountsOverviewPage.clickAccount(accountsOverviewPage.getAccounts().get(0));

        accountActivityPage = new AccountActivityPage(_driver);
    }

    @Test
    public void testBillPayScenario() {
        _driver.get(System.getProperty(ENDPOINT, "http://localhost:8080/parabank/"));
        boolean enableFindElements = Boolean.getBoolean(FIND_ELEMENTS);

        LandingPage landingPage = new LandingPage(_driver);
        LoginComponent loginComponent = landingPage.getLogin();

        loginComponent.setUsername("john");
        loginComponent.setPassword("demo");
        loginComponent.clickLogin();

        AccountsOverviewPage accountsOverviewPage = new AccountsOverviewPage(_driver);
        List<String> accounts = accountsOverviewPage.getAccounts();
        String firstAccountNumber = accounts.get(0);

        AccountServicesComponent accountServicesComponent = accountsOverviewPage.getAccountServices();
        accountServicesComponent.clickBillPay();

        BillPayPage billPayPage = new BillPayPage(_driver);
        billPayPage.setPayeeName("Mr. Kurtz");
        billPayPage.setAddress("101 East Huntington Drive");
        billPayPage.setCity("Monrovia");
        billPayPage.setState("CA");
        billPayPage.setZipcode("91016");
        if (enableFindElements) {
            billPayPage.setPhoneNumberToFirstField("(888) 305-0041");
        } else {
            billPayPage.setPhoneNumber("(888) 305-0041");
        }
        billPayPage.setAccountNumber("150608");
        billPayPage.setVerifyAccountNumber("150608");
        billPayPage.setAmount("1.0");
        billPayPage.selectAccount(firstAccountNumber);
        billPayPage.sendPayment();

        BillPayCompletePage billPayCompletePage = new BillPayCompletePage(_driver);

        assertEquals("Mr. Kurtz", billPayCompletePage.getPayeeName());
        assertEquals("$1.00", billPayCompletePage.getAmount());
        assertEquals(firstAccountNumber, billPayCompletePage.getFromAccountId());
    }

    @Test
    public void testRequestLoanScenario() {
        _driver.get(System.getProperty(ENDPOINT, "http://localhost:8080/parabank/"));

        LandingPage landingPage = new LandingPage(_driver);
        LoginComponent loginComponent = landingPage.getLogin();
        loginComponent.setUsername("john");
        loginComponent.setPassword("demo");
        loginComponent.clickLogin();

        AccountsOverviewPage accountsOverviewPage = new AccountsOverviewPage(_driver);
        accountsOverviewPage.clickRequestLoanLink();

        RequestLoanPage paraBankLoanRequestPage = new RequestLoanPage(_driver);
        paraBankLoanRequestPage.setAmountField("100.0");
        paraBankLoanRequestPage.setDownPaymentField("10.0");
        paraBankLoanRequestPage.selectFromAccountIdDropdown("12567");

        paraBankLoanRequestPage.clickApplyNowButton();
        assertEquals("Wealth Securities Dynamic Loans (WSDL)", paraBankLoanRequestPage.getLoanProviderName());
    }

    @After
    public void afterTest() {
        if (_driver != null) {
            _driver.quit();
        }
    }
}
