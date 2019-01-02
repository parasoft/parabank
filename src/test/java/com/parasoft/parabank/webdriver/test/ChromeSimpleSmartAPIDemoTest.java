package com.parasoft.parabank.webdriver.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {
    @Override
    public WebDriver getWebDriver() {
        // MUST set "webdriver.chrome.driver" via a system property
        return new ChromeDriver();
    }
}
