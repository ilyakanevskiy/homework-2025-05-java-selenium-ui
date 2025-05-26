package dev.ilyakanevskiy.tests;

import dev.ilyakanevskiy.config.TestData;
import dev.ilyakanevskiy.config.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.util.List;

@ResourceLock(BaseTest.ADMIN_ACCOUNT)
public class ConcludeCourseByProgressTest extends BaseTest {

    @Test
    @DisplayName("Conclude course by progress")
    public void learnerCompletesCourseByPassingAllMedia() {
        userSteps.loginAsLearner(User.LEARNER1);
        userSteps.openCatalogue();
        userSteps.selectCourse(TestData.COURSE_DEVOPS_SEARCH_PHRASE, TestData.COURSE_DEVOPS_FULLNAME);
        userSteps.enrolInCourse();
        userSteps.checkCourseRoom(TestData.COURSE_DEVOPS_FULLNAME, List.of(TestData.COURSE_DEVOPS_MEDIA_1, TestData.COURSE_DEVOPS_MEDIA_2));
        userSteps.passMedia(TestData.COURSE_DEVOPS_MEDIA_1);
        userSteps.checkCourseProgress(TestData.COURSE_HALF_PROGRESS);
        userSteps.passMedia(TestData.COURSE_DEVOPS_MEDIA_2);
        userSteps.checkCourseIsPassed(TestData.COURSE_PASSED_STATUS, TestData.COURSE_FULL_PROGRESS);
        userSteps.logout();

        userSteps.loginAsAdmin(User.ADMIN);
        userSteps.removeParticipantFromCourse(TestData.COURSE_DEVOPS_SEARCH_PHRASE, TestData.COURSE_DEVOPS_FULLNAME, User.LEARNER1);
        userSteps.logout();
    }

    @Override
    protected void restoreInitialState() {
        userSteps.removeParticipantFromCourseIfPresent(User.ADMIN, User.LEARNER1, TestData.COURSE_DEVOPS_SEARCH_PHRASE, TestData.COURSE_DEVOPS_FULLNAME);
    }
}
