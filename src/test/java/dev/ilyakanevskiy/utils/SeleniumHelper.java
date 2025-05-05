package dev.ilyakanevskiy.utils;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumHelper {
    private static final long DEFAULT_TIMEOUT_SECONDS = 20;

    private SeleniumHelper() {
    }

    public static boolean isElementDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    public static boolean isControlEnabled(WebElement field) {
        return field.isDisplayed() && field.isEnabled();
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
}
