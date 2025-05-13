package dev.ilyakanevskiy.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Objects;

import static dev.ilyakanevskiy.extensions.WebdriverExtension.getWebDriver;
import static dev.ilyakanevskiy.utils.ScreenshotUtils.attachPageScreenshot;

// Allure records the test and each setup/teardown method as a separate report section and closes the section
// when the method throws; exception handlers run only after that. Allure's interceptor is autodetected and
// therefore wraps this one, so a screenshot taken here lands in the section where the failure happened.
public class AutoScreenshotExtension implements InvocationInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AutoScreenshotExtension.class);

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext context) throws Throwable {
        proceedWithScreenshotOnFailure(invocation, context);
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext context) throws Throwable {
        proceedWithScreenshotOnFailure(invocation, context);
    }

    @Override
    public void interceptBeforeEachMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                          ExtensionContext context) throws Throwable {
        proceedWithScreenshotOnFailure(invocation, context);
    }

    @Override
    public void interceptAfterEachMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                         ExtensionContext context) throws Throwable {
        proceedWithScreenshotOnFailure(invocation, context);
    }

    private void proceedWithScreenshotOnFailure(Invocation<Void> invocation, ExtensionContext context) throws Throwable {
        try {
            invocation.proceed();
        } catch (Throwable throwable) {
            captureScreenshot(context);
            throw throwable;
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
