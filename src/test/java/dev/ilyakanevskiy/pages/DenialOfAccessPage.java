package dev.ilyakanevskiy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

public class DenialOfAccessPage extends ManagerPage {
    private static final String MANAGER_FRAME_CSS = "iframe[name^='iframe_navi:']";
    private static final String CELL_CSS = "td";
    private static final int LOGIN_ATTEMPTS_CELL_INDEX = 5;
    private static final String RESET_MENU_BUTTON_ID = "tbb_reset";
    private static final String RESET_SELECTED_MENU_ITEM_ID = "tbi_reset";
    private static final String RESET_DIALOG_BUTTON_TEXT = "Reset";

    public DenialOfAccessPage(WebDriver driver) {
        super(driver, MANAGER_FRAME_CSS);
    }

    private static String getCellText(WebElement row, int index) {
        List<WebElement> cells = row.findElements(By.cssSelector(CELL_CSS));
        return cells.size() > index ? cells.get(index).getText().trim() : "";
    }

    public boolean isLoadingComplete() {
        try {
            return inManagerFrame(this::waitForSearchControls);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void searchUser(String login) {
        inManagerFrame(() -> {
            searchUntilFiltered(login, rows -> allRowsContain(rows, login));
            return null;
        });
    }

    public boolean isUserListed(String login) {
        return inManagerFrame(() -> findRow(login).isPresent());
    }

    public Optional<Integer> getFailedLoginAttempts(String login) {
        return inManagerFrame(() -> {
            try {
                String attempts = waitForGrid().until(driver -> findRow(login)
                        .map(row -> getCellText(row, LOGIN_ATTEMPTS_CELL_INDEX))
                        .orElse(null));
                return Optional.of(Integer.parseInt(attempts));
            } catch (TimeoutException | NumberFormatException e) {
                return Optional.empty();
            }
        });
    }

    public boolean isUserAbsent(String login) {
        return inManagerFrame(() -> {
            try {
                return waitForGrid().until(driver -> findRow(login).isEmpty());
            } catch (TimeoutException e) {
                return false;
            }
        });
    }

    public void resetFailedLoginAttempts(String login) {
        inManagerFrame(() -> {
            selectRow(login, "Row of user '" + login + "'");
            openMenuItem(RESET_MENU_BUTTON_ID, RESET_SELECTED_MENU_ITEM_ID);
            confirmDialog(RESET_DIALOG_BUTTON_TEXT);
            return null;
        });
    }
}
