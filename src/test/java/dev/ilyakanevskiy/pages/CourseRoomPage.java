package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;

import static dev.ilyakanevskiy.utils.SeleniumHelper.isElementDisplayed;
import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.subElementOf;
import static dev.ilyakanevskiy.utils.SeleniumHelper.unavailableOf;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static java.util.Map.entry;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

public class CourseRoomPage {
    private static final String HEADER_CSS = "div.dir-header";
    private static final String SYLLABUS_TAB_ID = "curriculumButton";
    private static final String COURSE_TITLE_CSS = "h1.dir-header__content-container-course__left__title-container-wrapper__title-container__title";
    private static final String COURSE_IMAGE_CSS = "div.dir-header__content-container-course__right__image";
    private static final String BACKGROUND_IMAGE_PROPERTY = "background-image";
    private static final String PROGRESS_BAR_CSS = "div.dir-header__content-container-course__left__progress__bar";
    private static final String PROGRESS_TEXT_CSS = "div.dir-header__content-container-course__left__progress__text";
    private static final String STATUS_CSS = "span[id^='strCourseSubscriptionState_']";
    private static final String RATING_CSS = "div.dir-header__content-container-course__rating";
    private static final String CANCEL_COURSE_TEXT = "Cancel course";
    private static final String MEDIA_TILE_CSS = "div.dir-tile__outer";
    private static final String MEDIA_TITLE_CSS = "div.dir-tile__title";
    private static final String MEDIA_TILE_BODY_CSS = "div.dir-tile";
    private static final String MEDIA_PASSED_CLASS = "dir-tile_status__passed";
    private static final String RETRIEVE_MEDIA_BUTTON_CSS = "button[title='Retrieve media']";

    private final WebDriver driver;
    private String courseRoomUrl;

    @FindBy(css = HEADER_CSS)
    private WebElement header;

    @FindBy(id = SYLLABUS_TAB_ID)
    private WebElement syllabusTab;

    public CourseRoomPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Optional<WebElement> findInHeader(By locator) {
        return subElementOf(header, locator);
    }

    private Optional<WebElement> getCancelCourseButton() {
        // The button may sit in a collapsed menu, and getText() returns an empty string for hidden elements.
        return header.findElements(By.tagName("button")).stream()
                .filter(button -> CANCEL_COURSE_TEXT.equals(button.getDomProperty("textContent").trim()))
                .findFirst();
    }

    private Optional<WebElement> getMediaTile(String mediaName) {
        return driver.findElements(By.cssSelector(MEDIA_TILE_CSS)).stream()
                .filter(tile -> subElementOf(tile, By.cssSelector(MEDIA_TITLE_CSS))
                        .map(title -> mediaName.equals(title.getDomAttribute("title")))
                        .orElse(false))
                .findFirst();
    }

    public boolean isLoadingComplete() {
        try {
            return waitFor(driver).until(driver -> isElementDisplayed(header) && isElementDisplayed(syllabusTab)
                    && driver.findElements(By.cssSelector(MEDIA_TILE_CSS)).stream().anyMatch(SeleniumHelper::isElementDisplayed));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public Optional<String> getCourseName() {
        return findInHeader(By.cssSelector(COURSE_TITLE_CSS)).filter(SeleniumHelper::isElementDisplayed).map(WebElement::getText);
    }

    public List<String> getUnavailableHeaderElements() {
        return unavailableOf(
                entry("Course image", findInHeader(By.cssSelector(COURSE_IMAGE_CSS)).filter(SeleniumHelper::isElementDisplayed).map(image -> image.getCssValue(BACKGROUND_IMAGE_PROPERTY).startsWith("url(")).orElse(false)),
                entry("Course progress bar", findInHeader(By.cssSelector(PROGRESS_BAR_CSS)).map(SeleniumHelper::isElementDisplayed).orElse(false)),
                entry("Course status", getCourseStatus().map(status -> !status.isBlank()).orElse(false)),
                entry("Rating", findInHeader(By.cssSelector(RATING_CSS)).map(SeleniumHelper::isElementDisplayed).orElse(false)),
                entry("'Cancel course' option", getCancelCourseButton().isPresent()));
    }

    public Optional<String> getCourseStatus() {
        return findInHeader(By.cssSelector(STATUS_CSS)).filter(SeleniumHelper::isElementDisplayed).map(WebElement::getText);
    }

    public Optional<String> getCourseProgress() {
        return findInHeader(By.cssSelector(PROGRESS_TEXT_CSS)).filter(SeleniumHelper::isElementDisplayed).map(WebElement::getText);
    }

    public List<String> getMissingMedia(List<String> mediaNames) {
        return mediaNames.stream()
                .filter(mediaName -> !getMediaTile(mediaName).map(SeleniumHelper::isElementDisplayed).orElse(false))
                .toList();
    }

    public boolean isMediaPassed(String mediaName) {
        try {
            return waitFor(driver).ignoring(StaleElementReferenceException.class)
                    .until(driver -> getMediaTile(mediaName)
                            .flatMap(tile -> subElementOf(tile, By.cssSelector(MEDIA_TILE_BODY_CSS)))
                            .map(body -> body.getDomAttribute("class").contains(MEDIA_PASSED_CLASS))
                            .orElse(false));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openMedia(String mediaName) {
        courseRoomUrl = driver.getCurrentUrl();
        WebElement tile = required(getMediaTile(mediaName), "Tile of media '" + mediaName + "'");
        required(subElementOf(tile, By.cssSelector(RETRIEVE_MEDIA_BUTTON_CSS)), "'Retrieve media' button of media '" + mediaName + "'").click();
        waitFor(driver).until(not(urlToBe(courseRoomUrl)));
        waitFor(driver).until(driver -> "complete".equals(((JavascriptExecutor) driver).executeScript("return document.readyState")));
    }

    public void reopen() {
        driver.get(courseRoomUrl);
    }

    public String getUrl() {
        return courseRoomUrl;
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
