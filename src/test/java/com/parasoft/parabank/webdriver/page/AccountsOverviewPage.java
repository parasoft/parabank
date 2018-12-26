package com.parasoft.parabank.webdriver.page;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

public class AccountsOverviewPage {
    private WebDriver _driver;

    @FindBys({ @FindBy(css = "#accountTable>tbody>tr>td:nth-child(1)>a") })
    private List<WebElement> accountsCells;
    
    @FindBy(css = "#accountTable>tbody>tr>td:nth-child(1)>a")
    private WebElement anyAccount;

    public AccountsOverviewPage(WebDriver driver) {
        _driver = driver;
        Util.waitForTitleContains(_driver, "ParaBank", "Accounts Overview");
    }

    public List<String> getAccounts() {
        // wait until there is at least one account showing
        // assumes there is at least one account available on Parabank
        Util.waitUntilVisible(anyAccount, _driver);
        List<String> accounts = new ArrayList<>();
        accountsCells.stream().forEach(accountsCell -> {
            accounts.add(accountsCell.getText());
        });
        return accounts;
    }

    public void clickAccount(String id) {
        Optional<WebElement> foundAccount = accountsCells.stream().filter(cell -> cell.getText().trim().equals(id))
                .findFirst();
        assertTrue("Unable to find account: " + id, foundAccount.isPresent());
        WebElement account = foundAccount.get();
        Util.waitAndClick(account, _driver);
    }
}
