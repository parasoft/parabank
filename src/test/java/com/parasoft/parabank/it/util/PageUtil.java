package com.parasoft.parabank.it.util;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBeMoreThan;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageUtil {
    public static final long DEFAULT_WAIT_FOR_TITLE_TIMEOUT = 20;
    public static final long DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT = 10;
    public static final long DEFAULT_WAIT_FOR_DIALOG_TIMEOUT = 10;

    private PageUtil() {
        // do not instantiate
    }

    public static void waitForTitleContains(WebDriver driver, String... titleElements) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_FOR_TITLE_TIMEOUT));
        Arrays.stream(titleElements).forEach(element -> {
            wait.until(titleContains(element));
            // The following works for pages embedded in frames.
            // This is not necessary for now so we are just using
            // ExpectedConditions.titleContains as seen above.
            //wait.until(attributeContains(By.tagName("title"), "innerHTML", element));
        });
    }

    public static void waitForElements(By locator, int numOfElementsMoreThan, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_FOR_TITLE_TIMEOUT));
        wait.until(numberOfElementsToBeMoreThan(locator, numOfElementsMoreThan));
    }

    public static void waitUntilVisible(WebElement element, WebDriver driver) {
        waitUntilVisible(element, driver, DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT);
    }

    public static void waitUntilVisible(WebElement element, WebDriver driver, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(visibilityOf(element));
    }

    public static void waitUntilClickable(WebElement element, WebDriver driver, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.ignoring(StaleElementReferenceException.class);
        wait.until(elementToBeClickable(element));
    }

    public static void waitUntilClickable(WebElement element, WebDriver driver) {
        waitUntilClickable(element, driver, DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT);
    }

    public static void waitUntilInvisible(WebElement element, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT));
        wait.until(invisibilityOf(element));
    }

    public static void waitAndType(WebElement element, WebDriver driver, String text) {
        // checks that element is enabled
        waitUntilClickable(element, driver);
        element.sendKeys(text);
    }

    public static void waitClearAndType(WebElement element, WebDriver driver, String text) {
        // checks that element is enabled
        waitUntilClickable(element, driver);
        element.clear();
        element.sendKeys(text);
    }

    public static void waitAndClick(WebElement element, WebDriver driver) {
        waitUntilClickable(element, driver);
        element.click();
    }

    public static void waitAndClick(WebElement element, WebDriver driver, long timeout) {
        waitUntilClickable(element, driver, timeout);
        element.click();
    }

    public static void waitAndSelectByValue(WebElement element, WebDriver driver, String value) {
        waitUntilClickable(element, driver);
        waitUntilSelectOptionsPopulated(element, driver);
        Select dropdown = new Select(element);
        dropdown.selectByValue(value);
    }

    public static void waitAndSelect(WebElement element, WebDriver driver, String option, long timeout) {
        waitUntilClickable(element, driver, timeout);
        waitUntilSelectOptionsPopulated(element, driver, timeout);
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(option);
    }

    public static void waitAndSelect(WebElement element, WebDriver driver, String option) {
        waitAndSelect(element, driver, option, DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT);
    }

    private static void waitUntilSelectOptionsPopulated(WebElement element, WebDriver driver, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.ignoring(StaleElementReferenceException.class);
        wait.until(webdriver -> new Select(element).getFirstSelectedOption().getText().trim().length() > 0);
    }

    private static void waitUntilSelectOptionsPopulated(WebElement element, WebDriver driver) {
        waitUntilSelectOptionsPopulated(element, driver, DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT);
    }

    public static void scrollIntoView(WebElement element, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_FOR_ELEMENT_TIMEOUT));
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.until(e -> ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(false); arguments[0].scrollIntoView(true); return true;", element));
    }

    public static boolean isVisibleAfterWaiting(WebElement element, WebDriver driver) {
        boolean isVisible = false;
        try {
            waitUntilVisible(element, driver);
            isVisible = true;
        } catch (WebDriverException e) { }
        return isVisible;
    }

    public static String waitUntilVisibleAndGetText(WebElement element, WebDriver driver) {
        waitUntilVisible(element, driver);
        return element.getText();
    }

    public static void waitForDialog(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_FOR_DIALOG_TIMEOUT));
        wait.until(alertIsPresent());
    }
}
