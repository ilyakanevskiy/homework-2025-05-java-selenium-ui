package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static dev.ilyakanevskiy.utils.SeleniumHelper.cellContaining;
import static dev.ilyakanevskiy.utils.SeleniumHelper.inFrames;
import static dev.ilyakanevskiy.utils.SeleniumHelper.isControlEnabled;
import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.rowContains;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;

public abstract class ManagerPage {
    protected static final By CONTENT_FRAME = By.name("contentframe");
    private static final String SEARCH_FIELD_ID = "searchTerm-inputEl";
    private static final String SEARCH_BUTTON_ID = "startSearchButton";
    private static final String ROW_CSS = "tr.x-grid-row";
    private static final String DIALOG_BUTTON_CSS = ".x-window .x-btn";
    private static final Duration SEARCH_REPEAT_INTERVAL = Duration.ofSeconds(3);

    protected final WebDriver driver;
    private final By managerFrame;

    protected ManagerPage(WebDriver driver, String managerFrameCss) {
        this.driver = driver;
        this.managerFrame = By.cssSelector(managerFrameCss);
    }

    protected <T> T inManagerFrame(Supplier<T> action) {
        return inFrames(driver, action, CONTENT_FRAME, managerFrame);
    }

    protected FluentWait<WebDriver> waitForGrid() {
        return waitFor(driver).ignoring(StaleElementReferenceException.class);
    }

    protected boolean waitForSearchControls() {
        return waitFor(driver).until(driver -> isControlEnabled(driver.findElement(By.id(SEARCH_FIELD_ID)))
                && isControlEnabled(driver.findElement(By.id(SEARCH_BUTTON_ID))));
    }

    private List<WebElement> getRows() {
        return driver.findElements(By.cssSelector(ROW_CSS));
    }

    protected Optional<WebElement> findRow(String text) {
        return getRows().stream()
                .filter(row -> rowContains(row, text))
                .findFirst();
    }

    protected static boolean allRowsContain(List<WebElement> rows, String text) {
        return rows.stream().allMatch(row -> rowContains(row, text));
    }

    protected void selectRow(String text, String description) {
        cellContaining(required(findRow(text), description), text).click();
    }

    protected void searchUntilFiltered(String searchTerm, Predicate<List<WebElement>> isFiltered) {
        WebElement searchField = driver.findElement(By.id(SEARCH_FIELD_ID));
        searchField.clear();
        searchField.sendKeys(searchTerm);
        // A search started while the grid is still loading its initial content is lost, so repeat it until the grid is filtered.
        AtomicBoolean searched = new AtomicBoolean(false);
        waitForGrid().pollingEvery(SEARCH_REPEAT_INTERVAL).until(driver -> {
            if (searched.get() && isFiltered.test(getRows())) {
                return true;
            }
            driver.findElement(By.id(SEARCH_BUTTON_ID)).click();
            searched.set(true);
            return false;
        });
    }

    protected void openMenuItem(String menuButtonId, String menuItemId) {
        driver.findElement(By.id(menuButtonId)).click();
        waitFor(driver).until(elementToBeClickable(By.id(menuItemId))).click();
    }

    protected void confirmDialog(String buttonText) {
        WebElement confirmButton = waitFor(driver).until(driver -> driver.findElements(By.cssSelector(DIALOG_BUTTON_CSS)).stream()
                .filter(button -> button.isDisplayed() && buttonText.equals(button.getText().trim()))
                .findFirst()
                .orElse(null));
        confirmButton.click();
        waitFor(driver).until(invisibilityOf(confirmButton));
    }
}
