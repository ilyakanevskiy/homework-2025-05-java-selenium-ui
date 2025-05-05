package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.StartPage;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.config.ConfigReader.getProperty;

public class StartPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(StartPageSteps.class);
    private final StartPage startPage;

    public StartPageSteps(StartPage startPage) {
        this.startPage = startPage;
    }

    public void open() {
        logger.info(Open.DESCRIPTION);
        startPage.open(getProperty("base_url"));
    }

    public void checkStartPage() {
        checkLoadingIsComplete();
        checkControls();
    }

    private void checkLoadingIsComplete() {
        logger.info(CheckLoadingIsComplete.DESCRIPTION);
        Assertions.assertTrue(startPage.isLoadingComplete(), CheckLoadingIsComplete.ASSERTION);
    }

    private void checkControls() {
        logger.info(CheckControls.DESCRIPTION);
        Assertions.assertTrue(startPage.areControlsAvailable(), CheckControls.ASSERTION);
    }

    private static class Open {
        private static final String DESCRIPTION = "Opening start page";
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying login panel";
        private static final String ASSERTION = "Login panel is not available.";
    }

    private static class CheckControls {
        private static final String DESCRIPTION = "Verifying login controls";
        private static final String ASSERTION = "Login controls ('Login', 'Password' or 'Stay logged in' inputs, or 'Login' button) are not available.";
    }
}
