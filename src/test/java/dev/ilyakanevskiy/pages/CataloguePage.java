package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static dev.ilyakanevskiy.utils.SeleniumHelper.isControlEnabled;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
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

    private WebElement foundCourse;

    public CataloguePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean areControlsAvailable() {
        return isControlEnabled(searchField) && isControlEnabled(searchButton);
    }

    public boolean isLoadingComplete() {
        try {
            waitForLoadingComplete();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitForLoadingComplete() {
        waitFor(driver).until(driver -> {
            try {
                return (Boolean) (listItemElement.isDisplayed() || noContentElement.isDisplayed());
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return (Boolean) false;
            }
        });
    }

    public void searchForCourse(String searchPhrase) {
        List<WebElement> previousTiles = driver.findElements(By.cssSelector(LIST_ITEM_CSS));
        searchField.sendKeys(searchPhrase);
        searchButton.click();
        if (!previousTiles.isEmpty()) {
            waitFor(driver).until(stalenessOf(previousTiles.get(0)));
        }
    }

    public boolean checkSearchResult(String expectedCourseName) {
        foundCourse = driver.findElements(By.cssSelector(LIST_ITEM_CSS)).stream()
                .filter(element -> element
                        .findElement(By.cssSelector(LIST_TITLE_CSS))
                        .getText()
                        .equals(expectedCourseName))
                .findFirst()
                .orElse(null);
        return foundCourse != null;
    }

    public void goToCourse() {
        foundCourse.click();
    }
}
