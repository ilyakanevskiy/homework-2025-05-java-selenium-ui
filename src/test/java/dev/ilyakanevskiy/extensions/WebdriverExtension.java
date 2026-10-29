package dev.ilyakanevskiy.extensions;

import dev.ilyakanevskiy.config.ConfigReader;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class WebdriverExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {
    public static final String WEBDRIVER = "webdriver";
    static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(WebdriverExtension.class);
    private static final Logger logger = LoggerFactory.getLogger(WebdriverExtension.class);
    private static final Dimension HEADLESS_WINDOW_SIZE = new Dimension(1920, 1080);

    public static WebDriver getWebDriver(ExtensionContext context) {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        return store.get(WEBDRIVER, WebDriver.class);
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext context) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == WebDriver.class;
    }

    @Override
    public WebDriver resolveParameter(final ParameterContext parameterContext, final ExtensionContext context) throws ParameterResolutionException {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        return store.get(WEBDRIVER, WebDriver.class);
    }

    @Override
    public void beforeEach(final ExtensionContext context) {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);

        String browser = ConfigReader.getBrowser();
        boolean headless = ConfigReader.isHeadless();
        logger.info("Setting up WebDriver for browser: {}, headless: {}", browser, headless);

        WebDriver driver;
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless=new");
            }
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            driver = new FirefoxDriver(options);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        // Stored first: afterEach quits the browser even if resizing the window fails.
        store.put(WEBDRIVER, driver);

        if (headless) {
            driver.manage().window().setSize(HEADLESS_WINDOW_SIZE);
        } else {
            driver.manage().window().maximize();
        }
    }

    @Override
    public void afterEach(final ExtensionContext context) {
        final WebDriver driver = getWebDriver(context);
        if (Objects.nonNull(driver)) {
            logger.info("Closing WebDriver");
            driver.quit();
        }
    }
}
