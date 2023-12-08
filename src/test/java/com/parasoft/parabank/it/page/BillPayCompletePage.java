package com.parasoft.parabank.it.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.parasoft.parabank.it.util.PageUtil;
import com.parasoft.parabank.it.util.TitlePage;

public class BillPayCompletePage extends TitlePage {
    @FindBy(id = "payeeName")
    private WebElement payeeName;

    @FindBy(id = "amount")
    private WebElement amount;

    @FindBy(id = "fromAccountId")
    private WebElement fromAccountId;

    public BillPayCompletePage(WebDriver driver) {
        super(driver, "ParaBank", "Bill Payment Complete");
    }

    public String getPayeeName() {
        return PageUtil.waitUntilVisibleAndGetText(payeeName, getDriver());
    }

    public String getAmount() {
        return PageUtil.waitUntilVisibleAndGetText(amount, getDriver());
    }

    public String getFromAccountId() {
        return PageUtil.waitUntilVisibleAndGetText(fromAccountId, getDriver());
    }

}
