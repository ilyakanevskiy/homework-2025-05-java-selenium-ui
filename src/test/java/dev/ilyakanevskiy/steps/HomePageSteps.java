package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.HomePage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;

public class HomePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(HomePageSteps.class);
    private final HomePage homePage;

    public HomePageSteps(HomePage homePage) {
        this.homePage = homePage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        Assertions.assertTrue(homePage.isLoadingComplete(), CheckLoadingIsComplete.ASSERTION);
    }

    @Step(CheckNavigationPanel.DESCRIPTION)
    public void checkNavigationPanel() {
        logger.info(adaptPlaceholder(CheckNavigationPanel.DESCRIPTION));
        Assertions.assertTrue(homePage.isNavigationPanelAvailable(), CheckNavigationPanel.ASSERTION);
    }

    @Step(CheckLearnerControls.DESCRIPTION)
    public void checkLearnerControls() {
        logger.info(adaptPlaceholder(CheckLearnerControls.DESCRIPTION));
        Assertions.assertTrue(homePage.areLearnerControlsAvailable(), CheckLearnerControls.ASSERTION);
    }

    @Step(CheckAdminControls.DESCRIPTION)
    public void checkAdminControls() {
        logger.info(adaptPlaceholder(CheckAdminControls.DESCRIPTION));
        Assertions.assertTrue(homePage.areAdminControlsAvailable(), CheckAdminControls.ASSERTION);
    }

    @Step(GoToCatalogue.DESCRIPTION)
    public void goToCatalogue() {
        logger.info(adaptPlaceholder(GoToCatalogue.DESCRIPTION));
        homePage.goToCatalogue();
    }

    @Step(PerformLogout.DESCRIPTION)
    public void performLogout() {
        logger.info(adaptPlaceholder(PerformLogout.DESCRIPTION));
        homePage.performLogout();
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying home page content";
        private static final String ASSERTION = "Home page content is not available.";
    }

    private static class CheckNavigationPanel {
        private static final String DESCRIPTION = "Verifying navigation panel";
        private static final String ASSERTION = "Navigation panel is not available.";
    }

    private static class CheckLearnerControls {
        private static final String DESCRIPTION = "Verifying learner controls";
        private static final String ASSERTION = "Learner controls are not available.";
    }

    private static class CheckAdminControls {
        private static final String DESCRIPTION = "Verifying admin controls";
        private static final String ASSERTION = "Admin controls are not available.";
    }

    private static class GoToCatalogue {
        private static final String DESCRIPTION = "Attempting to go to catalogue";
    }

    public static class PerformLogout {
        private static final String DESCRIPTION = "Attempting to log out";
        public static final String RESULT = "Logging out successful";
    }
}
