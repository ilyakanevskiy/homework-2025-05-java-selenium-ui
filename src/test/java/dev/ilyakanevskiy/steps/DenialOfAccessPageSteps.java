package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.DenialOfAccessPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static java.text.MessageFormat.format;
import static org.assertj.core.api.Assertions.assertThat;

public class DenialOfAccessPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(DenialOfAccessPageSteps.class);
    private final DenialOfAccessPage denialOfAccessPage;

    public DenialOfAccessPageSteps(DenialOfAccessPage denialOfAccessPage) {
        this.denialOfAccessPage = denialOfAccessPage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(denialOfAccessPage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(SearchUser.DESCRIPTION)
    public void searchUser(String username) {
        logger.info(adaptPlaceholder(SearchUser.DESCRIPTION), username);
        denialOfAccessPage.searchUser(username);
    }

    @Step(IsUserListed.DESCRIPTION)
    public boolean isUserListed(String username) {
        logger.info(adaptPlaceholder(IsUserListed.DESCRIPTION), username);
        return denialOfAccessPage.isUserListed(username);
    }

    @Step(CheckFailedLoginAttempts.DESCRIPTION)
    public void checkFailedLoginAttempts(String username, int minimumAttempts) {
        logger.info(adaptPlaceholder(CheckFailedLoginAttempts.DESCRIPTION), username, minimumAttempts);
        int attempts = denialOfAccessPage.getFailedLoginAttempts(username).orElse(0);
        assertThat(attempts)
                .as(format(CheckFailedLoginAttempts.ASSERTION, username, minimumAttempts))
                .isGreaterThanOrEqualTo(minimumAttempts);
    }

    @Step(ResetFailedLoginAttempts.DESCRIPTION)
    public void resetFailedLoginAttempts(String username) {
        logger.info(adaptPlaceholder(ResetFailedLoginAttempts.DESCRIPTION), username);
        denialOfAccessPage.resetFailedLoginAttempts(username);
    }

    @Step(CheckUserIsNotListed.DESCRIPTION)
    public void checkUserIsNotListed(String username) {
        logger.info(adaptPlaceholder(CheckUserIsNotListed.DESCRIPTION), username);
        assertThat(denialOfAccessPage.isUserAbsent(username)).as(format(CheckUserIsNotListed.ASSERTION, username)).isTrue();
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying denial of access manager";
        private static final String ASSERTION = "Denial of access manager should be available.";
    }

    private static class SearchUser {
        private static final String DESCRIPTION = "Attempting to search for user: {0}";
    }

    private static class IsUserListed {
        private static final String DESCRIPTION = "Checking whether user is listed: {0}";
    }

    private static class CheckFailedLoginAttempts {
        private static final String DESCRIPTION = "Verifying failed login attempts of user: {0}, at least: {1}";
        private static final String ASSERTION = "User: {0} should be listed with at least {1} failed login attempts.";
    }

    private static class ResetFailedLoginAttempts {
        private static final String DESCRIPTION = "Attempting to reset failed login attempts of user: {0}";
    }

    private static class CheckUserIsNotListed {
        private static final String DESCRIPTION = "Verifying user is no longer listed: {0}";
        private static final String ASSERTION = "User: {0} should not be listed after reset.";
    }
}
