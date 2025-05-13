package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.TestData;
import dev.ilyakanevskiy.config.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddToWishlistTest extends BaseTest {

    @Test
    @DisplayName("Add to wishlist")
    public void learnerAddsCourseToWishlistAndRemovesIt() {
        userSteps.loginAsLearner(User.LEARNER2);
        userSteps.openCatalogue();
        userSteps.selectCourse(TestData.COURSE_SD_SEARCH_PHRASE, TestData.COURSE_SD_FULLNAME);
    }

    @AfterEach
    public void teardown() {
        if (userSteps != null && userSteps.isLoggedIn()) {
            userSteps.logout();
        }
    }
}
