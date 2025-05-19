package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.config.User;
import dev.ilyakanevskiy.pages.StartPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.config.ConfigReader.getProperty;
import static dev.ilyakanevskiy.config.TestData.INVALID_PASSWORD;
import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static org.assertj.core.api.Assertions.assertThat;

public class StartPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(StartPageSteps.class);
    private final StartPage startPage;

    public StartPageSteps(StartPage startPage) {
        this.startPage = startPage;
    }

    @Step(Open.DESCRIPTION)
    public void open() {
        logger.info(adaptPlaceholder(Open.DESCRIPTION));
        String url = getProperty("base_url");
        startPage.open(url);
        checkServerResponse(url);
    }

    @Step(OpenInNewSession.DESCRIPTION)
    public void openInNewSession() {
        logger.info(adaptPlaceholder(OpenInNewSession.DESCRIPTION));
        String url = getProperty("base_url");
        startPage.openInNewSession(url);
        checkServerResponse(url);
    }

    private void checkServerResponse(String url) {
        assertThat(startPage.getTitle()).as(CheckServerResponse.ASSERTION, url).doesNotMatch(CheckServerResponse.SERVER_ERROR_TITLE);
    }

    public void checkStartPage() {
        checkLoadingIsComplete();
        checkControls();
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    private void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(startPage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(CheckControls.DESCRIPTION)
    private void checkControls() {
        logger.info(adaptPlaceholder(CheckControls.DESCRIPTION));
        assertThat(startPage.getUnavailableControls()).as(CheckControls.ASSERTION).isEmpty();
    }

    @Step(PerformLogin.DESCRIPTION)
    public void performLogin(User user, int attemptNumber) {
        logger.info(adaptPlaceholder(PerformLogin.DESCRIPTION), attemptNumber, user);
        startPage.performLogin(user.username(), user.password());
    }

    @Step(PerformLoginWithInvalidPassword.DESCRIPTION)
    public void performLoginWithInvalidPassword(User user, int attemptNumber) {
        logger.info(adaptPlaceholder(PerformLoginWithInvalidPassword.DESCRIPTION), attemptNumber, user);
        startPage.performLogin(user.username(), INVALID_PASSWORD);
    }

    private static class Open {
        private static final String DESCRIPTION = "Opening start page";
    }

    private static class OpenInNewSession {
        private static final String DESCRIPTION = "Opening start page in a new session";
    }

    private static class CheckServerResponse {
        private static final String ASSERTION = "Server should respond without an error at URL: %s";
        private static final String SERVER_ERROR_TITLE = "(5\\d\\d|404) .*";
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying login panel";
        private static final String ASSERTION = "Login panel should be available.";
    }

    private static class CheckControls {
        private static final String DESCRIPTION = "Verifying login controls";
        private static final String ASSERTION = "Login controls ('Login', 'Password' and 'Stay logged in' inputs, and 'Login' button) should be available.";
    }

    private static class PerformLogin {
        private static final String DESCRIPTION = "Attempt: {1} to log in with username: {0}";
    }

    private static class PerformLoginWithInvalidPassword {
        private static final String DESCRIPTION = "Attempt: {1} to log in with username: {0} and an invalid password";
    }
}
