package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.function.Supplier;

import static dev.ilyakanevskiy.utils.SeleniumHelper.inFrames;
import static dev.ilyakanevskiy.utils.SeleniumHelper.waitFor;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class ParticipantAdministrationPage extends ManagerPage {
    private static final String MANAGER_FRAME_CSS = "iframe[name^='iframe_participants']";
    private static final String PARTICIPANT_LIST_FRAME_CSS = "iframe[name^='c_participants_booked_person']";
    private static final String CANCELLATION_LIST_FRAME_CSS = "iframe[name^='c_participants_cancelled_person']";
    private static final String TAB_CSS = ".x-tab";
    private static final String CANCELLATION_LIST_TAB_TEXT = "Cancellation list";
    private static final String PARTICIPANT_MENU_BUTTON_ID = "tbb_participant";
    private static final String CANCEL_MENU_ITEM_ID = "tbi_cancelParticipant";
    private static final String REMOVE_MENU_ITEM_ID = "tbi_removeParticipant";
    private static final String CONFIRM_CANCELLATION_BUTTON_TEXT = "Confirm cancellation";
    private static final String SAVE_AND_CLOSE_BUTTON_ID = "default_saveClose";

    private String courseManagerWindow;

    public ParticipantAdministrationPage(WebDriver driver) {
        super(driver, MANAGER_FRAME_CSS);
    }

    private <T> T inList(ParticipantList list, Supplier<T> action) {
        return inFrames(driver, action, CONTENT_FRAME, By.cssSelector(MANAGER_FRAME_CSS), By.cssSelector(list.frameCss));
    }

    private void openParticipantMenuItem(String login, String menuItemId) {
        selectRow(login, "Row of participant '" + login + "'");
        openMenuItem(PARTICIPANT_MENU_BUTTON_ID, menuItemId);
    }

    public void switchToWindow() {
        courseManagerWindow = driver.getWindowHandle();
        waitFor(driver).until(numberOfWindowsToBe(2));
        driver.getWindowHandles().stream()
                .filter(handle -> !handle.equals(courseManagerWindow))
                .findFirst()
                .ifPresent(handle -> driver.switchTo().window(handle));
    }

    public boolean isLoadingComplete(ParticipantList list) {
        try {
            return inList(list, this::waitForSearchControls);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void searchParticipant(ParticipantList list, String login) {
        inList(list, () -> {
            searchUntilFiltered(login, rows -> allRowsContain(rows, login));
            return null;
        });
    }

    public boolean isParticipantListed(ParticipantList list, String login) {
        return inList(list, () -> findRow(login).isPresent());
    }

    public boolean isParticipantAbsent(ParticipantList list, String login) {
        try {
            return inList(list, () -> waitForGrid().until(driver -> findRow(login).isEmpty()));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void cancelParticipant(String login) {
        inList(ParticipantList.PARTICIPANTS, () -> {
            openParticipantMenuItem(login, CANCEL_MENU_ITEM_ID);
            confirmDialog(CONFIRM_CANCELLATION_BUTTON_TEXT);
            return null;
        });
    }

    public void openCancellationList() {
        inManagerFrame(() -> {
            WebElement tab = waitFor(driver).until(driver -> driver.findElements(By.cssSelector(TAB_CSS)).stream()
                    .filter(element -> element.isDisplayed() && CANCELLATION_LIST_TAB_TEXT.equals(element.getText().trim()))
                    .findFirst()
                    .orElse(null));
            tab.click();
            return null;
        });
    }

    public void removeParticipant(String login) {
        inList(ParticipantList.CANCELLATIONS, () -> {
            openParticipantMenuItem(login, REMOVE_MENU_ITEM_ID);
            return null;
        });
    }

    public void saveAndClose() {
        try {
            inManagerFrame(() -> {
                driver.findElement(By.id(SAVE_AND_CLOSE_BUTTON_ID)).click();
                return null;
            });
        } catch (NoSuchWindowException e) {
            // 'Save and close' closes the window, possibly before the frame is left.
        }
        waitFor(driver).until(numberOfWindowsToBe(1));
        driver.switchTo().window(courseManagerWindow);
    }

    public enum ParticipantList {
        PARTICIPANTS(PARTICIPANT_LIST_FRAME_CSS),
        CANCELLATIONS(CANCELLATION_LIST_FRAME_CSS);

        private final String frameCss;

        ParticipantList(String frameCss) {
            this.frameCss = frameCss;
        }
    }
}
