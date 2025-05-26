package dev.ilyakanevskiy.steps;

import dev.ilyakanevskiy.pages.ParticipantAdministrationPage;
import dev.ilyakanevskiy.pages.ParticipantAdministrationPage.ParticipantList;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ilyakanevskiy.utils.PlaceholderHelper.adaptPlaceholder;
import static java.text.MessageFormat.format;
import static org.assertj.core.api.Assertions.assertThat;

public class ParticipantAdministrationPageSteps {
    private static final Logger logger = LoggerFactory.getLogger(ParticipantAdministrationPageSteps.class);
    private final ParticipantAdministrationPage participantAdministrationPage;

    public ParticipantAdministrationPageSteps(ParticipantAdministrationPage participantAdministrationPage) {
        this.participantAdministrationPage = participantAdministrationPage;
    }

    @Step(SwitchToWindow.DESCRIPTION)
    public void switchToWindow() {
        logger.info(adaptPlaceholder(SwitchToWindow.DESCRIPTION));
        participantAdministrationPage.switchToWindow();
    }

    @Step(CheckLoadingIsComplete.DESCRIPTION)
    public void checkLoadingIsComplete(ParticipantList list) {
        logger.info(adaptPlaceholder(CheckLoadingIsComplete.DESCRIPTION), list);
        assertThat(participantAdministrationPage.isLoadingComplete(list)).as(CheckLoadingIsComplete.ASSERTION).isTrue();
    }

    @Step(SearchParticipant.DESCRIPTION)
    public void searchParticipant(String login, ParticipantList list) {
        logger.info(adaptPlaceholder(SearchParticipant.DESCRIPTION), login, list);
        participantAdministrationPage.searchParticipant(list, login);
    }

    @Step(IsParticipantListed.DESCRIPTION)
    public boolean isParticipantListed(String login, ParticipantList list) {
        logger.info(adaptPlaceholder(IsParticipantListed.DESCRIPTION), login, list);
        return participantAdministrationPage.isParticipantListed(list, login);
    }

    @Step(CheckParticipantIsListed.DESCRIPTION)
    public void checkParticipantIsListed(String login, ParticipantList list) {
        logger.info(adaptPlaceholder(CheckParticipantIsListed.DESCRIPTION), login, list);
        assertThat(participantAdministrationPage.isParticipantListed(list, login)).as(format(CheckParticipantIsListed.ASSERTION, login, list)).isTrue();
    }

    @Step(CheckParticipantIsNotListed.DESCRIPTION)
    public void checkParticipantIsNotListed(String login, ParticipantList list) {
        logger.info(adaptPlaceholder(CheckParticipantIsNotListed.DESCRIPTION), login, list);
        assertThat(participantAdministrationPage.isParticipantAbsent(list, login)).as(format(CheckParticipantIsNotListed.ASSERTION, login, list)).isTrue();
    }

    @Step(CancelParticipant.DESCRIPTION)
    public void cancelParticipant(String login) {
        logger.info(adaptPlaceholder(CancelParticipant.DESCRIPTION), login);
        participantAdministrationPage.cancelParticipant(login);
    }

    @Step(OpenCancellationList.DESCRIPTION)
    public void openCancellationList() {
        logger.info(adaptPlaceholder(OpenCancellationList.DESCRIPTION));
        participantAdministrationPage.openCancellationList();
    }

    @Step(RemoveParticipant.DESCRIPTION)
    public void removeParticipant(String login) {
        logger.info(adaptPlaceholder(RemoveParticipant.DESCRIPTION), login);
        participantAdministrationPage.removeParticipant(login);
    }

    @Step(SaveAndClose.DESCRIPTION)
    public void saveAndClose() {
        logger.info(adaptPlaceholder(SaveAndClose.DESCRIPTION));
        participantAdministrationPage.saveAndClose();
    }

    private static class SwitchToWindow {
        private static final String DESCRIPTION = "Attempting to switch to participant administration window";
    }

    private static class CheckLoadingIsComplete {
        private static final String DESCRIPTION = "Verifying participant administration list: {0}";
        private static final String ASSERTION = "Participant list with the search should be available.";
    }

    private static class SearchParticipant {
        private static final String DESCRIPTION = "Attempting to search for participant: {0} in list: {1}";
    }

    private static class IsParticipantListed {
        private static final String DESCRIPTION = "Checking whether participant: {0} is listed in list: {1}";
    }

    private static class CheckParticipantIsListed {
        private static final String DESCRIPTION = "Verifying participant: {0} is listed in list: {1}";
        private static final String ASSERTION = "Participant: {0} should be listed in list: {1}.";
    }

    private static class CheckParticipantIsNotListed {
        private static final String DESCRIPTION = "Verifying participant: {0} is no longer listed in list: {1}";
        private static final String ASSERTION = "Participant: {0} should not be listed in list: {1}.";
    }

    private static class CancelParticipant {
        private static final String DESCRIPTION = "Attempting to cancel participant: {0}";
    }

    private static class OpenCancellationList {
        private static final String DESCRIPTION = "Attempting to open cancellation list";
    }

    private static class RemoveParticipant {
        private static final String DESCRIPTION = "Attempting to remove participant: {0}";
    }

    private static class SaveAndClose {
        private static final String DESCRIPTION = "Attempting to save and close participant administration";
    }
}
