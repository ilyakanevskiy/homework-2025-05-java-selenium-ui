package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.LearningStatusPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static java.text.MessageFormat.format;
import static org.assertj.core.api.Assertions.assertThat;

public class LearningStatusPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(LearningStatusPageSteps.class);
    private final LearningStatusPage learningStatusPage;

    public LearningStatusPageSteps(LearningStatusPage learningStatusPage) {
        this.learningStatusPage = learningStatusPage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(learningStatusPage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(CheckTabs.DESCRIPTION)
    public void checkTabs() {
        logger.info(adaptPlaceholder(CheckTabs.DESCRIPTION));
        assertThat(learningStatusPage.getUnavailableTabs()).as(CheckTabs.ASSERTION).isEmpty();
    }

    @Step(OpenWishlist.DESCRIPTION)
    public void openWishlist() {
        logger.info(adaptPlaceholder(OpenWishlist.DESCRIPTION));
        learningStatusPage.openWishlist();
    }

    @Step(CheckCourseIsListed.DESCRIPTION)
    public void checkCourseIsListed(String courseName) {
        logger.info(adaptPlaceholder(CheckCourseIsListed.DESCRIPTION), courseName);
        assertThat(learningStatusPage.isCourseListed(courseName)).as(format(CheckCourseIsListed.ASSERTION, courseName)).isTrue();
    }

    @Step(OpenCourse.DESCRIPTION)
    public void openCourse(String courseName) {
        logger.info(adaptPlaceholder(OpenCourse.DESCRIPTION), courseName);
        learningStatusPage.openCourse(courseName);
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying learning status page";
        private static final String ASSERTION = "Learning status page should be available.";
    }

    private static class CheckTabs {
        private static final String DESCRIPTION = "Verifying learning status tabs";
        private static final String ASSERTION = "Learning status tabs (current, completed, cancelled and wishlist) should be available.";
    }

    private static class OpenWishlist {
        private static final String DESCRIPTION = "Attempting to open wishlist";
    }

    private static class CheckCourseIsListed {
        private static final String DESCRIPTION = "Verifying wishlist contains course with name: {0}";
        private static final String ASSERTION = "Course with name: {0} should be listed in the wishlist.";
    }

    private static class OpenCourse {
        private static final String DESCRIPTION = "Attempting to open course from wishlist: {0}";
    }
}
