package dev.ilyakanevskiy.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static dev.ilyakanevskiy.extensions.WebdriverExtension.getWebDriver;
import static dev.ilyakanevskiy.utils.ScreenshotUtils.attachPageScreenshot;

public class AutoScreenshotExtension implements TestExecutionExceptionHandler, LifecycleMethodExecutionExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AutoScreenshotExtension.class);

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        captureScreenshot(context);
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        captureScreenshot(context);
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        captureScreenshot(context);
        throw throwable;
    }

    private void captureScreenshot(ExtensionContext context) {
        final WebDriver driver = getWebDriver(context);
        if (Objects.nonNull(driver)) {
            // A failed screenshot must not replace the original failure in the report.
            try {
                attachPageScreenshot(driver, "Screenshot on failure");
            } catch (RuntimeException e) {
                logger.error("Failed to capture screenshot on failure: {}", e.getMessage());
            }
        }
    }
}
