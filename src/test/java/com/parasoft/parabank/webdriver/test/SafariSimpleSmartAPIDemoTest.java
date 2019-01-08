package com.parasoft.parabank.webdriver.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class SafariSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {

    @Override
    public WebDriver getWebDriver() {
        return new SafariDriver();
    }

}
