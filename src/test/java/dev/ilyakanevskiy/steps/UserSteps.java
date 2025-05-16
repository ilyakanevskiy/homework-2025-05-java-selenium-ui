package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.config.User;
import dev.ilyakanevskiy.pages.*;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.steps.HomePageSteps.PerformLogout.RESULT;

public class UserSteps {
    private static final Logger logger = LoggerFactory.getLogger(UserSteps.class);

    private final StartPageSteps startPageSteps;
    private final HomePageSteps homePageSteps;
    private final CataloguePageSteps cataloguePageSteps;
    private final CoursePageSteps coursePageSteps;
    private final LoginPageSteps loginPageSteps;

    private boolean loggedIn = false;

    public UserSteps(WebDriver driver) {
        this.startPageSteps = new StartPageSteps(new StartPage(driver));
        this.homePageSteps = new HomePageSteps(new HomePage(driver));
        this.cataloguePageSteps = new CataloguePageSteps(new CataloguePage(driver));
        this.coursePageSteps = new CoursePageSteps(new CoursePage(driver));
        this.loginPageSteps = new LoginPageSteps(new LoginPage(driver));
    }

    @Step("Open start page")
    public void openStartPage() {
        startPageSteps.open();
        startPageSteps.checkStartPage();
    }

    @Step("Login as learner")
    public void loginAsLearner(User user) {
        startPageSteps.performLogin(user, 1);
        loginPageSteps.checkLoginAccepted();
        homePageSteps.checkLoadingIsComplete();
        homePageSteps.checkNavigationPanel();
        homePageSteps.checkLearnerControls();
        loggedIn = true;
    }

    @Step("Login as admin")
    public void loginAsAdmin(User user) {
        startPageSteps.performLogin(user, 1);
        loginPageSteps.checkLoginAccepted();
        homePageSteps.checkLoadingIsComplete();
        homePageSteps.checkNavigationPanel();
        homePageSteps.checkAdminControls();
        loggedIn = true;
    }

    @Step("Try to login with invalid credentials")
    public void tryToLoginWithInvalidCredentials(User user, int attemptNumber) {
        if (attemptNumber > 1) {
            startPageSteps.open();
            startPageSteps.checkStartPage();
        }
        startPageSteps.performLoginWithInvalidPassword(user, attemptNumber);
        loginPageSteps.checkLoadingIsComplete();
        loginPageSteps.checkErrorMessageIsDisplayed();
    }

    @Step("Open catalogue")
    public void openCatalogue() {
        homePageSteps.goToCatalogue();
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkControls();
    }

    @Step("Select course")
    public void selectCourse(String searchPhrase, String expectedCourseName) {
        cataloguePageSteps.searchForCourse(searchPhrase);
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkSearchResult(expectedCourseName);
        cataloguePageSteps.goToCourse(expectedCourseName);
        coursePageSteps.checkLoadingIsComplete();
        coursePageSteps.checkProperties(expectedCourseName);
        coursePageSteps.checkBreadcrumbs();
        coursePageSteps.checkControls();
    }

    @Step("Logout")
    public void logout() {
        homePageSteps.performLogout();
        startPageSteps.checkStartPage();
        loggedIn = false;
        logger.info(RESULT);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
