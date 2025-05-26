package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
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

public class CourseManagerPage {
    private static final String MANAGER_FRAME_CSS = "iframe[name^='iframe_navi:']";
    private static final String SEARCH_FIELD_ID = "searchTerm-inputEl";
    private static final String SEARCH_BUTTON_ID = "startSearchButton";
    private static final String COURSE_ROW_CSS = "tr.x-grid-row";
    private static final String CELL_CSS = "td";
    private static final String PARTICIPANTS_MENU_BUTTON_ID = "tbb_participants";
    private static final String PARTICIPANT_ADMINISTRATION_MENU_ITEM_ID = "tbi_participants";
    private static final Duration SEARCH_REPEAT_INTERVAL = Duration.ofSeconds(3);

    private final WebDriver driver;

    public CourseManagerPage(WebDriver driver) {
        this.driver = driver;
    }

    private <T> T inManagerFrame(Supplier<T> action) {
        return inFrame(driver, By.cssSelector(MANAGER_FRAME_CSS), action);
    }

    private List<WebElement> getCourseRows() {
        return driver.findElements(By.cssSelector(COURSE_ROW_CSS));
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

    private Optional<WebElement> findCourseRow(String courseName) {
        return getCourseRows().stream()
                .filter(row -> rowContains(row, courseName))
                .findFirst();
    }

    public boolean isLoadingComplete() {
        try {
            return inManagerFrame(() -> waitFor(driver).until(driver -> isControlEnabled(driver.findElement(By.id(SEARCH_FIELD_ID)))
                    && isControlEnabled(driver.findElement(By.id(SEARCH_BUTTON_ID)))));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void searchCourse(String searchPhrase, String courseName) {
        inManagerFrame(() -> {
            WebElement searchField = driver.findElement(By.id(SEARCH_FIELD_ID));
            searchField.clear();
            searchField.sendKeys(searchPhrase);
            // A search started while the grid is still loading its initial content is lost, so repeat it until the grid is filtered.
            AtomicBoolean searched = new AtomicBoolean(false);
            waitFor(driver).ignoring(StaleElementReferenceException.class).pollingEvery(SEARCH_REPEAT_INTERVAL).until(driver -> {
                List<WebElement> rows = getCourseRows();
                if (searched.get() && !rows.isEmpty() && rows.stream().allMatch(row -> rowContains(row, courseName))) {
                    return true;
                }
                driver.findElement(By.id(SEARCH_BUTTON_ID)).click();
                searched.set(true);
                return false;
            });
            return null;
        });
    }

    public boolean isCourseListed(String courseName) {
        return inManagerFrame(() -> findCourseRow(courseName).isPresent());
    }

    public void selectCourse(String courseName) {
        inManagerFrame(() -> {
            cellContaining(required(findCourseRow(courseName), "Row of course '" + courseName + "'"), courseName).click();
            return null;
        });
    }

    public void openParticipantAdministration() {
        inManagerFrame(() -> {
            driver.findElement(By.id(PARTICIPANTS_MENU_BUTTON_ID)).click();
            waitFor(driver).until(elementToBeClickable(By.id(PARTICIPANT_ADMINISTRATION_MENU_ITEM_ID))).click();
            return null;
        });
    }
}
