package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static dev.ilyakanevskiy.utils.SeleniumHelper.isControlEnabled;

public class StartPage {
    private static final String LOGIN_PANEL_ID = "dashboard-login-panel-160";
    private static final String LOGIN_FIELD_ID = "externalForm:login";
    private static final String PASSWORD_FIELD_ID = "externalForm:password";
    private static final String STAY_LOGGED_IN_FIELD_CSS = "input.login-panel__checkbox__checkbox[name='externalForm:stay-logged-in-checkbox'][type='checkbox']";
    private static final String SUBMIT_BUTTON_CSS = "button[type='submit']";
    private static final long LOADING_TIMEOUT_SECONDS = 10;

    private final WebDriver driver;

    @FindBy(id = LOGIN_PANEL_ID)
    private WebElement loginPanel;

    public StartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open(String url) {
        driver.get(url);
    }

    private WebElement getLoginField() {
        return loginPanel.findElement(By.id(LOGIN_FIELD_ID));
    }

    private WebElement getPasswordField() {
        return loginPanel.findElement(By.id(PASSWORD_FIELD_ID));
    }

    private WebElement getStayLoggedInField() {
        return loginPanel.findElement(By.cssSelector(STAY_LOGGED_IN_FIELD_CSS));
    }

    private WebElement getSubmitButton() {
        return loginPanel.findElement(By.cssSelector(SUBMIT_BUTTON_CSS));
    }

    public boolean isLoadingComplete() {
        return SeleniumHelper.isLoadingComplete(driver, loginPanel, LOADING_TIMEOUT_SECONDS);
    }

    public boolean areControlsAvailable() {
        return isControlEnabled(getLoginField()) && isControlEnabled(getPasswordField()) && isControlEnabled(getStayLoggedInField()) && isControlEnabled(getSubmitButton());
    }

    public void performLogin(String login, String password) {
        getLoginField().sendKeys(login);
        getPasswordField().sendKeys(password);
        if (!getStayLoggedInField().isSelected()) {
            getStayLoggedInField().click();
        }
        getSubmitButton().click();
    }
}
