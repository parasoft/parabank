package com.parasoft.parabank.webdriver.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.parasoft.parabank.webdriver.page.AccountActivityPage;
import com.parasoft.parabank.webdriver.page.AccountServicesComponent;
import com.parasoft.parabank.webdriver.page.AccountsOverviewPage;
import com.parasoft.parabank.webdriver.page.LandingPage;
import com.parasoft.parabank.webdriver.page.LoginComponent;
import com.parasoft.parabank.webdriver.page.TransferFundsPage;

public abstract class SimpleSmartAPIDemoTest {
    private static final String ENDPOINT = SimpleSmartAPIDemoTest.class.getName() + ".ENDPOINT";

    private WebDriver _driver;

    public abstract WebDriver getWebDriver();

    @Before
    public void beforeTest() {
        _driver = getWebDriver();
    }

    @Test
    public void testSmartAPIDemo() throws Throwable {
        _driver.get(System.getProperty(ENDPOINT, "http://localhost:8080/parabank/"));
        LandingPage landingPage = PageFactory.initElements(_driver, LandingPage.class);

        LoginComponent loginComponent = landingPage.getLogin();
        loginComponent.setUsername("john");
        loginComponent.setPassword("demo");
        loginComponent.clickLogin();

        AccountsOverviewPage accountsOverviewPage = PageFactory.initElements(_driver, AccountsOverviewPage.class);
        List<String> accounts = accountsOverviewPage.getAccounts();
        accountsOverviewPage.clickAccount(accounts.get(0));

        AccountActivityPage accountActivityPage = PageFactory.initElements(_driver, AccountActivityPage.class);

        AccountServicesComponent accountServicesComponent = accountActivityPage.getAccountServices();
        accountServicesComponent.clickTransferFunds();

        TransferFundsPage transferFundsPage = PageFactory.initElements(_driver, TransferFundsPage.class);
        transferFundsPage.selectFromAccount(accounts.get(0));
        transferFundsPage.selectToAccount(accounts.get(1));
        transferFundsPage.setAmount("1");
        transferFundsPage.clickTransfer();

        accountServicesComponent = transferFundsPage.getAccountServices();
        accountServicesComponent.clickAccountsOverview();

        accountsOverviewPage = PageFactory.initElements(_driver, AccountsOverviewPage.class);
        accounts = accountsOverviewPage.getAccounts();
        accountsOverviewPage.clickAccount(accounts.get(0));

        accountActivityPage = PageFactory.initElements(_driver, AccountActivityPage.class);
    }

    @After
    public void afterTest() {
        if (_driver != null) {
            _driver.quit();
        }
    }
}
