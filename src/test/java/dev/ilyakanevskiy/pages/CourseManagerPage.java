package dev.ilyakanevskiy.pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class CourseManagerPage extends ManagerPage {
    private static final String MANAGER_FRAME_CSS = "iframe[name^='iframe_navi:']";
    private static final String PARTICIPANTS_MENU_BUTTON_ID = "tbb_participants";
    private static final String PARTICIPANT_ADMINISTRATION_MENU_ITEM_ID = "tbi_participants";

    public CourseManagerPage(WebDriver driver) {
        super(driver, MANAGER_FRAME_CSS);
    }

    public boolean isLoadingComplete() {
        try {
            return inManagerFrame(this::waitForSearchControls);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void searchCourse(String searchPhrase, String courseName) {
        inManagerFrame(() -> {
            searchUntilFiltered(searchPhrase, rows -> !rows.isEmpty() && allRowsContain(rows, courseName));
            return null;
        });
    }

    public boolean isCourseListed(String courseName) {
        return inManagerFrame(() -> findRow(courseName).isPresent());
    }

    public void selectCourse(String courseName) {
        inManagerFrame(() -> {
            selectRow(courseName, "Row of course '" + courseName + "'");
            return null;
        });
    }

    public void openParticipantAdministration() {
        inManagerFrame(() -> {
            openMenuItem(PARTICIPANTS_MENU_BUTTON_ID, PARTICIPANT_ADMINISTRATION_MENU_ITEM_ID);
            return null;
        });
    }
}
