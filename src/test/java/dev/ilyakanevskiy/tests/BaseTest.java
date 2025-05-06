package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.extensions.WebdriverExtension;
import dev.ilyakanevskiy.pages.CataloguePage;
import dev.ilyakanevskiy.pages.HomePage;
import dev.ilyakanevskiy.pages.StartPage;
import dev.ilyakanevskiy.steps.CataloguePageSteps;
import dev.ilyakanevskiy.steps.HomePageSteps;
import dev.ilyakanevskiy.steps.StartPageSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(WebdriverExtension.class)
public abstract class BaseTest {
    protected StartPageSteps startPageSteps;
    protected HomePageSteps homePageSteps;
    protected CataloguePageSteps cataloguePageSteps;

    @BeforeEach
    void setup(WebDriver driver) {
        driver.manage().window().maximize();
        startPageSteps = new StartPageSteps(new StartPage(driver));
        homePageSteps = new HomePageSteps(new HomePage(driver));
        cataloguePageSteps = new CataloguePageSteps(new CataloguePage(driver));
        startPageSteps.open();
        startPageSteps.checkStartPage();
    }
}
