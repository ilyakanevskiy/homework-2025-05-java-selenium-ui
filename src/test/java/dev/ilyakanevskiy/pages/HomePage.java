package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static dev.ilyakanevskiy.utils.SeleniumHelper.isElementDisplayed;

public class HomePage {
    private static final String DASHBOARDS_SECTION_ID = "dashboards-section";
    private static final String NAVI_PANEL_ID = "navigationRoot";
    private static final String PROFILE_BUTTON_CSS = "button[title='User profile folder']";
    private static final String CATEGORIES_BUTTON_CSS = "button[title='Categories switch']";
    private static final String CATALOGUE_BUTTON_CSS = "a[title='Catalogue']";
    private static final String SIGN_OUT_BUTTON_CSS = "a[title='Sign out']";

    private final WebDriver driver;

    @FindBy(id = DASHBOARDS_SECTION_ID)
    private WebElement dashboardsSection;

    @FindBy(id = NAVI_PANEL_ID)
    private WebElement naviPanel;

    @FindBy(css = SIGN_OUT_BUTTON_CSS)
    private WebElement signOutButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private WebElement getProfileButton() {
        return naviPanel.findElement(By.cssSelector(PROFILE_BUTTON_CSS));
    }

    private WebElement getCategoriesButton() {
        return naviPanel.findElement(By.cssSelector(CATEGORIES_BUTTON_CSS));
    }

    private WebElement getCatalogueButton() {
        return naviPanel.findElement(By.cssSelector(CATALOGUE_BUTTON_CSS));
    }

    public boolean isNavigationPanelAvailable() {
        return isElementDisplayed(naviPanel);
    }

    public boolean areLearnerControlsAvailable() {
        return isElementDisplayed(getProfileButton());
    }

    public boolean areAdminControlsAvailable() {
        return isElementDisplayed(getProfileButton()) && isElementDisplayed(getCategoriesButton());
    }

    public boolean isLoadingComplete() {
        return SeleniumHelper.isLoadingComplete(driver, dashboardsSection);
    }

    public void goToCatalogue() {
        getCatalogueButton().click();
    }

    public void performLogout() {
        getProfileButton().click();
        signOutButton.click();
    }
}
