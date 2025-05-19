package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;

import static dev.ilyakanevskiy.utils.SeleniumHelper.isControlEnabled;
import static dev.ilyakanevskiy.utils.SeleniumHelper.isElementDisplayed;
import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.unavailableOf;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static java.util.Map.entry;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

public class CataloguePage {
    private static final String LIST_ITEM_CSS = "div.imc-list-item";
    private static final String LIST_TITLE_CSS = "div.imc-list-title";
    private static final String NO_CONTENT_CSS = "div.no-content-page";
    private static final String SEARCH_FIELD_ID = "searchField";
    private static final String SEARCH_BUTTON_ID = "startSearchBtn";

    private final WebDriver driver;

    @FindBy(css = LIST_ITEM_CSS)
    private WebElement listItemElement;

    @FindBy(css = NO_CONTENT_CSS)
    private WebElement noContentElement;

    @FindBy(id = SEARCH_FIELD_ID)
    private WebElement searchField;

    @FindBy(id = SEARCH_BUTTON_ID)
    private WebElement searchButton;

    public CataloguePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List<String> getUnavailableControls() {
        return unavailableOf(
                entry("'Search' input", isControlEnabled(searchField)),
                entry("'Search' button", isControlEnabled(searchButton)));
    }

    public boolean isLoadingComplete() {
        try {
            waitFor(driver).until(driver -> isElementDisplayed(listItemElement) || isElementDisplayed(noContentElement));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void searchForCourse(String searchPhrase) {
        List<WebElement> previousTiles = driver.findElements(By.cssSelector(LIST_ITEM_CSS));
        searchField.sendKeys(searchPhrase);
        searchButton.click();
        if (!previousTiles.isEmpty()) {
            waitFor(driver).until(stalenessOf(previousTiles.get(0)));
        }
    }

    private Optional<WebElement> findCourseTile(String courseName) {
        return driver.findElements(By.cssSelector(LIST_ITEM_CSS)).stream()
                .filter(element -> element
                        .findElement(By.cssSelector(LIST_TITLE_CSS))
                        .getText()
                        .equals(courseName))
                .findFirst();
    }

    public boolean checkSearchResult(String expectedCourseName) {
        try {
            return waitFor(driver).ignoring(StaleElementReferenceException.class)
                    .until(driver -> findCourseTile(expectedCourseName).isPresent());
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void goToCourse(String courseName) {
        waitFor(driver).ignoring(StaleElementReferenceException.class).until(driver -> {
            required(findCourseTile(courseName), "Course tile '" + courseName + "'").click();
            return true;
        });
    }
}
