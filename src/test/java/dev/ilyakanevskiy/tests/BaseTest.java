package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.extensions.AutoScreenshotExtension;
import dev.ilyakanevskiy.extensions.WebdriverExtension;
import dev.ilyakanevskiy.steps.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith({WebdriverExtension.class, AutoScreenshotExtension.class})
public class BaseTest {
    protected UserSteps userSteps;

    @BeforeEach
    public void setup(WebDriver driver) {
        driver.manage().window().maximize();
        userSteps = new UserSteps(driver);
        userSteps.openStartPage();
    }
}
