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

    @Step(IsInWishList.DESCRIPTION)
    public boolean isInWishList() {
        logger.info(adaptPlaceholder(IsInWishList.DESCRIPTION));
        return coursePage.isInWishList();
    }

    @Step(AddToWishList.DESCRIPTION)
    public void addToWishList() {
        logger.info(adaptPlaceholder(AddToWishList.DESCRIPTION));
        coursePage.addToWishList();
    }

    @Step(CheckRemoveFromWishList.DESCRIPTION)
    public void checkRemoveFromWishListIsAvailable() {
        logger.info(adaptPlaceholder(CheckRemoveFromWishList.DESCRIPTION));
        assertThat(coursePage.isRemoveFromWishListAvailable()).as(CheckRemoveFromWishList.ASSERTION).isTrue();
    }

    @Step(RemoveFromWishList.DESCRIPTION)
    public void removeFromWishList() {
        logger.info(adaptPlaceholder(RemoveFromWishList.DESCRIPTION));
        coursePage.removeFromWishList();
    }

    @Step(CheckAddToWishList.DESCRIPTION)
    public void checkAddToWishListIsAvailable() {
        logger.info(adaptPlaceholder(CheckAddToWishList.DESCRIPTION));
        assertThat(coursePage.isAddToWishListAvailable()).as(CheckAddToWishList.ASSERTION).isTrue();
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

    private static class IsInWishList {
        private static final String DESCRIPTION = "Checking whether course is in wish list";
    }

    private static class AddToWishList {
        private static final String DESCRIPTION = "Attempting to add course to wish list";
    }

    private static class CheckRemoveFromWishList {
        private static final String DESCRIPTION = "Verifying 'Remove from wish list' button";
        private static final String ASSERTION = "'Remove from wish list' button should be available for a course in the wish list.";
    }

    private static class RemoveFromWishList {
        private static final String DESCRIPTION = "Attempting to remove course from wish list";
    }

    private static class CheckAddToWishList {
        private static final String DESCRIPTION = "Verifying 'Add to wish list' button";
        private static final String ASSERTION = "'Add to wish list' button should be available for a course not in the wish list.";
    }
}
