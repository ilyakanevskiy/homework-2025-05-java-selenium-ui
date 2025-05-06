package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.HomePage;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(HomePageSteps.class);
    private final HomePage homePage;

    public HomePageSteps(HomePage homePage) {
        this.homePage = homePage;
    }

    public void checkLoadingIsComplete() {
        logger.info(CheckLoadingIsComplete.DESCRIPTION);
        Assertions.assertTrue(homePage.isLoadingComplete(), CheckLoadingIsComplete.ASSERTION);
    }

    public void checkNavigationPanel() {
        logger.info(CheckNavigationPanel.DESCRIPTION);
        Assertions.assertTrue(homePage.isNavigationPanelAvailable(), CheckNavigationPanel.ASSERTION);
    }

    public void checkLearnerControls() {
        logger.info(CheckLearnerControls.DESCRIPTION);
        Assertions.assertTrue(homePage.areLearnerControlsAvailable(), CheckLearnerControls.ASSERTION);
    }

    public void goToCatalogue() {
        logger.info(GoToCatalogue.DESCRIPTION);
        homePage.goToCatalogue();
    }

    public void performLogout() {
        logger.info(PerformLogout.DESCRIPTION);
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

    private static class GoToCatalogue {
        private static final String DESCRIPTION = "Attempting to go to catalogue";
    }

    public static class PerformLogout {
        private static final String DESCRIPTION = "Attempting to log out";
        public static final String RESULT = "Logging out successful";
    }
}
