package com.parasoft.parabank.it.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import com.parasoft.parabank.it.util.PageUtil;
import com.parasoft.parabank.it.util.TitlePage;

public class AccountsOverviewPage extends TitlePage {
    @FindBys({ @FindBy(css = "#accountTable>tbody>tr>td:nth-child(1)>a") })
    private List<WebElement> accountsCells;

    @FindBy(css = "#accountTable>tbody>tr>td:nth-child(1)>a")
    private WebElement anyAccount;

    @FindBy(linkText = "Request Loan")
    private WebElement requestLoanLink;

    public AccountsOverviewPage(WebDriver driver) {
        super(driver, "ParaBank", "Accounts Overview");
    }

    public List<String> getAccounts() {
        // wait until there is at least one account showing
        // assumes there is at least one account available on Parabank
        // This step is regularly failing during nightly test runs.
        // Try giving it more time
        PageUtil.waitUntilVisible(anyAccount, getDriver(), PageUtil.DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT * 2);
        List<String> accounts = new ArrayList<>();
        accountsCells.stream().forEach(accountsCell -> {
            accounts.add(accountsCell.getText());
        });
        return accounts;
    }

    public void clickAccount(String id) {
        Optional<WebElement> foundAccount = accountsCells.stream().filter(cell -> cell.getText().trim().equals(id))
                .findFirst();
        assert foundAccount.isPresent() : "Unable to find account: " + id;
        WebElement account = foundAccount.get();
        PageUtil.waitAndClick(account, getDriver());
    }

    public void clickRequestLoanLink() {
        PageUtil.waitAndClick(requestLoanLink, getDriver());
    }

    public AccountServicesComponent getAccountServices() {
        return new AccountServicesComponent(getDriver());
    }
}
