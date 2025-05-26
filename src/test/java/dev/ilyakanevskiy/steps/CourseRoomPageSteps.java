package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.CourseRoomPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static dev.ilyakanevskiy.steps.ServerResponseCheck.checkServerResponse;
import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static java.text.MessageFormat.format;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseRoomPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(CourseRoomPageSteps.class);
    private final CourseRoomPage courseRoomPage;

    public CourseRoomPageSteps(CourseRoomPage courseRoomPage) {
        this.courseRoomPage = courseRoomPage;
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete() {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION));
        assertThat(courseRoomPage.isLoadingComplete()).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(CheckHeader.DESCRIPTION)
    public void checkHeader(String expectedCourseName) {
        logger.info(adaptPlaceholder(CheckHeader.DESCRIPTION));
        assertThat(courseRoomPage.getCourseName()).as(CheckHeader.NAME_ASSERTION).hasValue(expectedCourseName);
        assertThat(courseRoomPage.getUnavailableHeaderElements()).as(CheckHeader.ASSERTION).isEmpty();
    }

    @Step(CheckMedia.DESCRIPTION)
    public void checkMedia(List<String> mediaNames) {
        logger.info(adaptPlaceholder(CheckMedia.DESCRIPTION), mediaNames);
        assertThat(courseRoomPage.getMissingMedia(mediaNames)).as(CheckMedia.ASSERTION).isEmpty();
    }

    @Step(OpenMedia.DESCRIPTION)
    public void openMedia(String mediaName) {
        logger.info(adaptPlaceholder(OpenMedia.DESCRIPTION), mediaName);
        courseRoomPage.openMedia(mediaName);
    }

    @Step(Reopen.DESCRIPTION)
    public void reopen() {
        logger.info(adaptPlaceholder(Reopen.DESCRIPTION));
        courseRoomPage.reopen();
        checkServerResponse(courseRoomPage.getTitle(), courseRoomPage.getUrl());
    }

    @Step(CheckMediaPassed.DESCRIPTION)
    public void checkMediaPassed(String mediaName) {
        logger.info(adaptPlaceholder(CheckMediaPassed.DESCRIPTION), mediaName);
        assertThat(courseRoomPage.isMediaPassed(mediaName)).as(format(CheckMediaPassed.ASSERTION, mediaName)).isTrue();
    }

    @Step(CheckProgress.DESCRIPTION)
    public void checkProgress(String expectedProgress) {
        logger.info(adaptPlaceholder(CheckProgress.DESCRIPTION), expectedProgress);
        assertThat(courseRoomPage.getCourseProgress()).as(CheckProgress.ASSERTION).hasValue(expectedProgress);
    }

    @Step(CheckStatus.DESCRIPTION)
    public void checkStatus(String expectedStatus) {
        logger.info(adaptPlaceholder(CheckStatus.DESCRIPTION), expectedStatus);
        assertThat(courseRoomPage.getCourseStatus()).as(CheckStatus.ASSERTION).hasValue(expectedStatus);
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying course room and syllabus";
        private static final String ASSERTION = "Course room with the syllabus should be available.";
    }

    private static class CheckHeader {
        private static final String DESCRIPTION = "Verifying course room header";
        private static final String NAME_ASSERTION = "Course name should be displayed.";
        private static final String ASSERTION = "Course room header (image, progress bar, status, rating and 'Cancel course' option) should be available.";
    }

    private static class CheckMedia {
        private static final String DESCRIPTION = "Verifying syllabus contains media: {0}";
        private static final String ASSERTION = "All media should be listed in the syllabus.";
    }

    private static class OpenMedia {
        private static final String DESCRIPTION = "Attempting to open media: {0}";
    }

    private static class Reopen {
        private static final String DESCRIPTION = "Attempting to go back to the course room";
    }

    private static class CheckMediaPassed {
        private static final String DESCRIPTION = "Verifying media is passed: {0}";
        private static final String ASSERTION = "Media: {0} should have the passed status.";
    }

    private static class CheckProgress {
        private static final String DESCRIPTION = "Verifying course progress: {0}";
        private static final String ASSERTION = "Course progress should be as expected.";
    }

    private static class CheckStatus {
        private static final String DESCRIPTION = "Verifying course status: {0}";
        private static final String ASSERTION = "Course status should be as expected.";
    }
}
