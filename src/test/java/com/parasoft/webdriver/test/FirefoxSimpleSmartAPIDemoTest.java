package com.parasoft.webdriver.test;

import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {
    @Test
    public void testSmartAPIDemo() throws Throwable {
        // MUST set "webdriver.gecko.driver" via a system property
        doSmartAPIDemo(new FirefoxDriver());
    }
}
