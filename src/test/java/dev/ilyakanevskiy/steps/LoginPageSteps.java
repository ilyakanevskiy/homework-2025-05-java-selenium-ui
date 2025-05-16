package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.LoginPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.config.TestData.INVALID_CREDENTIALS_MESSAGE;
import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(LoginPageSteps.class);
    private final LoginPage loginPage;

    public LoginPageSteps(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(loginPage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(CheckErrorMessage.DESCRIPTION)
    public void checkErrorMessageIsDisplayed() {
        logger.info(adaptPlaceholder(CheckErrorMessage.DESCRIPTION));
        assertThat(loginPage.getErrorMessage()).as(CheckErrorMessage.ASSERTION).hasValue(INVALID_CREDENTIALS_MESSAGE);
    }

    @Step(CheckLoginAccepted.DESCRIPTION)
    public void checkLoginAccepted() {
        logger.info(adaptPlaceholder(CheckLoginAccepted.DESCRIPTION));
        assertThat(loginPage.getErrorMessage()).as(CheckLoginAccepted.ASSERTION).isEmpty();
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying login section";
        private static final String ASSERTION = "Login section should be available.";
    }

    private static class CheckErrorMessage {
        private static final String DESCRIPTION = "Verifying error message";
        private static final String ASSERTION = "Error message should be displayed for invalid credentials.";
    }

    private static class CheckLoginAccepted {
        private static final String DESCRIPTION = "Verifying login is accepted";
        private static final String ASSERTION = "Login should be accepted without an error message.";
    }
}
