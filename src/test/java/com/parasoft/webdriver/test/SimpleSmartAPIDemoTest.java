package com.parasoft.webdriver.test;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.parasoft.webdriver.page.AccountActivityPage;
import com.parasoft.webdriver.page.AccountServicesComponent;
import com.parasoft.webdriver.page.AccountsOverviewPage;
import com.parasoft.webdriver.page.LandingPage;
import com.parasoft.webdriver.page.LoginComponent;
import com.parasoft.webdriver.page.TransferFundsPage;

public abstract class SimpleSmartAPIDemoTest {
    void doSmartAPIDemo(WebDriver driver) throws Throwable {
        try {
            driver.get("http://localhost:8080/parabank/");
            LandingPage landingPage = PageFactory.initElements(driver, LandingPage.class);

            LoginComponent loginComponent = landingPage.getLogin();
            loginComponent.setUsername("john");
            loginComponent.setPassword("demo");
            loginComponent.clickLogin();

            AccountsOverviewPage accountsOverviewPage = PageFactory.initElements(driver, AccountsOverviewPage.class);
            List<String> accounts = accountsOverviewPage.getAccounts();
            accountsOverviewPage.clickAccount(accounts.get(0));

            AccountActivityPage accountActivityPage = PageFactory.initElements(driver, AccountActivityPage.class);

            AccountServicesComponent accountServicesComponent = accountActivityPage.getAccountServices();
            accountServicesComponent.clickTransferFunds();

            TransferFundsPage transferFundsPage = PageFactory.initElements(driver, TransferFundsPage.class);
            transferFundsPage.setAmount("1");
            transferFundsPage.selectFromAccount(accounts.get(0));
            transferFundsPage.selectToAccount(accounts.get(1));
            transferFundsPage.clickTransfer();

            accountServicesComponent = transferFundsPage.getAccountServices();
            accountServicesComponent.clickAccountsOverview();

            accountsOverviewPage = PageFactory.initElements(driver, AccountsOverviewPage.class);
            accounts = accountsOverviewPage.getAccounts();
            accountsOverviewPage.clickAccount(accounts.get(0));

            accountActivityPage = PageFactory.initElements(driver, AccountActivityPage.class);
        } finally {
            driver.quit();
        }
    }
}
