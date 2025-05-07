package dev.ilyakanevskiy.extensions;

import java.util.Objects;

import dev.ilyakanevskiy.utils.ScreenshotUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoScreenshotExtension implements TestExecutionExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AutoScreenshotExtension.class);

    @Override
    public void handleTestExecutionException(final ExtensionContext context, final Throwable throwable) throws Throwable {
        WebDriver webDriver = WebdriverExtension.getWebDriver(context);
        if (Objects.nonNull(webDriver)) {
            final String attachmentName = "Screenshot on failure (" + throwable.getClass().getSimpleName() + ")";
            // A failed screenshot must not replace the original failure in the report.
            try {
                ScreenshotUtils.attachPageScreenshot(webDriver, attachmentName);
            } catch (RuntimeException e) {
                logger.error("Failed to capture screenshot on failure: {}", e.getMessage());
            }
        }
        throw throwable;
    }
}
