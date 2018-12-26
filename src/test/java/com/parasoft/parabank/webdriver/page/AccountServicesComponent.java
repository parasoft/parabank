package com.parasoft.parabank.webdriver.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountServicesComponent {
    private WebDriver _driver;

    @FindBy(xpath = "//div[@id='leftPanel']//a[text()='Accounts Overview']")
    private WebElement accountsOverviewLink;

    @FindBy(xpath = "//div[@id='leftPanel']//a[text()='Transfer Funds']")
    private WebElement transferFundsLink;

    public AccountServicesComponent(WebDriver driver) {
        _driver = driver;
    }

    public void clickAccountsOverview() {
        Util.waitAndClick(accountsOverviewLink, _driver);
    }

    public void clickTransferFunds() {
        Util.waitAndClick(transferFundsLink, _driver);
    }
}
