package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
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
import static java.lang.String.format;
import static java.util.Map.entry;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class HomePage {
    private static final String DASHBOARDS_SECTION_ID = "dashboards-section";
    private static final String NAVIGATION_PANEL_ID = "navigationRoot";
    private static final String PROFILE_BUTTON_CSS = "button[title='User profile folder']";
    private static final String CATEGORIES_BUTTON_CSS = "button[title='Categories switch']";
    private static final String CATALOGUE_BUTTON_CSS = "a[title='Catalogue']";
    private static final String SIGN_OUT_BUTTON_CSS = "a[title='Sign out']";
    private static final String SEARCH_BUTTON_CSS = "button[title='Search']";
    private static final String SEARCH_NAVIGATION_FIELD_CSS = "input[placeholder='Search navigation']";
    private static final String SEARCH_NAVIGATION_RESULT_CSS = "a.search-option span[title='%s']";
    private static final String CLOSE_NOTIFICATION_CSS = "a[title='Close notification']";

    private final WebDriver driver;

    @FindBy(id = DASHBOARDS_SECTION_ID)
    private WebElement dashboardsSection;

    @FindBy(id = NAVIGATION_PANEL_ID)
    private WebElement navigationPanel;

    @FindBy(css = SIGN_OUT_BUTTON_CSS)
    private WebElement signOutButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Optional<WebElement> getProfileButton() {
        return subElementOf(navigationPanel, By.cssSelector(PROFILE_BUTTON_CSS));
    }

    private Optional<WebElement> getCategoriesButton() {
        return subElementOf(navigationPanel, By.cssSelector(CATEGORIES_BUTTON_CSS));
    }

    private Optional<WebElement> getCatalogueButton() {
        return subElementOf(navigationPanel, By.cssSelector(CATALOGUE_BUTTON_CSS));
    }

    private Optional<WebElement> getSearchButton() {
        return subElementOf(navigationPanel, By.cssSelector(SEARCH_BUTTON_CSS));
    }

    public boolean isNavigationPanelAvailable() {
        return isElementDisplayed(navigationPanel);
    }

    public List<String> getUnavailableLearnerControls() {
        return unavailableOf(
                entry("'User profile' button", getProfileButton().map(SeleniumHelper::isElementDisplayed).orElse(false)),
                entry("'Catalogue' button", getCatalogueButton().map(SeleniumHelper::isElementDisplayed).orElse(false)));
    }

    public List<String> getUnavailableAdminControls() {
        return unavailableOf(
                entry("'User profile' button", getProfileButton().map(SeleniumHelper::isElementDisplayed).orElse(false)),
                entry("'Categories switch' button", getCategoriesButton().map(SeleniumHelper::isElementDisplayed).orElse(false)));
    }

    public boolean isLoadingComplete() {
        return SeleniumHelper.isLoadingComplete(driver, dashboardsSection);
    }

    // The welcome notification stays open after login and can cover menu items. It is closed before a menu
    // is opened, because a click outside an open menu closes the menu.
    private void closeNotifications() {
        driver.findElements(By.cssSelector(CLOSE_NOTIFICATION_CSS)).stream()
                .filter(SeleniumHelper::isElementDisplayed)
                .forEach(WebElement::click);
    }

    public void goToCatalogue() {
        required(getCatalogueButton(), "Catalogue button").click();
    }

    public void openFromNavigationSearch(String menuItem) {
        closeNotifications();
        required(getSearchButton(), "Search button").click();
        waitFor(driver).until(elementToBeClickable(By.cssSelector(SEARCH_NAVIGATION_FIELD_CSS))).sendKeys(menuItem);
        waitFor(driver).until(elementToBeClickable(By.cssSelector(format(SEARCH_NAVIGATION_RESULT_CSS, menuItem)))).click();
    }

    public void performLogout() {
        required(getProfileButton(), "User profile button").click();
        signOutButton.click();
    }
}
