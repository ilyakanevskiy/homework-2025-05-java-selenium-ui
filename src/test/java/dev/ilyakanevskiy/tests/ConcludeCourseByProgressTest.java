package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.TestData;
import dev.ilyakanevskiy.config.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConcludeCourseByProgressTest extends BaseTest {

    @Test
    @DisplayName("Conclude course by progress")
    public void learnerCompletesCourseByPassingAllMedia() {
        userSteps.loginAsLearner(User.LEARNER1);
        userSteps.openCatalogue();
        userSteps.selectCourse(TestData.COURSE_DEVOPS_SEARCH_PHRASE, TestData.COURSE_DEVOPS_FULLNAME);
    }

    @AfterEach
    public void teardown() {
        if (userSteps != null && userSteps.isLoggedIn()) {
            userSteps.logout();
        }
    }
}
