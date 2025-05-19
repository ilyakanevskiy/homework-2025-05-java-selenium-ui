package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.HomePage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static org.assertj.core.api.Assertions.assertThat;

public class HomePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(HomePageSteps.class);
    private final HomePage homePage;

    public HomePageSteps(HomePage homePage) {
        this.homePage = homePage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(homePage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(CheckNavigationPanel.DESCRIPTION)
    public void checkNavigationPanel() {
        logger.info(adaptPlaceholder(CheckNavigationPanel.DESCRIPTION));
        assertThat(homePage.isNavigationPanelAvailable()).as(CheckNavigationPanel.ASSERTION).isTrue();
    }

    @Step(CheckLearnerControls.DESCRIPTION)
    public void checkLearnerControls() {
        logger.info(adaptPlaceholder(CheckLearnerControls.DESCRIPTION));
        assertThat(homePage.getUnavailableLearnerControls()).as(CheckLearnerControls.ASSERTION).isEmpty();
    }

    @Step(CheckAdminControls.DESCRIPTION)
    public void checkAdminControls() {
        logger.info(adaptPlaceholder(CheckAdminControls.DESCRIPTION));
        assertThat(homePage.getUnavailableAdminControls()).as(CheckAdminControls.ASSERTION).isEmpty();
    }

    @Step(GoToCatalogue.DESCRIPTION)
    public void goToCatalogue() {
        logger.info(adaptPlaceholder(GoToCatalogue.DESCRIPTION));
        homePage.goToCatalogue();
    }

    @Step(GoToLearningStatus.DESCRIPTION)
    public void goToLearningStatus() {
        logger.info(adaptPlaceholder(GoToLearningStatus.DESCRIPTION));
        homePage.goToLearningStatus();
    }

    @Step(OpenFromNavigationSearch.DESCRIPTION)
    public void openFromNavigationSearch(String menuItem) {
        logger.info(adaptPlaceholder(OpenFromNavigationSearch.DESCRIPTION), menuItem);
        homePage.openFromNavigationSearch(menuItem);
    }

    @Step(PerformLogout.DESCRIPTION)
    public void performLogout() {
        logger.info(adaptPlaceholder(PerformLogout.DESCRIPTION));
        homePage.performLogout();
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying home page content";
        private static final String ASSERTION = "Home page content should be available.";
    }

    private static class CheckNavigationPanel {
        private static final String DESCRIPTION = "Verifying navigation panel";
        private static final String ASSERTION = "Navigation panel should be available.";
    }

    private static class CheckLearnerControls {
        private static final String DESCRIPTION = "Verifying learner controls";
        private static final String ASSERTION = "Learner controls should be available.";
    }

    private static class CheckAdminControls {
        private static final String DESCRIPTION = "Verifying admin controls";
        private static final String ASSERTION = "Admin controls should be available.";
    }

    private static class GoToCatalogue {
        private static final String DESCRIPTION = "Attempting to go to catalogue";
    }

    private static class GoToLearningStatus {
        private static final String DESCRIPTION = "Attempting to go to learning status";
    }

    private static class OpenFromNavigationSearch {
        private static final String DESCRIPTION = "Attempting to open from navigation search: {0}";
    }

    public static class PerformLogout {
        private static final String DESCRIPTION = "Attempting to log out";
        public static final String RESULT = "Logging out successful";
    }
}
