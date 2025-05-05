package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.extensions.WebdriverExtension;
import dev.ilyakanevskiy.pages.StartPage;
import dev.ilyakanevskiy.steps.StartPageSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(WebdriverExtension.class)
public abstract class BaseTest {
    protected StartPageSteps startPageSteps;

    @BeforeEach
    void setup(WebDriver driver) {
        driver.manage().window().maximize();
        startPageSteps = new StartPageSteps(new StartPage(driver));
        startPageSteps.open();
    }
}
