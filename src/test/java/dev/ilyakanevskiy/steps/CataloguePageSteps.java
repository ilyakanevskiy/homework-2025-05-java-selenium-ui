package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.CataloguePage;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CataloguePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(CataloguePageSteps.class);
    private final CataloguePage cataloguePage;

    public CataloguePageSteps(CataloguePage cataloguePage) {
        this.cataloguePage = cataloguePage;
    }

    public void checkLoadingIsComplete() {
        logger.info(CheckLoadingIsComplete.DESCRIPTION);
        Assertions.assertTrue(cataloguePage.isLoadingComplete(), CheckLoadingIsComplete.ASSERTION);
    }

    public void checkControls() {
        logger.info(CheckControls.DESCRIPTION);
        Assertions.assertTrue(cataloguePage.areControlsAvailable(), CheckControls.ASSERTION);
    }

    public void searchForCourse(String searchPhrase) {
        logger.info(SearchForCourse.DESCRIPTION, searchPhrase);
        cataloguePage.searchForCourse(searchPhrase);
    }

    public void checkSearchResult(String expectedCourseName) {
        logger.info(CheckSearchResult.DESCRIPTION, expectedCourseName);
        Assertions.assertTrue(cataloguePage.checkSearchResult(expectedCourseName), CheckSearchResult.ASSERTION);
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
        private static final String DESCRIPTION = "Attempting to search for course with phrase: {}";
    }

    private static class CheckSearchResult {
        private static final String DESCRIPTION = "Verifying catalogue contains course with name: {}";
        private static final String ASSERTION = "Course is not found.";
    }
}
