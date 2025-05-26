package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.CourseManagerPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static java.text.MessageFormat.format;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseManagerPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(CourseManagerPageSteps.class);
    private final CourseManagerPage courseManagerPage;

    public CourseManagerPageSteps(CourseManagerPage courseManagerPage) {
        this.courseManagerPage = courseManagerPage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(courseManagerPage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(SearchCourse.DESCRIPTION)
    public void searchCourse(String searchPhrase, String courseName) {
        logger.info(adaptPlaceholder(SearchCourse.DESCRIPTION), searchPhrase);
        courseManagerPage.searchCourse(searchPhrase, courseName);
    }

    @Step(CheckCourseIsListed.DESCRIPTION)
    public void checkCourseIsListed(String courseName) {
        logger.info(adaptPlaceholder(CheckCourseIsListed.DESCRIPTION), courseName);
        assertThat(courseManagerPage.isCourseListed(courseName)).as(format(CheckCourseIsListed.ASSERTION, courseName)).isTrue();
    }

    @Step(SelectCourse.DESCRIPTION)
    public void selectCourse(String courseName) {
        logger.info(adaptPlaceholder(SelectCourse.DESCRIPTION), courseName);
        courseManagerPage.selectCourse(courseName);
    }

    @Step(OpenParticipantAdministration.DESCRIPTION)
    public void openParticipantAdministration() {
        logger.info(adaptPlaceholder(OpenParticipantAdministration.DESCRIPTION));
        courseManagerPage.openParticipantAdministration();
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying course manager";
        private static final String ASSERTION = "Course manager with the search should be available.";
    }

    private static class SearchCourse {
        private static final String DESCRIPTION = "Attempting to search for course with phrase: {0}";
    }

    private static class CheckCourseIsListed {
        private static final String DESCRIPTION = "Verifying course manager lists course: {0}";
        private static final String ASSERTION = "Course: {0} should be listed in the search result.";
    }

    private static class SelectCourse {
        private static final String DESCRIPTION = "Attempting to select course: {0}";
    }

    private static class OpenParticipantAdministration {
        private static final String DESCRIPTION = "Attempting to open participant administration";
    }
}
