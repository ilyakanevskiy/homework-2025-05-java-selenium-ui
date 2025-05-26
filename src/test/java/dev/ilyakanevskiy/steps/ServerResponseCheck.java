package dev.ilyakanevskiy.steps;

import static org.assertj.core.api.Assertions.assertThat;

final class ServerResponseCheck {
    private static final String ASSERTION = "Server should respond without an error at URL: %s";
    private static final String SERVER_ERROR_TITLE = "(5\\d\\d|404) .*";

    private ServerResponseCheck() {
    }

    static void checkServerResponse(String pageTitle, String url) {
        assertThat(pageTitle).as(ASSERTION, url).doesNotMatch(SERVER_ERROR_TITLE);
    }
}
