package com.parasoft.parabank.it.util;

import org.openqa.selenium.WebDriver;

public class TitlePage extends PageObject {
    public TitlePage(WebDriver driver, String ... titleElements) {
        super(driver);
        PageUtil.waitForTitleContains(driver, titleElements);
    }
}
