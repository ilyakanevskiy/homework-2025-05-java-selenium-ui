package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DenialOfAccessTest extends BaseTest {
    private static final int FAILED_LOGIN_ATTEMPTS_TO_BLOCK = 3;

    @Test
    @DisplayName("Denial of access")
    public void blockedLearnerRegainsAccessAfterAdminReset() {
        for (int attempt = 1; attempt < FAILED_LOGIN_ATTEMPTS_TO_BLOCK; attempt++) {
            userSteps.tryToLoginWithInvalidCredentials(User.LEARNER3, attempt);
        }
        userSteps.tryToLoginAsBlockedUser(User.LEARNER3, FAILED_LOGIN_ATTEMPTS_TO_BLOCK);

        userSteps.openStartPage();
        userSteps.loginAsAdmin(User.ADMIN);
        userSteps.resetFailedLoginAttempts(User.LEARNER3.username(), FAILED_LOGIN_ATTEMPTS_TO_BLOCK);
        userSteps.logout();

        userSteps.loginAsLearner(User.LEARNER3);
        userSteps.logout();
    }

    @AfterEach
    public void teardown() {
        if (userSteps != null && userSteps.isLoggedIn()) {
            userSteps.logout();
        }
    }
}
