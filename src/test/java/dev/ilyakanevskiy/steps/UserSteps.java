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

    private boolean loggedIn = false;

    public UserSteps(WebDriver driver) {
        this.startPageSteps = new StartPageSteps(new StartPage(driver));
        this.homePageSteps = new HomePageSteps(new HomePage(driver));
        this.cataloguePageSteps = new CataloguePageSteps(new CataloguePage(driver));
    }

    @Step("Open start page")
    public void openStartPage() {
        startPageSteps.open();
        startPageSteps.checkStartPage();
    }

    @Step("Log in as learner")
    public void loginAsLearner(User user) {
        startPageSteps.performLogin(user);
        homePageSteps.checkLoadingIsComplete();
        homePageSteps.checkNavigationPanel();
        homePageSteps.checkLearnerControls();
        loggedIn = true;
    }

    @Step("Open catalogue")
    public void openCatalogue() {
        homePageSteps.goToCatalogue();
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkControls();
    }

    @Step("Find course")
    public void findCourse(String searchPhrase, String expectedCourseName) {
        cataloguePageSteps.searchForCourse(searchPhrase);
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkSearchResult(expectedCourseName);
    }

    @Step("Log out")
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
