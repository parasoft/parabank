package com.parasoft.parabank.it.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.parasoft.parabank.it.util.PageObject;
import com.parasoft.parabank.it.util.PageUtil;

public class LoginComponent extends PageObject {
    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@value='Log In']")
    private WebElement loginButton;

    public LoginComponent(WebDriver driver) {
        super(driver);
    }

    public void setUsername(String username) {
        PageUtil.waitAndType(usernameField, getDriver(), username);
    }

    public void setPassword(String password) {
        PageUtil.waitAndType(passwordField, getDriver(), password);
    }

    public void clickLogin() {
        PageUtil.waitAndClick(loginButton, getDriver());
    }
}
