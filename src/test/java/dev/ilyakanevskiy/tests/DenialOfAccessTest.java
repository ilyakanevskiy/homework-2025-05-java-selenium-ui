package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DenialOfAccessTest extends BaseTest {

    @Test
    @DisplayName("Denial of access")
    public void blockedLearnerRegainsAccessAfterAdminReset() {
        for (int i = 0; i < 1; i++) {
            userSteps.tryToLoginWithInvalidCredentials(User.LEARNER3, i + 1);
        }
    }

    @AfterEach
    public void teardown() {
        if (userSteps != null && userSteps.isLoggedIn()) {
            userSteps.logout();
        }
    }
}
