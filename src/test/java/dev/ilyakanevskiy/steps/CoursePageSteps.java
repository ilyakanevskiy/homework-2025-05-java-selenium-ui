package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.CoursePage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;

public class CoursePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(CoursePageSteps.class);
    private final CoursePage coursePage;

    public CoursePageSteps(CoursePage coursePage) {
        this.coursePage = coursePage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        Assertions.assertTrue(coursePage.isLoadingComplete(), CheckLoadingIsComplete.ASSERTION);
    }

    @Step(CheckProperties.DESCRIPTION)
    public void checkProperties(String expectedCourseName) {
        logger.info(adaptPlaceholder(CheckProperties.DESCRIPTION));
        Assertions.assertTrue(coursePage.arePropertiesAvailable(expectedCourseName), CheckProperties.ASSERTION);
    }

    @Step(CheckBreadcrumbs.DESCRIPTION)
    public void checkBreadcrumbs() {
        logger.info(adaptPlaceholder(CheckBreadcrumbs.DESCRIPTION));
        Assertions.assertTrue(coursePage.areBreadcrumbsAvailable(), CheckBreadcrumbs.ASSERTION);
    }

    @Step(CheckControls.DESCRIPTION)
    public void checkControls() {
        logger.info(adaptPlaceholder(CheckControls.DESCRIPTION));
        Assertions.assertTrue(coursePage.areControlsAvailable(), CheckControls.ASSERTION);
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying course content";
        private static final String ASSERTION = "Course content is not available.";
    }

    private static class CheckProperties {
        private static final String DESCRIPTION = "Verifying course properties";
        private static final String ASSERTION = "Course properties (name, image and type) are not available.";
    }

    private static class CheckBreadcrumbs {
        private static final String DESCRIPTION = "Verifying breadcrumbs";
        private static final String ASSERTION = "Breadcrumbs are not available.";
    }

    private static class CheckControls {
        private static final String DESCRIPTION = "Verifying course controls";
        private static final String ASSERTION = "Course controls ('Enrol' and 'Add to wish list' buttons) are not available.";
    }
}
