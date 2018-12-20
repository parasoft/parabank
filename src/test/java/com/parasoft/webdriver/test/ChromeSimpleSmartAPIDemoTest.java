package com.parasoft.webdriver.test;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeSimpleSmartAPIDemoTest extends SimpleSmartAPIDemoTest {
    @Test
    public void testSmartAPIDemo() throws Throwable {
        // MUST set "webdriver.chrome.driver" via a system property
        doSmartAPIDemo(new ChromeDriver());
    }
}
