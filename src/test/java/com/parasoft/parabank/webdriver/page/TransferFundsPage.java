package com.parasoft.parabank.webdriver.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TransferFundsPage {
    private WebDriver _driver;

    @FindBy(id = "amount")
    private WebElement amountField;

    @FindBy(id = "fromAccountId")
    private WebElement fromSelect;

    @FindBy(id = "toAccountId")
    private WebElement toSelect;

    @FindBy(xpath = "//input[@value='Transfer']")
    private WebElement transferButton;

    public TransferFundsPage(WebDriver driver) {
        _driver = driver;
        Util.waitForTitleContains(driver, "ParaBank", "Transfer Funds");
    }

    public void setAmount(String amount) {
        Util.waitAndType(amountField, _driver, amount);
    }

    public void selectFromAccount(String from) {
        Util.waitAndSelect(fromSelect, _driver, from);
    }

    public void selectToAccount(String to) {
        Util.waitAndSelect(toSelect, _driver, to);
    }

    public void clickTransfer() {
        Util.waitAndClick(transferButton, _driver);
    }

    public AccountServicesComponent getAccountServices() {
        return PageFactory.initElements(_driver, AccountServicesComponent.class);
    }
}
