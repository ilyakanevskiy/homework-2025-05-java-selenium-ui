package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.extensions.AutoScreenshotExtension;
import dev.ilyakanevskiy.extensions.WebdriverExtension;
import dev.ilyakanevskiy.steps.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assumptions.abort;

@ExtendWith({WebdriverExtension.class, AutoScreenshotExtension.class})
public abstract class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected UserSteps userSteps;

    @BeforeEach
    void setup(WebDriver driver) {
        driver.manage().window().maximize();
        UserSteps steps = new UserSteps(driver);
        steps.openStartPage();
        // Set only once the start page is available: teardown skips the cleanup if the system is down.
        userSteps = steps;
    }

    // A test that cannot start from the initial state is skipped, not failed. Used only before the test:
    // aborting in teardown would turn a passed test into a skipped one.
    protected static void assumeInitialStateRestored(Runnable restore) {
        try {
            restore.run();
        } catch (AssertionError | RuntimeException e) {
            logger.error("Failed to restore the initial state: {}", e.getMessage());
            abort("Initial state could not be restored: " + e.getMessage());
        }
    }
}
