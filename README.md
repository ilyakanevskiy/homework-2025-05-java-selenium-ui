# UI test automation framework for a learning management system

[![Build](https://github.com/ilyakanevskiy/homework-2025-05-java-selenium-ui/actions/workflows/build.yml/badge.svg)](https://github.com/ilyakanevskiy/homework-2025-05-java-selenium-ui/actions/workflows/build.yml)

This is a lightweight test automation framework, built from scratch as a test assignment for a QA Automation Engineer position.

The tests cover the 3 end-to-end UI scenarios of the assignment in a learning management system: course enrolment, the wishlist and the account lock after failed login attempts.

The test environment is not public. Its address and the test accounts are not in this repository (see [Configuration](#configuration)). Without them, the project builds, and the code and its structure are available for review. A test run needs a filled-in `config.local.properties`.

## Stack

- Java 21, Maven (through the Maven Wrapper)
- Selenium WebDriver 4 with the built-in Selenium Manager, which downloads the browser driver
- JUnit 5 with parallel execution
- AssertJ
- Allure Report: steps, assertions and a screenshot on failure
- SLF4J (simple logger)

## Demonstrated concepts

- **Layered tests.** Tests contain only the scenario steps, as in the test case. Step classes contain the assertions and appear in the report. Page objects contain the locators and the actions. Thus a change in a locator affects only one page object.
- **Repeatable tests on shared accounts.** Each test restores the initial state of its accounts before and after the test, and only if necessary. The cleanup after the test deletes the cookies and opens the start page again. Thus it does not use the session of the test, even after a failure in the middle of a scenario. The tests with the shared admin account run sequentially (see [Repeatable tests on shared accounts](#repeatable-tests-on-shared-accounts)).
- **Clear failures instead of Selenium stack traces.** If a checked element is missing, the check fails with a message that names the element. Most actions on a missing element also fail with an error that names the element. Either way, the report has a screenshot.
- **One screenshot for each failure, in the right section of the report.** The screenshot extension takes a screenshot at the moment of the failure. It attaches the screenshot to the section of the report where the failure happened: the setup, the test body or the teardown. Thus the screenshot shows the failed page, not the page after the cleanup. If the screenshot fails, the report still shows the original error.
- **Adaptive waits for dynamic pages.** The catalogue search waits until the old results disappear. The admin managers (Denial of access, courses, course participants) can ignore a search while their tables are still loading, so the test repeats the search until the table shows only the matching rows. These pages redraw their content, so the test ignores stale elements while it waits.
- **Readable and secure report.** Each step and each assertion is a step in the Allure report. Passwords are not in the report (see [Design notes](#design-notes)).
- **Minimum setup.** Only JDK 21 and a browser are necessary. The Maven Wrapper downloads Maven. The Allure Maven plugin downloads the Allure command-line tool. Selenium Manager downloads the browser driver and, if necessary, the browser.
- **Granular commit history.** Each commit makes one logical change, for example a page object together with its steps, and each commit builds. Commit messages follow the [Conventional Commits](https://www.conventionalcommits.org/) format: `<type>: <description>`, without a scope, one line without a body.

## Not included

The assignment asks for a lightweight framework. Thus the project does not include these common parts of test frameworks:

- **Test data setup through an API or the database.** The preparation and the cleanup use the UI (see [Repeatable tests on shared accounts](#repeatable-tests-on-shared-accounts)).
- **A base page class and page components.** Page objects share code through `SeleniumHelper`, not through inheritance. The navigation bar is a part of `HomePage`, not a separate component.
- **Remote browsers** (Selenium Grid). All browsers run on the local machine.
- **A code formatter and a linter** (for example, Spotless and Checkstyle). No tool checks the code style.

## Scenarios

- **Conclude course by progress.** A learner finds a course in the catalogue, enrols and opens the 2 media of the course. The test checks the course room, the "passed" status of each media, the course progress and the course status. Then an admin cancels the learner in the participant administration of the course and removes the learner from the course.
- **Add to wishlist.** A learner adds a course to the wishlist and finds it on the Learning status page. The learner opens the course from the wishlist and removes it from the wishlist.
- **Denial of access.** After 3 failed login attempts, the system blocks a learner. An admin resets the failed attempts in the Denial of access manager. The learner logs in again.

## Requirements

| Requirement | Done | Comment |
|---|---|---|
| Maven project | ✓ | With the Maven Wrapper. |
| Page Object Model | ✓ | One page object for each page, in `pages`. |
| JUnit 5 | ✓ | |
| Configuration file with the base URL and the browser | ✓ | The browser is in `config.properties`. The base URL is in the local configuration (see [Design notes](#design-notes)). |
| WebDriver management | ✓ | Selenium Manager, built into Selenium 4. It downloads the browser driver and, if necessary, the browser. |
| Logging | ✓ | SLF4J. |
| Screenshot on failure | ✓ | Attached to the Allure report, in the section where the failure happened. |
| Test data in the code or in a separate class | ✓ | Separate classes. `TestData` contains constants: search phrases, course names, expected messages. The `User` enum lists the test accounts and reads their credentials from the local configuration. |
| All scenarios automated completely | ✗ | 2 of the 3 scenarios are fully automated. "Conclude course by progress" is automated from the first to the last step, but without the checks on the media page. See [Not done](#not-done). |
| Repeatable tests | ✓ | See [Repeatable tests on shared accounts](#repeatable-tests-on-shared-accounts). |
| Assertions | ✓ | AssertJ replaced the JUnit assertions during the development. Its failure messages show the actual value, for example the list of missing elements. Each assertion appears in the report as a step. |
| Page objects for all UI interactions | ✓ | Only page objects contain locators. Tests and steps do not find elements. |
| README: setup, how to run the tests, assumptions | ✓ | This file. |
| Bonus: browser configuration | ✓ | Chrome or Firefox, with or without a window, in `config.properties` or on the command line (see [Configuration](#configuration)). |
| Bonus: parallel execution | ✓ | JUnit 5 parallel execution. The 2 tests that use the shared admin account run sequentially (`@ResourceLock`). |
| Bonus: parameterization with JUnit 5 | ✗ | See [Not done](#not-done). |
| Bonus: reporting | ✓ | Allure Report. |

## Not done

- **The media page in "Conclude course by progress" is not checked.** The test opens the 2 media and checks the result in the course room: the "passed" status of each media, the progress and the course status. It does not check the media page itself: the name, the image, the description, the navigation buttons and the green checkmark. These checks would use the same techniques as the checks on the other pages, so the work focused on the framework instead.
- **Parameterization with JUnit 5.** A good candidate for it is "Denial of access", with the login form as the parameter (`@ParameterizedTest` with `@EnumSource`). The test case does not tell where the learner makes the second and third attempts (see [Assumptions](#assumptions)). The system must count failed attempts in each login form. If one form does not count them, it does not stop password guessing. Thus the test can run 2 times:
  - all 3 attempts in the login panel on the start page;
  - the first attempt on the start page, the second and third attempts on the login page. This run also checks that the system adds up the attempts from different forms.

  The steps after the failed attempts are the same in the 2 runs. The 2 runs block the same learner, so they must run sequentially.

## Assumptions

- **Typos and conflicts in the test cases.** Some test cases contain typos in user names and passwords. In some test cases, the preconditions and the steps also give different credentials for the same user. The tests use the credentials that work on the test environment.
- **Course type** on the course page. The assignment does not define it. The test checks the label in the course header, because it is the only possible element on the page.
- **Repeated login attempts.** The "Denial of access" test case does not tell where the user makes the second and third attempts. The test makes all 3 attempts in the login panel on the start page. Before each attempt, the test opens the start page again. A parameterized test could cover the 2 possible meanings (see [Not done](#not-done)).
- **Navigation search.** The assignment refers to a search icon in the navigation bar. The test uses the Search button in the navigation bar of the admin.
- **Back to the syllabus.** The "Conclude course by progress" test case does not tell how to go back from a media to the course room. The test opens the course room page again. Thus the test needs no page object for the media page, which it does not check (see [Not done](#not-done)).

## Design notes

- **The base URL and the credentials are not in the repository.** The assignment asks for a configuration file with the base URL. The browser is in the committed `config.properties`. However, the base URL and the accounts are only in the ignored `config.local.properties`: the test environment belongs to the company and must not be public.
- **Passwords are not in the report.** Steps receive a `User` object and read the password internally. Thus Allure does not show the password as a step parameter.
- **CI only compiles the tests.** The GitHub Actions workflow builds the project on every push. It does not run the UI tests: in a public repository, the CI logs and screenshots would show the address of the test environment and its accounts. Thus the build badge shows that the project compiles, not that the tests pass.
- **WebDriver management.** Selenium Manager is part of Selenium 4. It downloads the driver, so the project does not use WebDriverManager or local driver files.
- **Server errors.** If the page title starts with a 5xx or 404 status code, the test fails immediately with a clear message. It does not wait for the timeouts of the next steps. Selenium cannot read HTTP status codes, so the test checks the page title.
- **Rejected login.** If the system rejects a login, the test fails with the error message from the login page. It does not fail later because an element of the home page is missing.
- **Errors in the configuration.** If a key is missing, only the test that needs this key fails. The message names the key.

### Repeatable tests on shared accounts

The tests change data on the server: course participation, the wishlist and failed login attempts. Other people can also use the same accounts. Thus each test restores the initial state of its account before the test and again after the test:

- "Conclude course by progress": if the learner is a participant of the course (enrolled, passed or cancelled), an admin cancels the participation and removes the learner from the course;
- "Add to wishlist": if the course is in the wishlist, the learner removes it;
- "Denial of access": if the learner has failed login attempts, an admin resets them for this learner only.

If the account is already in the initial state, the test does not change it. If the test cannot restore the initial state before the test, JUnit skips the test with a message. If the cleanup after the test fails, the teardown fails. This also applies to a skipped test: JUnit then reports the teardown failure instead of the skip.

The cleanup after the test deletes the cookies and opens the start page again. Thus it does not use the session of the test, even if the test failed in the middle of a scenario.

This approach has a cost. Each test takes more time, because the preparation and the cleanup repeat many steps of the scenarios: login, search and navigation through the UI. 2 independent changes can reduce this cost:

- **Other test data.** Each test can create its own users and courses instead of shared accounts. Then the test does not depend on changes by other people. But the creation takes time, and the test environment can forbid it.
- **Other access.** The preparation and the cleanup can use an API or the database instead of the UI. This is much faster, but needs access that the assignment does not give. Direct changes in the database also bypass the application logic, so the data can become inconsistent.

The 2 changes can be combined: an API can restore the shared accounts or create new data.

## Prerequisites

- **JDK 21.** Install it with one command:
  - Windows: `winget install BellSoft.LibericaJDK.21`
  - macOS: `brew install --cask temurin@21`
  - Linux: `sudo apt install openjdk-21-jdk` (or the package manager of the distribution)
- **Chrome** (default) or **Firefox.** Selenium Manager downloads the correct driver. If the browser is not installed, Selenium Manager downloads it.
- **Maven and Allure need no installation.** The Maven Wrapper (`mvnw`) downloads Maven. The Allure Maven plugin downloads the Allure command-line tool.
- **IDE (optional).** IntelliJ IDEA without a paid subscription is sufficient. It has built-in support for Maven and JUnit 5. For VS Code, install the Extension Pack for Java.

## Getting the code

```
git clone https://github.com/ilyakanevskiy/homework-2025-05-java-selenium-ui.git
cd homework-2025-05-java-selenium-ui
```

Without Git, download the ZIP archive on GitHub (*Code → Download ZIP*) and extract it.

## Configuration

The configuration files are in `src/test/resources`:

- `config.properties` — the browser and the headless mode;
- `config.local.properties.example` — a template for the local configuration (base URL and accounts);
- `junit-platform.properties` — parallel execution and automatic registration of the Allure JUnit 5 extension (setup and teardown appear as separate sections in the report);
- `allure.properties` — the results directory.

To prepare the local configuration:

1. Copy `src/test/resources/config.local.properties.example` to `src/test/resources/config.local.properties`.
2. Set these values:
   - `base_url` — the start page of the test environment (the external dashboard);
   - `learner1_*`, `learner2_*`, `learner3_*` — the learners of the 3 scenarios;
   - `admin_*` — the admin account.

Git ignores `config.local.properties`. If a key is missing, the test fails with a message that names the key.

To select the browser, set `browser=chrome` or `browser=firefox` in `src/test/resources/config.properties`. To run the browser without a window, set `headless=true`. In headless mode, the window size is 1920 × 1080.

The same values can also come from the command line. A value on the command line has priority over the value in the file:

```
./mvnw clean test -Dbrowser=firefox -Dheadless=true
```

## Running from the command line

Run the commands in the project root. On Windows, use `.\mvnw`. On Linux and macOS, use `./mvnw`.

```
# all tests
./mvnw clean test

# one test class
./mvnw clean test -Dtest=AddToWishlistTest

# compile only, do not run the tests
./mvnw test-compile
```

The tests run in parallel. The exception is the tests that log in as the admin ("Conclude course by progress" and "Denial of access"): they use the same admin account, and a logout ends the admin session in every browser. Thus they run one after another (`@ResourceLock`), and "Add to wishlist" runs in parallel with them. To run all tests sequentially:

```
./mvnw clean test "-Djunit.jupiter.execution.parallel.enabled=false"
```

## Running from an IDE

The AspectJ agent records Allure steps (`@Step`) and AssertJ assertions in the report. Maven attaches this agent through the Surefire `argLine`. The agent is a jar in the local Maven repository (by default, `~/.m2/repository`). Maven downloads this jar only when it runs the `test` phase. Without the jar, an IDE cannot start the tests. Thus, before the first test run from an IDE, run this command once in the project root:

```
./mvnw test -DskipTests
```

### IntelliJ IDEA

1. Open the project folder and click **Trust Project**. IDEA loads the Maven project and uses the Maven Wrapper.
2. If the editor shows "Project JDK is not defined", click **Setup SDK** and select JDK 21. If JDK 21 is not installed, select **Download JDK**.
3. Download the AspectJ agent. Use the command above or the **Maven** tool window: turn on **Skip Tests**, double-click *Lifecycle → test*, then turn off **Skip Tests**.
4. Run a test class or a test method from the editor.

IDEA passes the Surefire `argLine` to its test runs: *Settings → Build, Execution, Deployment → Build Tools → Maven → Running Tests → argLine* is selected by default. Thus the report contains the steps.

To open the report, double-click *Plugins → allure → allure:serve* in the **Maven** tool window.

### VS Code

Install the Extension Pack for Java and open the project folder. Add the agent to `java.test.config` in `.vscode/settings.json`:

```json
"java.test.config": {
    "name": "allure",
    "vmArgs": ["-javaagent:<local Maven repository>/org/aspectj/aspectjweaver/1.9.24/aspectjweaver-1.9.24.jar"]
}
```

Replace `<local Maven repository>` with the full path. On Windows, use forward slashes, for example `C:/Users/<user>/.m2/repository`. Do not add quotes, even if the path contains spaces.

Then run a test class or a test method from the editor or from the Testing view.

To open the report, run `./mvnw allure:serve` in the terminal. The Maven view in VS Code builds a wrong command for this plugin.

## Report

After a test run:

```
./mvnw allure:serve
```

This command generates the report and opens it in the browser. If there is no browser (for example, on a server), use `./mvnw allure:report`. It writes the report to `target/site/allure-maven-plugin`.

Always use `clean test` before a report. Allure keeps all results in `target/allure-results`. Without `clean`, the report also shows the results of earlier runs.

If a test fails, the report contains a screenshot of the page. The screenshot is in the section where the failure happened: the setup, the test or the teardown.

## Confidentiality

- The assignment and the test scenarios are not published. This README describes them in its own words.
- The address of the test environment, the user names and the passwords are not in the repository.
