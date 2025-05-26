package dev.ilyakanevskiy.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

    @SafeVarargs
    public static List<String> unavailableOf(Map.Entry<String, Boolean>... elements) {
        return Stream.of(elements)
                .filter(element -> !element.getValue())
                .map(Map.Entry::getKey)
                .toList();
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

    public static <T> T inFrame(WebDriver driver, By frameLocator, Supplier<T> action) {
        waitFor(driver).ignoring(StaleElementReferenceException.class).ignoring(NoSuchFrameException.class)
                .until(ignored -> switchToFrame(driver, frameLocator));
        try {
            return action.get();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    // ExtJS managers nest their frames differently, so the frame is searched for at any depth.
    private static boolean switchToFrame(WebDriver driver, By frameLocator) {
        driver.switchTo().defaultContent();
        return switchToFrameWithin(driver, frameLocator);
    }

    private static boolean switchToFrameWithin(WebDriver driver, By frameLocator) {
        List<WebElement> matches = driver.findElements(frameLocator);
        if (!matches.isEmpty()) {
            driver.switchTo().frame(matches.get(0));
            return true;
        }
        for (WebElement frame : driver.findElements(By.tagName("iframe"))) {
            driver.switchTo().frame(frame);
            if (switchToFrameWithin(driver, frameLocator)) {
                return true;
            }
            driver.switchTo().parentFrame();
        }
        return false;
    }
}
