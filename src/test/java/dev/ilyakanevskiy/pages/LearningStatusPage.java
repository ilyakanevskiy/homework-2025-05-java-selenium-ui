package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static java.lang.String.format;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class LearningStatusPage {
    private static final String TAB_ID_PREFIX = "tab_entry_tab_entry_";
    private static final String CURRENT_TAB = "current";
    private static final String COMPLETED_TAB = "completed";
    private static final String CANCELLED_TAB = "cancelled";
    private static final String WISHLIST_TAB = "wishlist";
    private static final String COURSE_CARD_TITLE_CSS = "article.LearningCard h4[title='%s']";

    private final WebDriver driver;

    public LearningStatusPage(WebDriver driver) {
        this.driver = driver;
    }

    private Optional<WebElement> getTab(String name) {
        return driver.findElements(By.id(TAB_ID_PREFIX + name)).stream().findFirst();
    }

    private Optional<WebElement> getCourseCardTitle(String courseName) {
        return driver.findElements(By.cssSelector(format(COURSE_CARD_TITLE_CSS, courseName))).stream().findFirst();
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

    public void openWishlist() {
        required(getTab(WISHLIST_TAB), "Wishlist tab").click();
    }

    public boolean isCourseListed(String courseName) {
        try {
            waitFor(driver).until(visibilityOfElementLocated(By.cssSelector(format(COURSE_CARD_TITLE_CSS, courseName))));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openCourse(String courseName) {
        required(getCourseCardTitle(courseName), "Card of course '" + courseName + "'").click();
    }
}
