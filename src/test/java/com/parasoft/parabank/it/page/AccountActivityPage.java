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

public class AccountActivityPage extends TitlePage {
    @FindBys({ @FindBy(css = "#transactionTable>tbody>tr>td:nth-child(2)>a") })
    private List<WebElement> transactionHistory;

    @FindBy(css = "#transactionTable>tbody>tr>td:nth-child(2)>a")
    private WebElement anyTransaction;

    public AccountActivityPage(WebDriver driver) {
        super(driver, "ParaBank", "Account Activity");
    }

    public AccountServicesComponent getAccountServices() {
        return new AccountServicesComponent(getDriver());
    }

    public List<String> getTransactions() {
        // wait until there is at least one transaction showing
        // assumes there is at least one transaction available on Parabank
        PageUtil.waitUntilVisible(anyTransaction, getDriver());
        List<String> transactions = new ArrayList<>();
        transactionHistory.stream().forEach(transaction -> {
            transactions.add(transaction.getText());
        });
        return transactions;
    }

    public void clickTransaction(String name) {
        Optional<WebElement> foundTransaction = transactionHistory.stream()
                .filter(cell -> cell.getText().trim().equals(name))
                .findFirst();
        assert foundTransaction.isPresent() : "Unable to find transaction: " + name;
        WebElement transaction = foundTransaction.get();
        PageUtil.waitAndClick(transaction, getDriver());
    }
}
