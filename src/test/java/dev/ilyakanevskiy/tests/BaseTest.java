package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.ConfigReader;
import dev.ilyakanevskiy.extensions.WebdriverExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(WebdriverExtension.class)
public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    void setup(WebDriver driver) {
        this.driver = driver;
        driver.manage().window().maximize();
        driver.get(ConfigReader.getProperty("base_url"));
    }
}
