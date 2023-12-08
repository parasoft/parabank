package com.parasoft.parabank.it.page;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.parasoft.parabank.it.util.PageUtil;
import com.parasoft.parabank.it.util.TitlePage;

public class RequestLoanPage extends TitlePage {

    @FindBy(id = "amount")
    private WebElement amountField;

    @FindBy(id = "downPayment")
    private WebElement downPaymentField;

    @FindBy(id = "fromAccountId")
    private WebElement fromAccountIdDropdown;

    @FindBy(id = "loanProviderName")
    private WebElement loanProviderName;

    public RequestLoanPage(WebDriver driver) {
        super(driver, "ParaBank", "Loan", "Request");
    }

    // Annotated WebElement, no wait condition
    public void setAmountField(String text) {
        amountField.clear();
        amountField.sendKeys(text);
    }

    public void setDownPaymentField(String text) {
        downPaymentField.clear();
        downPaymentField.sendKeys(text);
    }

    // Annotated WebElement, insufficient wait condition
    public void selectFromAccountIdDropdown(String text) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(1));
        wait.until(visibilityOf(fromAccountIdDropdown));

        Select dropdown = new Select(fromAccountIdDropdown);
        dropdown.selectByVisibleText(text);
    }

    // Find element, no wait condition
    public void clickApplyNowButton() {
        WebElement applyNowButton = getDriver().findElement(By.cssSelector("input[value='Apply Now']"));
        applyNowButton.click();
    }

    public String getLoanProviderName() {
        return PageUtil.waitUntilVisibleAndGetText(loanProviderName, getDriver());
    }
}
