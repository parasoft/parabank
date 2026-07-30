package com.parabank.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/parabank/index.htm");
    }

    @Test
    void validLoginShowsAccountsOverview() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");

        String pageSource = driver.getPageSource();
        System.out.println("PAGE TITLE: " + driver.getTitle());
        System.out.println("PAGE SOURCE SNIPPET: " + pageSource.substring(0, Math.min(500, pageSource.length())));

        assertTrue(pageSource.contains("Accounts Overview"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
