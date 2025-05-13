package dev.ilyakanevskiy.pages;

import dev.ilyakanevskiy.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;

import static dev.ilyakanevskiy.utils.SeleniumHelper.required;
import static dev.ilyakanevskiy.utils.SeleniumHelper.subElementOf;
import static dev.ilyakanevskiy.utils.SeleniumHelper.unavailableOf;
import static java.util.Map.entry;

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

    private Optional<WebElement> findInLoginPanel(By locator) {
        return subElementOf(loginPanel, locator);
    }

    private Optional<WebElement> getLoginField() {
        return findInLoginPanel(By.id(LOGIN_FIELD_ID));
    }

    private Optional<WebElement> getPasswordField() {
        return findInLoginPanel(By.id(PASSWORD_FIELD_ID));
    }

    private Optional<WebElement> getStayLoggedInField() {
        return findInLoginPanel(By.cssSelector(STAY_LOGGED_IN_FIELD_CSS));
    }

    private Optional<WebElement> getSubmitButton() {
        return findInLoginPanel(By.cssSelector(SUBMIT_BUTTON_CSS));
    }

    public boolean isLoadingComplete() {
        return SeleniumHelper.isLoadingComplete(driver, loginPanel, LOADING_TIMEOUT_SECONDS);
    }

    public List<String> getUnavailableControls() {
        return unavailableOf(
                entry("'Login' input", getLoginField().map(SeleniumHelper::isControlEnabled).orElse(false)),
                entry("'Password' input", getPasswordField().map(SeleniumHelper::isControlEnabled).orElse(false)),
                entry("'Stay logged in' checkbox", getStayLoggedInField().map(SeleniumHelper::isControlEnabled).orElse(false)),
                entry("'Login' button", getSubmitButton().map(SeleniumHelper::isControlEnabled).orElse(false)));
    }

    public void performLogin(String login, String password) {
        required(getLoginField(), "Login field").sendKeys(login);
        required(getPasswordField(), "Password field").sendKeys(password);
        WebElement stayLoggedInField = required(getStayLoggedInField(), "'Stay logged in' checkbox");
        if (!stayLoggedInField.isSelected()) {
            stayLoggedInField.click();
        }
        required(getSubmitButton(), "Login button").click();
    }
}
