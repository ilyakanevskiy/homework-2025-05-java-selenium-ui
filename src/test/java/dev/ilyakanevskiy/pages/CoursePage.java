package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.subElementOf;
import static dev.ilyakanevskiy.utils.SeleniumHelper.unavailableOf;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static java.util.Arrays.asList;
import static java.util.Map.entry;

public class CoursePage {
    private static final String COURSE_DESCRIPTION_LEFT_CSS = "div.description__left";
    private static final String COURSE_DESCRIPTION_RIGHT_CSS = "div.description__right";
    private static final String COURSE_HEADER_LEFT_CSS = "div.header__left";
    private static final String COURSE_HEADER_RIGHT_CSS = "div.header__right";

    private static final String COURSE_TITLE_CSS = "div.imc-header__title-wrapper__title";
    private static final String COURSE_TYPE_CSS = "span.imc-header__label__text";
    private static final String COURSE_IMAGE_CSS = "div.imc-header-right-side__image div.imc-container";
    private static final String BACKGROUND_IMAGE_PROPERTY = "background-image";
    private static final String BREADCRUMBS_ITEM_CSS = "button.header-breadcrumbs__item__link";
    private static final String ENROL_BUTTON_CSS = "button[title='Enrol']";
    private static final String ADD_TO_WISH_LIST_BUTTON_CSS = "button[title='Add to wish list']";
    private static final String REMOVE_FROM_WISH_LIST_BUTTON_CSS = "button[title='Remove from wish list']";

    private final WebDriver driver;

    @FindBy(css = COURSE_DESCRIPTION_LEFT_CSS)
    private WebElement courseDescriptionLeftElement;
    @FindBy(css = COURSE_DESCRIPTION_RIGHT_CSS)
    private WebElement courseDescriptionRightElement;
    @FindBy(css = COURSE_HEADER_LEFT_CSS)
    private WebElement courseHeaderLeftElement;
    @FindBy(css = COURSE_HEADER_RIGHT_CSS)
    private WebElement courseHeaderRightElement;

    public CoursePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isLoadingComplete() {
        return SeleniumHelper.isLoadingComplete(driver, asList(courseDescriptionLeftElement, courseDescriptionRightElement, courseHeaderLeftElement, courseHeaderRightElement));
    }

    private Optional<WebElement> getCourseTitle() {
        return subElementOf(courseHeaderLeftElement, By.cssSelector(COURSE_TITLE_CSS));
    }

    private Optional<WebElement> getCourseType() {
        return subElementOf(courseHeaderLeftElement, By.cssSelector(COURSE_TYPE_CSS));
    }

    private Optional<WebElement> getCourseImage() {
        return subElementOf(courseHeaderRightElement, By.cssSelector(COURSE_IMAGE_CSS));
    }

    private Optional<WebElement> getBreadcrumbsItem() {
        return subElementOf(courseHeaderLeftElement, By.cssSelector(BREADCRUMBS_ITEM_CSS));
    }

    private Optional<WebElement> getEnrolButton() {
        return subElementOf(courseDescriptionRightElement, By.cssSelector(ENROL_BUTTON_CSS));
    }

    private Optional<WebElement> getAddToWishListButton() {
        return subElementOf(courseDescriptionRightElement, By.cssSelector(ADD_TO_WISH_LIST_BUTTON_CSS));
    }

    private Optional<WebElement> getRemoveFromWishListButton() {
        return subElementOf(courseDescriptionRightElement, By.cssSelector(REMOVE_FROM_WISH_LIST_BUTTON_CSS));
    }

    private boolean isButtonAvailable(Supplier<Optional<WebElement>> button) {
        try {
            return waitFor(driver).ignoring(StaleElementReferenceException.class)
                    .until(driver -> button.get().map(SeleniumHelper::isControlEnabled).orElse(false));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public Optional<String> getCourseName() {
        return getCourseTitle().filter(SeleniumHelper::isElementDisplayed).map(WebElement::getText);
    }

    public List<String> getUnavailableProperties() {
        return unavailableOf(
                entry("Course image", getCourseImage().filter(SeleniumHelper::isElementDisplayed).map(image -> image.getCssValue(BACKGROUND_IMAGE_PROPERTY).startsWith("url(")).orElse(false)),
                entry("Course type", getCourseType().filter(SeleniumHelper::isElementDisplayed).map(type -> !type.getText().isBlank()).orElse(false)));
    }

    public boolean areBreadcrumbsAvailable() {
        return getBreadcrumbsItem().map(SeleniumHelper::isElementDisplayed).orElse(false);
    }

    public List<String> getUnavailableControls() {
        return unavailableOf(
                entry("'Enrol' button", getEnrolButton().map(SeleniumHelper::isControlEnabled).orElse(false)),
                entry("'Add to wish list' button", getAddToWishListButton().map(SeleniumHelper::isControlEnabled).orElse(false)));
    }

    public boolean isAddToWishListAvailable() {
        return isButtonAvailable(this::getAddToWishListButton);
    }

    public boolean isRemoveFromWishListAvailable() {
        return isButtonAvailable(this::getRemoveFromWishListButton);
    }

    public void addToWishList() {
        required(getAddToWishListButton(), "'Add to wish list' button").click();
    }

    public void removeFromWishList() {
        required(getRemoveFromWishListButton(), "'Remove from wish list' button").click();
    }
}
