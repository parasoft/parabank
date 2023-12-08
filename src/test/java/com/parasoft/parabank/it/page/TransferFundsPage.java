package com.parasoft.parabank.it.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.parasoft.parabank.it.util.PageUtil;
import com.parasoft.parabank.it.util.TitlePage;

public class TransferFundsPage extends TitlePage {
    @FindBy(id = "amount")
    private WebElement amountField;

    @FindBy(id = "fromAccountId")
    private WebElement fromSelect;

    @FindBy(id = "toAccountId")
    private WebElement toSelect;

    @FindBy(xpath = "//input[@value='Transfer']")
    private WebElement transferButton;

    public TransferFundsPage(WebDriver driver) {
        super(driver, "ParaBank", "Transfer Funds");
    }

    public void setAmount(String amount) {
        PageUtil.waitAndType(amountField, getDriver(), amount);
    }

    public void selectFromAccount(String from) {
        PageUtil.waitAndSelect(fromSelect, getDriver(), from, 3);
    }

    public void selectToAccount(String to) {
        PageUtil.waitAndSelect(toSelect, getDriver(), to, 3);
    }

    public void clickTransfer() {
        PageUtil.waitAndClick(transferButton, getDriver());
    }

    public AccountServicesComponent getAccountServices() {
        return new AccountServicesComponent(getDriver());
    }
}
