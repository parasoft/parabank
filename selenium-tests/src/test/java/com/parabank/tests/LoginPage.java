package com.parabank.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ఇక్కడ మీరు Inspect చేసి తీసుకున్న XPaths పెట్టండి
    private By usernameField = By.xpath("//input[@name='username']");
    private By passwordField = By.xpath("//input[@name='password']");
    private By loginButton = By.xpath("//input[@value='Log In']");

    // Constructor - Test class నుండి driver ని అందుకుంటుంది
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        // గరిష్టంగా 10 సెకన్ల వరకు వేచి ఉండటానికి సిద్ధమైన "wait" object
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Action method - login చేసే పని ఇక్కడే జరుగుతుంది
    public void login(String username, String password) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();

        // "Accounts Overview" పేజీ టైటిల్‌లో వచ్చేవరకు వేచి ఉండు (ఎంత త్వరగా వస్తే అంత త్వరగా కొనసాగుతుంది)
        wait.until(ExpectedConditions.titleContains("Accounts Overview"));
    }
}
