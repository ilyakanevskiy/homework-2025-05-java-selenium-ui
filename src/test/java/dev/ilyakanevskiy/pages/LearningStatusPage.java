package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static java.lang.String.format;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class LearningStatusPage {
    private static final String TAB_ID_PREFIX = "mylearnings-";
    private static final String CURRENT_TAB = "current";
    private static final String COMPLETED_TAB = "completed";
    private static final String CANCELLED_TAB = "cancelled";
    private static final String WISHLIST_TAB = "wishlist";
    private static final String WISHLIST_TAB_NAME = "Wishlist tab";
    private static final String COURSE_TITLE_XPATH = "//div[normalize-space()='%s']";
    private static final String LOADING_OVERLAY_CLASS = "LoadingOverlay";
    private static final Duration COURSE_ABSENCE_TIMEOUT = Duration.ofSeconds(5);

    private final WebDriver driver;

    public LearningStatusPage(WebDriver driver) {
        this.driver = driver;
    }

    private Optional<WebElement> getTab(String name) {
        return driver.findElements(By.id(TAB_ID_PREFIX + name)).stream().findFirst();
    }

    private Optional<WebElement> getCourseTitle(String courseName) {
        return driver.findElements(By.xpath(format(COURSE_TITLE_XPATH, courseName))).stream().findFirst();
    }

    public boolean isLoadingComplete() {
        try {
            waitFor(driver).until(visibilityOfElementLocated(By.id(TAB_ID_PREFIX + WISHLIST_TAB)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public List<String> getUnavailableTabs() {
        return Stream.of(CURRENT_TAB, COMPLETED_TAB, CANCELLED_TAB, WISHLIST_TAB)
                .filter(tab -> !getTab(tab).map(SeleniumHelper::isElementDisplayed).orElse(false))
                .map(tab -> "'" + tab + "' tab")
                .toList();
    }

    private int getTabPosition(String name) {
        return required(getTab(name), "'" + name + "' tab").getLocation().getX();
    }

    public boolean isWishlistLastTab() {
        int wishlistPosition = getTabPosition(WISHLIST_TAB);
        return Stream.of(CURRENT_TAB, COMPLETED_TAB, CANCELLED_TAB)
                .allMatch(tab -> getTabPosition(tab) < wishlistPosition);
    }

    public void openWishlist() {
        waitFor(driver).until(invisibilityOfElementLocated(By.className(LOADING_OVERLAY_CLASS)));
        required(getTab(WISHLIST_TAB), WISHLIST_TAB_NAME).click();
    }

    public boolean isCourseListed(String courseName) {
        try {
            waitFor(driver).until(visibilityOfElementLocated(By.xpath(format(COURSE_TITLE_XPATH, courseName))));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // A list without the course has no element to wait for, so give the course time to appear.
    public boolean isCourseAbsent(String courseName) {
        try {
            new WebDriverWait(driver, COURSE_ABSENCE_TIMEOUT).until(visibilityOfElementLocated(By.xpath(format(COURSE_TITLE_XPATH, courseName))));
            return false;
        } catch (TimeoutException e) {
            return true;
        }
    }

    public void openCourse(String courseName) {
        required(getCourseTitle(courseName), "Title of course '" + courseName + "'").click();
    }
}
