package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.config.User;
import dev.ilyakanevskiy.pages.*;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.pages.ParticipantAdministrationPage.ParticipantList.CANCELLATIONS;
import static dev.ilyakanevskiy.pages.ParticipantAdministrationPage.ParticipantList.PARTICIPANTS;
import static dev.ilyakanevskiy.steps.HomePageSteps.PerformLogout.RESULT;

public class UserSteps {
    private static final Logger logger = LoggerFactory.getLogger(UserSteps.class);
    private static final String DENIAL_OF_ACCESS_MENU_ITEM = "Denial of access";
    private static final String ADMIN_CATEGORY = "Admin";

    private final StartPageSteps startPageSteps;
    private final HomePageSteps homePageSteps;
    private final CataloguePageSteps cataloguePageSteps;
    private final CoursePageSteps coursePageSteps;
    private final LoginPageSteps loginPageSteps;
    private final DenialOfAccessPageSteps denialOfAccessPageSteps;
    private final LearningStatusPageSteps learningStatusPageSteps;
    private final CourseManagerPageSteps courseManagerPageSteps;
    private final ParticipantAdministrationPageSteps participantAdministrationPageSteps;

    private boolean loggedIn = false;

    public UserSteps(WebDriver driver) {
        this.startPageSteps = new StartPageSteps(new StartPage(driver));
        this.homePageSteps = new HomePageSteps(new HomePage(driver));
        this.cataloguePageSteps = new CataloguePageSteps(new CataloguePage(driver));
        this.coursePageSteps = new CoursePageSteps(new CoursePage(driver));
        this.loginPageSteps = new LoginPageSteps(new LoginPage(driver));
        this.denialOfAccessPageSteps = new DenialOfAccessPageSteps(new DenialOfAccessPage(driver));
        this.learningStatusPageSteps = new LearningStatusPageSteps(new LearningStatusPage(driver));
        this.courseManagerPageSteps = new CourseManagerPageSteps(new CourseManagerPage(driver));
        this.participantAdministrationPageSteps = new ParticipantAdministrationPageSteps(new ParticipantAdministrationPage(driver));
    }

    @Step("Open start page")
    public void openStartPage() {
        startPageSteps.open();
        startPageSteps.checkStartPage();
    }

    @Step("Start new session")
    public void startNewSession() {
        startPageSteps.openInNewSession();
        startPageSteps.checkStartPage();
        loggedIn = false;
    }

    @Step("Log in as learner")
    public void loginAsLearner(User user) {
        startPageSteps.performLogin(user, 1);
        loginPageSteps.checkLoginAccepted();
        homePageSteps.checkLoadingIsComplete();
        homePageSteps.checkNavigationPanel();
        homePageSteps.checkLearnerControls();
        loggedIn = true;
    }

    @Step("Log in as admin")
    public void loginAsAdmin(User user) {
        startPageSteps.performLogin(user, 1);
        loginPageSteps.checkLoginAccepted();
        homePageSteps.checkLoadingIsComplete();
        homePageSteps.checkNavigationPanel();
        homePageSteps.checkAdminControls();
        loggedIn = true;
    }

    @Step("Try to log in with invalid credentials")
    public void tryToLoginWithInvalidCredentials(User user, int attemptNumber) {
        attemptLogin(user, attemptNumber);
        loginPageSteps.checkErrorMessageIsDisplayed();
    }

    @Step("Try to log in as blocked user")
    public void tryToLoginAsBlockedUser(User user, int attemptNumber) {
        attemptLogin(user, attemptNumber);
        loginPageSteps.checkBlockedUserMessageIsDisplayed();
    }

    private void attemptLogin(User user, int attemptNumber) {
        if (attemptNumber > 1) {
            openStartPage();
        }
        startPageSteps.performLoginWithInvalidPassword(user, attemptNumber);
        loginPageSteps.checkLoadingIsComplete();
    }

    @Step("Reset failed login attempts")
    public void resetFailedLoginAttempts(String username, int minimumAttempts) {
        openDenialOfAccessManager();
        denialOfAccessPageSteps.searchUser(username);
        denialOfAccessPageSteps.checkFailedLoginAttempts(username, minimumAttempts);
        resetListedUser(username);
    }

    @Step("Reset failed login attempts if there are any")
    public void resetFailedLoginAttemptsIfAny(User admin, User user) {
        loginAsAdmin(admin);
        openDenialOfAccessManager();
        denialOfAccessPageSteps.searchUser(user.username());
        if (denialOfAccessPageSteps.isUserListed(user.username())) {
            resetListedUser(user.username());
        }
        logout();
    }

    private void openDenialOfAccessManager() {
        homePageSteps.openFromNavigationSearch(DENIAL_OF_ACCESS_MENU_ITEM);
        denialOfAccessPageSteps.checkLoadingIsComplete();
    }

    private void resetListedUser(String username) {
        denialOfAccessPageSteps.resetFailedLoginAttempts(username);
        denialOfAccessPageSteps.searchUser(username);
        denialOfAccessPageSteps.checkUserIsNotListed(username);
    }

