package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Optional;

import static dev.ilyakanevskiy.utils.SeleniumHelper.isElementDisplayed;
import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.subElementOf;

public class HomePage {
    private static final String DASHBOARDS_SECTION_ID = "dashboards-section";
    private static final String NAVIGATION_PANEL_ID = "navigationRoot";
    private static final String PROFILE_BUTTON_CSS = "button[title='User profile folder']";
    private static final String CATEGORIES_BUTTON_CSS = "button[title='Categories switch']";
    private static final String CATALOGUE_BUTTON_CSS = "a[title='Catalogue']";
    private static final String SIGN_OUT_BUTTON_CSS = "a[title='Sign out']";

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

    public boolean isNavigationPanelAvailable() {
        return isElementDisplayed(navigationPanel);
    }

    public boolean areLearnerControlsAvailable() {
        return getProfileButton().map(SeleniumHelper::isElementDisplayed).orElse(false)
                && getCatalogueButton().map(SeleniumHelper::isElementDisplayed).orElse(false);
    }

    public boolean areAdminControlsAvailable() {
        return getProfileButton().map(SeleniumHelper::isElementDisplayed).orElse(false)
                && getCategoriesButton().map(SeleniumHelper::isElementDisplayed).orElse(false);
    }

    public boolean isLoadingComplete() {
        return SeleniumHelper.isLoadingComplete(driver, dashboardsSection);
    }

    public void goToCatalogue() {
        required(getCatalogueButton(), "Catalogue button").click();
    }

    public void performLogout() {
        required(getProfileButton(), "User profile button").click();
        signOutButton.click();
    }
}
