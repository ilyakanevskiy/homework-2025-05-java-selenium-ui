package dev.ilyakanevskiy.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static dev.ilyakanevskiy.extensions.WebdriverExtension.getWebDriver;
import static dev.ilyakanevskiy.utils.ScreenshotUtils.attachPageScreenshot;

public class AutoScreenshotExtension implements AfterEachCallback {
    private static final Logger logger = LoggerFactory.getLogger(AutoScreenshotExtension.class);

    @Override
    public void afterEach(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            captureScreenshot(context);
        }
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
