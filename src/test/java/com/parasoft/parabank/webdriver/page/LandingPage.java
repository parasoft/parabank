package com.parasoft.parabank.webdriver.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class LandingPage {
    private WebDriver _driver;

    public LandingPage(WebDriver driver) {
        _driver = driver;
        Util.waitForTitleContains(driver, "ParaBank", "Welcome", "Online Banking");
    }

    public LoginComponent getLogin() {
        return PageFactory.initElements(_driver, LoginComponent.class);
    }
}
