package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.CataloguePage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static java.text.MessageFormat.format;
import static org.assertj.core.api.Assertions.assertThat;

public class CataloguePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(CataloguePageSteps.class);
    private final CataloguePage cataloguePage;

    public CataloguePageSteps(CataloguePage cataloguePage) {
        this.cataloguePage = cataloguePage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(cataloguePage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(CheckControls.DESCRIPTION)
    public void checkControls() {
        logger.info(adaptPlaceholder(CheckControls.DESCRIPTION));
        assertThat(cataloguePage.getUnavailableControls()).as(CheckControls.ASSERTION).isEmpty();
    }

    @Step(SearchForCourse.DESCRIPTION)
    public void searchForCourse(String searchPhrase) {
        logger.info(adaptPlaceholder(SearchForCourse.DESCRIPTION), searchPhrase);
        cataloguePage.searchForCourse(searchPhrase);
    }

    @Step(CheckSearchResult.DESCRIPTION)
    public void checkSearchResult(String expectedCourseName) {
        logger.info(adaptPlaceholder(CheckSearchResult.DESCRIPTION), expectedCourseName);
        assertThat(cataloguePage.checkSearchResult(expectedCourseName)).as(format(CheckSearchResult.ASSERTION, expectedCourseName)).isTrue();
    }

    @Step(GoToCourse.DESCRIPTION)
    public void goToCourse(String courseName) {
        logger.info(adaptPlaceholder(GoToCourse.DESCRIPTION), courseName);
        cataloguePage.goToCourse(courseName);
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying catalogue content";
        private static final String ASSERTION = "Catalogue content should be available.";
    }

    private static class CheckControls {
        private static final String DESCRIPTION = "Verifying search controls";
        private static final String ASSERTION = "Search controls (input and button) should be available.";
    }

    private static class SearchForCourse {
        private static final String DESCRIPTION = "Attempting to search for course with phrase: {0}";
    }

    private static class CheckSearchResult {
        private static final String DESCRIPTION = "Verifying catalogue contains course with name: {0}";
        private static final String ASSERTION = "Course with name: {0} should be available.";
    }

    private static class GoToCourse {
        private static final String DESCRIPTION = "Attempting to go to course: {0}";
    }
}
