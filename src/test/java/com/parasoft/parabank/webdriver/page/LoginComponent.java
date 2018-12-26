package com.parasoft.parabank.webdriver.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginComponent {
    private WebDriver _driver;

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@value='Log In']")
    private WebElement loginButton;

    public LoginComponent(WebDriver driver) {
        _driver = driver;
    }

    public void setUsername(String username) {
        Util.waitAndType(usernameField, _driver, username);
    }

    public void setPassword(String password) {
        Util.waitAndType(passwordField, _driver, password);
    }

    public void clickLogin() {
        Util.waitAndClick(loginButton, _driver);
    }
}
