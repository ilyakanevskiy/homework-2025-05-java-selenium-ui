package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.extensions.AutoScreenshotExtension;
import dev.ilyakanevskiy.extensions.WebdriverExtension;
import dev.ilyakanevskiy.steps.UserSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assumptions.abort;

@ExtendWith({WebdriverExtension.class, AutoScreenshotExtension.class})
public abstract class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    // Logging the shared admin out in one browser ends its session in the others,
    // so tests using the admin never run in parallel.
    protected static final String ADMIN_ACCOUNT = "admin account";

    protected UserSteps userSteps;
    private boolean testFailed;

    // Runs after the test method and before teardown. If setup fails, the test method and this callback do not run.
    @RegisterExtension
    final AfterTestExecutionCallback failureTracker = context -> testFailed = context.getExecutionException().isPresent();

    @BeforeEach
    void setup(WebDriver driver) {
        userSteps = new UserSteps(driver);
        userSteps.openStartPage();
        assumeInitialStateRestored();
    }

    // A passed scenario has already returned its account to the initial state, a failed one may have stopped halfway.
    @AfterEach
    void teardown() {
        if (testFailed) {
            userSteps.startNewSession();
            restoreInitialState();
        }
    }

    protected abstract void restoreInitialState();

    // A test that cannot start from the initial state is skipped, not failed.
    private void assumeInitialStateRestored() {
        try {
            restoreInitialState();
        } catch (AssertionError | RuntimeException e) {
            logger.error("Failed to restore the initial state: {}", e.getMessage());
            abort("Initial state could not be restored: " + e.getMessage());
        }
    }
}
