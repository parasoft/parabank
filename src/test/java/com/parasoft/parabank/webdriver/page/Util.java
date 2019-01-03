package com.parasoft.parabank.webdriver.page;

import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Util {
    private static final long DEFAULT_WAIT_FOR_TITLE_TIMEOUT = 10;
    private static final long DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT = 5;

    public static void waitForTitleContains(WebDriver driver, String... titleElements) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_FOR_TITLE_TIMEOUT);
        Arrays.stream(titleElements).forEach(element -> {
            wait.until(ExpectedConditions.titleContains(element));
        });
    }

    public static void waitUntilVisible(WebElement element, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitUntilClickable(WebElement element, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitAndType(WebElement element, WebDriver driver, String text) {
        // checks that element is enabled
        waitUntilClickable(element, driver);
        element.sendKeys(text);
    }

    public static void waitAndClick(WebElement element, WebDriver driver) {
        waitUntilClickable(element, driver);
        element.click();
    }

    public static void waitAndSelect(WebElement element, WebDriver driver, String option) {
        waitUntilClickable(element, driver);
        waitUntilSelectOptionsPopulated(element, driver);
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(option);
    }

    private static void waitUntilSelectOptionsPopulated(WebElement element, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT);
        wait.until(webdriver -> new Select(element).getFirstSelectedOption().getText().trim().length() > 0);
    }
}
