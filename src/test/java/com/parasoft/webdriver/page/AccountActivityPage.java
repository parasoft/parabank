package com.parasoft.webdriver.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

public class AccountActivityPage {
    private WebDriver _driver;

    @FindBys({ @FindBy(css = "#accountTable>tbody>tr>td:nth-child(1)>a") })
    private List<WebElement> accountsCells;

    public AccountActivityPage(WebDriver driver) {
        _driver = driver;
        Util.waitForTitleContains(_driver, "ParaBank", "Account Activity");
    }

    public AccountServicesComponent getAccountServices() {
        return PageFactory.initElements(_driver, AccountServicesComponent.class);
    }
}
