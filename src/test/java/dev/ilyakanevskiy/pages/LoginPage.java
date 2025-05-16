package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Optional;

import static dev.ilyakanevskiy.utils.SeleniumHelper.subElementOf;

public class LoginPage {
    private static final String LOGIN_SECTION_ID = "login-section";
    private static final String ERROR_SPAN_CSS = "span#idm-error";
    private static final long LOADING_TIMEOUT_SECONDS = 10;

    private final WebDriver driver;

    @FindBy(id = LOGIN_SECTION_ID)
    private WebElement loginSection;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Optional<WebElement> findInLoginSection(By locator) {
        return subElementOf(loginSection, locator);
    }

    private Optional<WebElement> getErrorSpan() {
        return findInLoginSection(By.cssSelector(ERROR_SPAN_CSS));
    }

    public boolean isLoadingComplete() {
        return SeleniumHelper.isLoadingComplete(driver, loginSection, LOADING_TIMEOUT_SECONDS);
    }

    public Optional<String> getErrorMessage() {
        return getErrorSpan()
                .filter(SeleniumHelper::isElementDisplayed)
                .map(WebElement::getText);
    }
}
