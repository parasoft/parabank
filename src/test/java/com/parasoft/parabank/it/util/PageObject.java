package com.parasoft.parabank.it.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageObject {
    private final WebDriver driver;

    public PageObject(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
