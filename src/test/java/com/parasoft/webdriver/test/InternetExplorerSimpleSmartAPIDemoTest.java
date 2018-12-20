package com.parasoft.webdriver.test;

import org.junit.Test;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class InternetExplorerSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {
    @Test
    public void testSmartAPIDemo() throws Throwable {
        // MUST set "webdriver.ie.driver" via a system property
        doSmartAPIDemo(new InternetExplorerDriver());
    }
}
