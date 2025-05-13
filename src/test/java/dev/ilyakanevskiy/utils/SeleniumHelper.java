package dev.ilyakanevskiy.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class SeleniumHelper {
    private static final long DEFAULT_TIMEOUT_SECONDS = 20;

    private SeleniumHelper() {
    }

    public static boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isControlEnabled(WebElement control) {
        try {
            return control.isDisplayed() && control.isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    private static void waitForLoadingComplete(WebDriver driver, WebElement element, long timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(ExpectedConditions.visibilityOf(element));
    }

    private static void waitForLoadingComplete(WebDriver driver, List<WebElement> elements, long timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public static boolean isLoadingComplete(WebDriver driver, WebElement element) {
        return isLoadingComplete(driver, element, DEFAULT_TIMEOUT_SECONDS);
    }

    public static boolean isLoadingComplete(WebDriver driver, WebElement element, long timeoutInSeconds) {
        try {
            waitForLoadingComplete(driver, element, timeoutInSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static boolean isLoadingComplete(WebDriver driver, List<WebElement> elements) {
        return isLoadingComplete(driver, elements, DEFAULT_TIMEOUT_SECONDS);
    }

    public static boolean isLoadingComplete(WebDriver driver, List<WebElement> elements, long timeoutInSeconds) {
        try {
            waitForLoadingComplete(driver, elements, timeoutInSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static WebDriverWait waitFor(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    public static Optional<WebElement> subElementOf(WebElement parent, By locator) {
        try {
            return parent.findElements(locator).stream().findFirst();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return Optional.empty();
        }
    }

    public static WebElement required(Optional<WebElement> element, String description) {
        return element.orElseThrow(() -> new NoSuchElementException(description + " not found"));
    }
}
