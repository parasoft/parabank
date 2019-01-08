package com.parasoft.parabank.webdriver.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class EdgeSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {

    @Override
    public WebDriver getWebDriver() {
        // MUST set "webdriver.edge.driver" via a system property
        return new EdgeDriver();
    }

}
