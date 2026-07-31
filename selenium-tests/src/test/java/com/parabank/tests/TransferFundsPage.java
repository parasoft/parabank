package com.parabank.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class TransferFundsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By amountField = By.id("amount");
    private By fromAccountDropdown = By.id("fromAccountId");
    private By toAccountDropdown = By.id("toAccountId");
    private By transferButton = By.xpath("//input[@value='Transfer']");

    public TransferFundsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void transfer(String amount) {
        driver.findElement(amountField).sendKeys(amount);

        Select fromDropdown = new Select(driver.findElement(fromAccountDropdown));
        fromDropdown.selectByIndex(0);

        Select toDropdown = new Select(driver.findElement(toAccountDropdown));
        toDropdown.selectByIndex(1);

        driver.findElement(transferButton).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("showResult"), "Transfer Complete!"));
    }
}
