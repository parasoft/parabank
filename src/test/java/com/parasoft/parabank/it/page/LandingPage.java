package com.parasoft.parabank.it.page;

import org.openqa.selenium.WebDriver;

import com.parasoft.parabank.it.util.TitlePage;

public class LandingPage extends TitlePage {

    public LandingPage(WebDriver driver) {
        super(driver, "ParaBank", "Welcome", "Online Banking");
    }

    public LoginComponent getLogin() {
        return new LoginComponent(getDriver());
    }
}
