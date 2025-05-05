package dev.ilyakanevskiy.extensions;

import dev.ilyakanevskiy.config.ConfigReader;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class WebdriverExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {
    public static final String WEBDRIVER = "webdriver";
    static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(WebdriverExtension.class);
    private static final Logger logger = LoggerFactory.getLogger(WebdriverExtension.class);

    public static WebDriver getWebDriver(ExtensionContext context) {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        return store.get(WEBDRIVER, WebDriver.class);
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == WebDriver.class;
    }

    @Override
    public WebDriver resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        final ExtensionContext.Store store = extensionContext.getStore(NAMESPACE);
        return store.get(WEBDRIVER, WebDriver.class);
    }

    @Override
    public void beforeEach(final ExtensionContext context) {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);

        String browser = ConfigReader.getBrowser();
        logger.info("Setting up WebDriver for browser: {}", browser);

        WebDriver driver;
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        store.put(WEBDRIVER, driver);
    }

    @Override
    public void afterEach(final ExtensionContext context) {
        final WebDriver maybeDriver = getWebDriver(context);
        if (Objects.nonNull(maybeDriver)) {
            logger.info("Closing WebDriver");
            maybeDriver.quit();
        }
    }
}
