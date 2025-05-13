package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.CoursePage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static org.assertj.core.api.Assertions.assertThat;

public class CoursePageSteps {
    private static final Logger logger = LoggerFactory.getLogger(CoursePageSteps.class);
    private final CoursePage coursePage;

    public CoursePageSteps(CoursePage coursePage) {
        this.coursePage = coursePage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(coursePage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(CheckProperties.DESCRIPTION)
    public void checkProperties(String expectedCourseName) {
        logger.info(adaptPlaceholder(CheckProperties.DESCRIPTION));
        assertThat(coursePage.getCourseName()).as(CheckProperties.NAME_ASSERTION).hasValue(expectedCourseName);
        assertThat(coursePage.getUnavailableProperties()).as(CheckProperties.ASSERTION).isEmpty();
    }

    @Step(CheckBreadcrumbs.DESCRIPTION)
    public void checkBreadcrumbs() {
        logger.info(adaptPlaceholder(CheckBreadcrumbs.DESCRIPTION));
        assertThat(coursePage.areBreadcrumbsAvailable()).as(CheckBreadcrumbs.ASSERTION).isTrue();
    }

    @Step(CheckControls.DESCRIPTION)
    public void checkControls() {
        logger.info(adaptPlaceholder(CheckControls.DESCRIPTION));
        assertThat(coursePage.getUnavailableControls()).as(CheckControls.ASSERTION).isEmpty();
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying course content";
        private static final String ASSERTION = "Course content should be available.";
    }

    private static class CheckProperties {
        private static final String DESCRIPTION = "Verifying course properties";
        private static final String NAME_ASSERTION = "Course name should be displayed.";
        private static final String ASSERTION = "Course properties (image and type) should be available.";
    }

    private static class CheckBreadcrumbs {
        private static final String DESCRIPTION = "Verifying breadcrumbs";
        private static final String ASSERTION = "Breadcrumbs should be available.";
    }

    private static class CheckControls {
        private static final String DESCRIPTION = "Verifying course controls";
        private static final String ASSERTION = "Course controls ('Enrol' and 'Add to wish list' buttons) should be available.";
    }
}