    @Step("Open catalogue")
    public void openCatalogue() {
        homePageSteps.goToCatalogue();
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkControls();
    }

    @Step("Select course")
    public void selectCourse(String searchPhrase, String expectedCourseName) {
        openCourseFromCatalogue(searchPhrase, expectedCourseName);
        coursePageSteps.checkBreadcrumbs();
        coursePageSteps.checkControls();
    }

    private void openCourseFromCatalogue(String searchPhrase, String expectedCourseName) {
        cataloguePageSteps.searchForCourse(searchPhrase);
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkSearchResult(expectedCourseName);
        cataloguePageSteps.goToCourse(expectedCourseName);
        coursePageSteps.checkLoadingIsComplete();
        coursePageSteps.checkProperties(expectedCourseName);
    }

    @Step("Add course to wishlist")
    public void addCourseToWishlist() {
        coursePageSteps.addToWishList();
        coursePageSteps.checkRemoveFromWishListIsAvailable();
    }

    @Step("Open learning status")
    public void openLearningStatus() {
        homePageSteps.goToLearningStatus();
        learningStatusPageSteps.checkLoadingIsComplete();
        learningStatusPageSteps.checkTabs();
        learningStatusPageSteps.checkWishlistIsLastTab();
    }

    @Step("Open course from wishlist")
    public void openCourseFromWishlist(String courseName) {
        learningStatusPageSteps.openWishlist();
        learningStatusPageSteps.checkCourseIsListed(courseName);
        learningStatusPageSteps.openCourse(courseName);
        coursePageSteps.checkLoadingIsComplete();
        coursePageSteps.checkProperties(courseName);
        coursePageSteps.checkRemoveFromWishListIsAvailable();
    }

    @Step("Remove course from wishlist")
    public void removeCourseFromWishlist() {
        coursePageSteps.removeFromWishList();
        coursePageSteps.checkAddToWishListIsAvailable();
    }

    @Step("Check course is not in wishlist")
    public void checkCourseIsNotInWishlist(String courseName) {
        homePageSteps.goToLearningStatus();
        learningStatusPageSteps.checkLoadingIsComplete();
        learningStatusPageSteps.openWishlist();
        learningStatusPageSteps.checkCourseIsNotListed(courseName);
    }

    @Step("Remove course from wishlist if it is there")
    public void removeCourseFromWishlistIfPresent(User user, String searchPhrase, String courseName) {
        loginAsLearner(user);
        openCatalogue();
        openCourseFromCatalogue(searchPhrase, courseName);
        if (coursePageSteps.isInWishList()) {
            removeCourseFromWishlist();
        }
        logout();
    }

    @Step("Remove participant from course if enrolled")
    public void removeParticipantFromCourseIfPresent(User admin, User learner, String searchPhrase, String courseName) {
        loginAsAdmin(admin);
        openParticipantAdministration(searchPhrase, courseName);
        participantAdministrationPageSteps.searchParticipant(learner.username(), PARTICIPANTS);
        if (participantAdministrationPageSteps.isParticipantListed(learner.username(), PARTICIPANTS)) {
            cancelParticipant(learner.username());
        } else {
            openCancellationList(learner.username());
        }
        if (participantAdministrationPageSteps.isParticipantListed(learner.username(), CANCELLATIONS)) {
            removeCancelledParticipant(learner.username());
        }
        participantAdministrationPageSteps.saveAndClose();
        logout();
    }

    private void openParticipantAdministration(String searchPhrase, String courseName) {
        homePageSteps.switchCategory(ADMIN_CATEGORY);
        homePageSteps.checkAdminNavigation();
        homePageSteps.goToCourseManager();
        courseManagerPageSteps.checkLoadingIsComplete();
        courseManagerPageSteps.searchCourse(searchPhrase, courseName);
        courseManagerPageSteps.checkCourseIsListed(courseName);
        courseManagerPageSteps.selectCourse(courseName);
        courseManagerPageSteps.openParticipantAdministration();
        participantAdministrationPageSteps.switchToWindow();
        participantAdministrationPageSteps.checkLoadingIsComplete(PARTICIPANTS);
    }

    private void cancelParticipant(String login) {
        participantAdministrationPageSteps.cancelParticipant(login);
        participantAdministrationPageSteps.checkParticipantIsNotListed(login, PARTICIPANTS);
        openCancellationList(login);
    }

    private void openCancellationList(String login) {
        participantAdministrationPageSteps.openCancellationList();
        participantAdministrationPageSteps.checkLoadingIsComplete(CANCELLATIONS);
        participantAdministrationPageSteps.searchParticipant(login, CANCELLATIONS);
    }

    private void removeCancelledParticipant(String login) {
        participantAdministrationPageSteps.removeParticipant(login);
        participantAdministrationPageSteps.checkParticipantIsNotListed(login, CANCELLATIONS);
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
