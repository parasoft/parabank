package com.parasoft.parabank.it.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.parasoft.parabank.it.util.PageObject;
import com.parasoft.parabank.it.util.PageUtil;

public class TransferCompletePage extends PageObject {
    @FindBy(xpath = "//h1[text()='Transfer Complete!']")
    private WebElement title;

    @FindBy(id = "amountResult")
    private WebElement transferAmount;

    public TransferCompletePage(WebDriver driver) {
        super(driver);
        PageUtil.waitUntilVisible(title, driver);
    }

    public String getTransferredAmount() {
        return PageUtil.waitUntilVisibleAndGetText(transferAmount, getDriver());
    }
}
