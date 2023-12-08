package com.parasoft.parabank.it.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.parasoft.parabank.it.util.PageObject;
import com.parasoft.parabank.it.util.PageUtil;

public class AccountServicesComponent extends PageObject {
    @FindBy(xpath = "//div[@id='leftPanel']//a[text()='Accounts Overview']")
    private WebElement accountsOverviewLink;

    @FindBy(xpath = "//div[@id='leftPanel']//a[text()='Transfer Funds']")
    private WebElement transferFundsLink;

    @FindBy(xpath = "//div[@id='leftPanel']//a[text()='Bill Pay']")
    private WebElement billPayLink;

    public AccountServicesComponent(WebDriver driver) {
        super(driver);
    }

    public void clickAccountsOverview() {
        PageUtil.waitAndClick(accountsOverviewLink, getDriver());
    }

    public void clickTransferFunds() {
        PageUtil.waitAndClick(transferFundsLink, getDriver());
    }

    public void clickBillPay() {
        PageUtil.waitAndClick(billPayLink, getDriver());
    }
}
