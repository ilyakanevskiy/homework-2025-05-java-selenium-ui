package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static dev.ilyakanevskiy.utils.SeleniumHelper.inFrame;
import static dev.ilyakanevskiy.utils.SeleniumHelper.isControlEnabled;
import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class ParticipantAdministrationPage {
    private static final String MANAGER_FRAME_CSS = "iframe[name^='iframe_participants']";
    private static final String PARTICIPANT_LIST_FRAME_CSS = "iframe[name^='c_participants_booked_person']";
    private static final String CANCELLATION_LIST_FRAME_CSS = "iframe[name^='c_participants_cancelled_person']";
    private static final String TAB_CSS = ".x-tab";
    private static final String CANCELLATION_LIST_TAB_TEXT = "Cancellation list";
    private static final String SEARCH_FIELD_ID = "searchTerm-inputEl";
    private static final String SEARCH_BUTTON_ID = "startSearchButton";
    private static final String PARTICIPANT_ROW_CSS = "tr.x-grid-row";
    private static final String CELL_CSS = "td";
    private static final String PARTICIPANT_MENU_BUTTON_ID = "tbb_participant";
    private static final String CANCEL_MENU_ITEM_ID = "tbi_cancelParticipant";
    private static final String REMOVE_MENU_ITEM_ID = "tbi_removeParticipant";
    private static final String DIALOG_BUTTON_CSS = ".x-window .x-btn";
    private static final String CONFIRM_CANCELLATION_BUTTON_TEXT = "Confirm cancellation";
    private static final String SAVE_AND_CLOSE_BUTTON_ID = "default_saveClose";
    private static final Duration SEARCH_REPEAT_INTERVAL = Duration.ofSeconds(3);

    private final WebDriver driver;
    private String courseManagerWindow;

    public ParticipantAdministrationPage(WebDriver driver) {
        this.driver = driver;
    }

    private <T> T inManagerFrame(Supplier<T> action) {
        return inFrame(driver, By.cssSelector(MANAGER_FRAME_CSS), action);
    }

    private <T> T inList(ParticipantList list, Supplier<T> action) {
        return inFrame(driver, By.cssSelector(list.frameCss), action);
    }

    private List<WebElement> getParticipantRows() {
        return driver.findElements(By.cssSelector(PARTICIPANT_ROW_CSS));
    }

    // The grid highlights the search words in the rows, and getText() can lose a highlighted word: compare the raw text instead.
    private static boolean rowContains(WebElement row, String text) {
        return row.getDomProperty("textContent").contains(text);
    }

    private static WebElement cellContaining(WebElement row, String text) {
        return row.findElements(By.cssSelector(CELL_CSS)).stream()
                .filter(cell -> rowContains(cell, text))
                .findFirst()
                .orElse(row);
    }

    private Optional<WebElement> findParticipantRow(String login) {
        return getParticipantRows().stream()
                .filter(row -> rowContains(row, login))
                .findFirst();
    }

    private void openParticipantMenuItem(String login, String menuItemId) {
        cellContaining(required(findParticipantRow(login), "Row of participant '" + login + "'"), login).click();
        driver.findElement(By.id(PARTICIPANT_MENU_BUTTON_ID)).click();
        waitFor(driver).until(elementToBeClickable(By.id(menuItemId))).click();
    }

    public void switchToWindow() {
        courseManagerWindow = driver.getWindowHandle();
        waitFor(driver).until(numberOfWindowsToBe(2));
        driver.getWindowHandles().stream()
                .filter(handle -> !handle.equals(courseManagerWindow))
                .findFirst()
                .ifPresent(handle -> driver.switchTo().window(handle));
    }

    public boolean isLoadingComplete(ParticipantList list) {
        try {
            return inList(list, () -> waitFor(driver).until(driver -> isControlEnabled(driver.findElement(By.id(SEARCH_FIELD_ID)))
                    && isControlEnabled(driver.findElement(By.id(SEARCH_BUTTON_ID)))));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void searchParticipant(ParticipantList list, String login) {
        inList(list, () -> {
            WebElement searchField = driver.findElement(By.id(SEARCH_FIELD_ID));
            searchField.clear();
            searchField.sendKeys(login);
            // A search started while the grid is still loading its initial content is lost, so repeat it until the grid is filtered.
            AtomicBoolean searched = new AtomicBoolean(false);
            waitFor(driver).ignoring(StaleElementReferenceException.class).pollingEvery(SEARCH_REPEAT_INTERVAL).until(driver -> {
                if (searched.get() && getParticipantRows().stream().allMatch(row -> rowContains(row, login))) {
                    return true;
                }
                driver.findElement(By.id(SEARCH_BUTTON_ID)).click();
                searched.set(true);
                return false;
            });
            return null;
        });
    }

    public boolean isParticipantListed(ParticipantList list, String login) {
        return inList(list, () -> findParticipantRow(login).isPresent());
    }

    public boolean isParticipantAbsent(ParticipantList list, String login) {
        try {
            return inList(list, () -> waitFor(driver).ignoring(StaleElementReferenceException.class)
                    .until(driver -> findParticipantRow(login).isEmpty()));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void cancelParticipant(String login) {
        inList(ParticipantList.PARTICIPANTS, () -> {
            openParticipantMenuItem(login, CANCEL_MENU_ITEM_ID);
            WebElement confirmButton = waitFor(driver).until(driver -> driver.findElements(By.cssSelector(DIALOG_BUTTON_CSS)).stream()
                    .filter(button -> button.isDisplayed() && CONFIRM_CANCELLATION_BUTTON_TEXT.equals(button.getText().trim()))
                    .findFirst()
                    .orElse(null));
            confirmButton.click();
            waitFor(driver).until(invisibilityOf(confirmButton));
            return null;
        });
    }

    public void openCancellationList() {
        inManagerFrame(() -> {
            WebElement tab = waitFor(driver).until(driver -> driver.findElements(By.cssSelector(TAB_CSS)).stream()
                    .filter(element -> element.isDisplayed() && CANCELLATION_LIST_TAB_TEXT.equals(element.getText().trim()))
                    .findFirst()
                    .orElse(null));
            tab.click();
            return null;
        });
    }

    public void removeParticipant(String login) {
        inList(ParticipantList.CANCELLATIONS, () -> {
            openParticipantMenuItem(login, REMOVE_MENU_ITEM_ID);
            return null;
        });
    }

    public void saveAndClose() {
        try {
            inManagerFrame(() -> {
                driver.findElement(By.id(SAVE_AND_CLOSE_BUTTON_ID)).click();
                return null;
            });
        } catch (NoSuchWindowException e) {
            // 'Save and close' closes the window, possibly before the frame is left.
        }
        waitFor(driver).until(numberOfWindowsToBe(1));
        driver.switchTo().window(courseManagerWindow);
    }

    public enum ParticipantList {
        PARTICIPANTS(PARTICIPANT_LIST_FRAME_CSS),
        CANCELLATIONS(CANCELLATION_LIST_FRAME_CSS);

        private final String frameCss;

        ParticipantList(String frameCss) {
            this.frameCss = frameCss;
        }
    }
}
