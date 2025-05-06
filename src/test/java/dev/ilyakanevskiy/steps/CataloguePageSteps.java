package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.CataloguePage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;

public class CataloguePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(CataloguePageSteps.class);
    private final CataloguePage cataloguePage;

    public CataloguePageSteps(CataloguePage cataloguePage) {
        this.cataloguePage = cataloguePage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        Assertions.assertTrue(cataloguePage.isLoadingComplete(), CheckLoadingIsComplete.ASSERTION);
    }

    @Step(CheckControls.DESCRIPTION)
    public void checkControls() {
        logger.info(adaptPlaceholder(CheckControls.DESCRIPTION));
        Assertions.assertTrue(cataloguePage.areControlsAvailable(), CheckControls.ASSERTION);
    }

    @Step(SearchForCourse.DESCRIPTION)
    public void searchForCourse(String searchPhrase) {
        logger.info(adaptPlaceholder(SearchForCourse.DESCRIPTION), searchPhrase);
        cataloguePage.searchForCourse(searchPhrase);
    }

    @Step(CheckSearchResult.DESCRIPTION)
    public void checkSearchResult(String expectedCourseName) {
        logger.info(adaptPlaceholder(CheckSearchResult.DESCRIPTION), expectedCourseName);
        Assertions.assertTrue(cataloguePage.checkSearchResult(expectedCourseName), CheckSearchResult.ASSERTION);
    }

    @Step(GoToCourse.DESCRIPTION)
    public void goToCourse() {
        logger.info(adaptPlaceholder(GoToCourse.DESCRIPTION));
        cataloguePage.goToCourse();
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying catalogue content";
        private static final String ASSERTION = "Catalogue content is not available.";
    }

    private static class CheckControls {
        private static final String DESCRIPTION = "Verifying search controls";
        private static final String ASSERTION = "Search controls (input or button) are not available.";
    }

    private static class SearchForCourse {
        private static final String DESCRIPTION = "Attempting to search for course with phrase: {0}";
    }

    private static class CheckSearchResult {
        private static final String DESCRIPTION = "Verifying catalogue contains course with name: {0}";
        private static final String ASSERTION = "Course with name: {0} is not found.";
    }

    private static class GoToCourse {
        private static final String DESCRIPTION = "Attempting to go to course";
    }
}
