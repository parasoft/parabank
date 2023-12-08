package com.parasoft.parabank.it.page;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import com.parasoft.parabank.it.util.PageUtil;
import com.parasoft.parabank.it.util.TitlePage;

public class BillPayPage extends TitlePage {
    @FindBy(name = "payee.name")
    private WebElement payeeNameField;

    @FindBy(name = "payee.address.street")
    private WebElement addressField;

    @FindBy(name = "payee.address.city")
    private WebElement cityField;

    @FindBy(name = "payee.address.state")
    private WebElement stateField;

    @FindBy(name = "payee.address.zipCode")
    private WebElement zipCodeField;

    private final By payeePhoneNumber = new ByName("payee.phoneNumber");

    @FindBy(name = "payee.phoneNumber")
    private WebElement phoneNumberField;

    @FindAll({ @FindBy(name = "payee.phoneNumber") })
    private List<WebElement> phoneNumberFields;

    @FindBy(name = "payee.accountNumber")
    private WebElement accountNumberField;

    @FindBy(name = "verifyAccount")
    private WebElement verifyAccountNumberField;

    @FindBy(name = "amount")
    private WebElement amountField;

    @FindBy(name = "fromAccountId")
    private WebElement accountSelect;

    @FindBy(xpath = "//input[@value='Send Payment']")
    private WebElement sendPaymentButton;

    public BillPayPage(WebDriver driver) {
        super(driver, "ParaBank", "Bill Pay");
    }

    public void setPayeeName(String name) {
        PageUtil.waitAndType(payeeNameField, getDriver(), name);
    }

    public void setAddress(String address) {
        PageUtil.waitAndType(addressField, getDriver(), address);
    }

    public void setCity(String city) {
        PageUtil.waitAndType(cityField, getDriver(), city);
    }

    public void setState(String state) {
        PageUtil.waitAndType(stateField, getDriver(), state);
    }

    public void setZipcode(String zipcode) {
        PageUtil.waitAndType(zipCodeField, getDriver(), zipcode);
    }

    public void setPhoneNumber(String phoneNumber) {
        PageUtil.waitAndType(phoneNumberField, getDriver(), phoneNumber);
    }

    public void setPhoneNumberToFirstField(String phoneNumber) {
        PageUtil.waitForElements(payeePhoneNumber, 0, getDriver());
        Optional<WebElement> phoneNumberField = phoneNumberFields.stream().findFirst();
        PageUtil.waitAndType(phoneNumberField.get(), getDriver(), phoneNumber);
    }

    public void setAccountNumber(String accountNumber) {
        PageUtil.waitAndType(accountNumberField, getDriver(), accountNumber);
    }

    public void setVerifyAccountNumber(String accountNumber) {
        PageUtil.waitAndType(verifyAccountNumberField, getDriver(), accountNumber);
    }

    public void setAmount(String amount) {
        PageUtil.waitAndType(amountField, getDriver(), amount);
    }

    public void selectAccount(String account) {
        PageUtil.scrollIntoView(accountSelect, getDriver());
        PageUtil.waitAndSelect(accountSelect, getDriver(), account);
    }

    public void sendPayment() {
        PageUtil.waitAndClick(sendPaymentButton, getDriver());
    }
}
