package com.parasoft.parabank.webdriver.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class InternetExplorerSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {
    @Override
    public WebDriver getWebDriver() {
        // MUST set "webdriver.ie.driver" via a system property
        return new InternetExplorerDriver();
    }
}
