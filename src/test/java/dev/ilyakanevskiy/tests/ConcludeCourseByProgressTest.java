package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.TestData;
import dev.ilyakanevskiy.config.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConcludeCourseByProgressTest extends BaseTest {

    @Test
    @DisplayName("Conclude course by progress")
    public void learnerCompletesCourseByPassingAllMedia() {
        startPageSteps.performLogin(User.LEARNER1);
        homePageSteps.checkLoadingIsComplete();
        homePageSteps.checkNavigationPanel();
        homePageSteps.checkLearnerControls();

        homePageSteps.goToCatalogue();
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkControls();

        cataloguePageSteps.searchForCourse(TestData.COURSE_DEVOPS_SEARCH_PHRASE);
        cataloguePageSteps.checkLoadingIsComplete();
        cataloguePageSteps.checkSearchResult(TestData.COURSE_DEVOPS_FULLNAME);

        homePageSteps.performLogout();
        startPageSteps.checkStartPage();
    }
}
