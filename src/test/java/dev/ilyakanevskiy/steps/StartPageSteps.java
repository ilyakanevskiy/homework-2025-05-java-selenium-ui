package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.config.User;
import dev.ilyakanevskiy.pages.StartPage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.config.ConfigReader.getProperty;
import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;

public class StartPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(StartPageSteps.class);
    private final StartPage startPage;

    public StartPageSteps(StartPage startPage) {
        this.startPage = startPage;
    }

    @Step(Open.DESCRIPTION)
    public void open() {
        logger.info(adaptPlaceholder(Open.DESCRIPTION));
        startPage.open(getProperty("base_url"));
    }

    public void checkStartPage() {
        checkLoadingIsComplete();
        checkControls();
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    private void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        Assertions.assertTrue(startPage.isLoadingComplete(), CheckLoadingIsComplete.ASSERTION);
    }

    @Step(CheckControls.DESCRIPTION)
    private void checkControls() {
        logger.info(adaptPlaceholder(CheckControls.DESCRIPTION));
        Assertions.assertTrue(startPage.areControlsAvailable(), CheckControls.ASSERTION);
    }

    @Step(PerformLogin.DESCRIPTION)
    public void performLogin(User user) {
        logger.info(adaptPlaceholder(PerformLogin.DESCRIPTION), user);
        startPage.performLogin(user.username(), user.password());
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

    private static class PerformLogin {
        private static final String DESCRIPTION = "Attempting to log in with username: {0}";
    }
}
