package com.parabank.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferFundsTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/parabank/index.htm");

        // ముందు Login అవ్వాలి - LoginPage ని reuse చేస్తున్నాం
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");
    }

    @Test
    void transferShowsCompleteMessage() {
        // "Transfer Funds" లింక్ నొక్కడం
        driver.findElement(By.linkText("Transfer Funds")).click();

        TransferFundsPage transferFundsPage = new TransferFundsPage(driver);
        transferFundsPage.transfer("50");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Transfer Complete!"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
