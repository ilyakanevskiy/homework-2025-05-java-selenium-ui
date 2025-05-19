package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.TestData;
import dev.ilyakanevskiy.config.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddToWishlistTest extends BaseTest {

    @BeforeEach
    void restoreInitialState() {
        assumeInitialStateRestored(this::removeCourseFromWishlistIfPresent);
    }

    @Test
    @DisplayName("Add to wishlist")
    public void learnerAddsCourseToWishlistAndRemovesIt() {
        userSteps.loginAsLearner(User.LEARNER2);
        userSteps.openCatalogue();
        userSteps.selectCourse(TestData.COURSE_SD_SEARCH_PHRASE, TestData.COURSE_SD_FULLNAME);
        userSteps.addCourseToWishlist();
        userSteps.openLearningStatus();
        userSteps.openCourseFromWishlist(TestData.COURSE_SD_FULLNAME);
        userSteps.removeCourseFromWishlist();
        userSteps.logout();
    }

    @AfterEach
    public void teardown() {
        if (userSteps != null) {
            userSteps.startNewSession();
            removeCourseFromWishlistIfPresent();
        }
    }

    private void removeCourseFromWishlistIfPresent() {
        userSteps.removeCourseFromWishlistIfPresent(User.LEARNER2, TestData.COURSE_SD_SEARCH_PHRASE, TestData.COURSE_SD_FULLNAME);
    }
}
