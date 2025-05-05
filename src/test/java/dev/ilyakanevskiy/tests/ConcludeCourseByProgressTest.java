package dev.ilyakanevskiy.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConcludeCourseByProgressTest extends BaseTest {

    @Test
    @DisplayName("Conclude course by progress")
    public void learnerCompletesCourseByPassingAllMedia() {
        startPageSteps.checkStartPage();
    }
}
