package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static dev.ilyakanevskiy.utils.SeleniumHelper.isControlEnabled;
import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;

public class DenialOfAccessPage {
    private static final String CONTENT_FRAME_NAME = "contentframe";
    private static final String MANAGER_FRAME_CSS = "iframe[name^='iframe_navi:']";
    private static final String SEARCH_FIELD_ID = "searchTerm-inputEl";
    private static final String SEARCH_BUTTON_ID = "startSearchButton";
    private static final String USER_ROW_CSS = "tr.x-grid-row";
    private static final String CELL_CSS = "td";
    private static final int LOGIN_CELL_INDEX = 4;
    private static final int LOGIN_ATTEMPTS_CELL_INDEX = 5;
    private static final String RESET_MENU_BUTTON_ID = "tbb_reset";
    private static final String RESET_SELECTED_MENU_ITEM_ID = "tbi_reset";
    private static final String DIALOG_BUTTON_CSS = ".x-window .x-btn";
    private static final String RESET_DIALOG_BUTTON_TEXT = "Reset";

    private final WebDriver driver;

    @FindBy(id = SEARCH_FIELD_ID)
    private WebElement searchField;

    @FindBy(id = SEARCH_BUTTON_ID)
    private WebElement searchButton;

    public DenialOfAccessPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private <T> T inManagerFrame(Supplier<T> action) {
        driver.switchTo().defaultContent();
        waitFor(driver).until(frameToBeAvailableAndSwitchToIt(CONTENT_FRAME_NAME));
        waitFor(driver).until(frameToBeAvailableAndSwitchToIt(By.cssSelector(MANAGER_FRAME_CSS)));
        try {
            return action.get();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    private FluentWait<WebDriver> waitForGrid() {
        return waitFor(driver).ignoring(StaleElementReferenceException.class);
    }

    private Optional<WebElement> findUserRow(String login) {
        return driver.findElements(By.cssSelector(USER_ROW_CSS)).stream()
                .filter(row -> getCellText(row, LOGIN_CELL_INDEX).equals(login))
                .findFirst();
    }

    private static String getCellText(WebElement row, int index) {
        List<WebElement> cells = row.findElements(By.cssSelector(CELL_CSS));
        return cells.size() > index ? cells.get(index).getText().trim() : "";
    }

    public boolean isLoadingComplete() {
        try {
            return inManagerFrame(() -> waitFor(driver).until(driver -> isControlEnabled(searchField) && isControlEnabled(searchButton)));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void searchUser(String login) {
        inManagerFrame(() -> {
            searchField.clear();
            searchField.sendKeys(login);
            searchButton.click();
            return null;
        });
    }

    public Optional<Integer> getFailedLoginAttempts(String login) {
        return inManagerFrame(() -> {
            try {
                String attempts = waitForGrid().until(driver -> findUserRow(login)
                        .map(row -> getCellText(row, LOGIN_ATTEMPTS_CELL_INDEX))
                        .orElse(null));
                return Optional.of(Integer.parseInt(attempts));
            } catch (TimeoutException | NumberFormatException e) {
                return Optional.empty();
            }
        });
    }

    public boolean isUserAbsent(String login) {
        return inManagerFrame(() -> {
            try {
                return waitForGrid().until(driver -> findUserRow(login).isEmpty());
            } catch (TimeoutException e) {
                return false;
            }
        });
    }

    public void resetFailedLoginAttempts(String login) {
        inManagerFrame(() -> {
            WebElement row = required(findUserRow(login), "Row of user '" + login + "'");
            row.findElements(By.cssSelector(CELL_CSS)).get(LOGIN_CELL_INDEX).click();
            driver.findElement(By.id(RESET_MENU_BUTTON_ID)).click();
            waitFor(driver).until(elementToBeClickable(By.id(RESET_SELECTED_MENU_ITEM_ID))).click();
            WebElement confirmButton = waitFor(driver).until(driver -> driver.findElements(By.cssSelector(DIALOG_BUTTON_CSS)).stream()
                    .filter(button -> button.isDisplayed() && RESET_DIALOG_BUTTON_TEXT.equals(button.getText().trim()))
                    .findFirst()
                    .orElse(null));
            confirmButton.click();
            waitFor(driver).until(invisibilityOf(confirmButton));
            return null;
        });
    }
}
