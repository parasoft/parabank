package com.parasoft.parabank.webdriver.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {
    public WebDriver getWebDriver() {
        // MUST set "webdriver.gecko.driver" via a system property
        return new FirefoxDriver();
    }
}
