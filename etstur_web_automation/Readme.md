# Ucuzabilet Automated UI Testing

## Table of Contents
- [Project Overview](#project-overview)
- [Folder Structure](#folder-structure)
- [Dependencies](#dependencies)
- [Prerequisites](#prerequisites)
- [Browser Configuration](#browser-configuration)
- [How to Run Tests](#how-to-run-tests)
- [Logging and Reporting](#logging-and-reporting)
- [Test Steps and Design](#test-steps-and-design)


---

## Project Overview

This project is an automated UI testing framework for the **Ucuzabilet** flight search website. It leverages Java, Selenium WebDriver, TestNG, and Allure reporting. The project uses a **Page Object Model (POM)** style with elements stored in JSON files per page, enabling modular and maintainable test steps. Tests can be run in parallel on Chrome and Edge browsers with headless and incognito/private modes.

---

## Folder Structure

## Folder Structure

src/
├── main/
│ └── java/
│ └── utils/ # Utility classes: WebDriver factory, date utils, etc.
├── test/
│ ├── java/
│ │ ├── tests/ # Test classes and test listeners
│ │ └── pages/ # Page objects / base page class
│ ├── resources/
│ │ ├── homepage.json # JSON locator files per page
│ │ └── testng.xml # TestNG suite configuration
target/ # Compiled classes, reports, screenshots
pom.xml # Maven dependencies and build configuration
README.md # Project documentation



- **utils:** Helper classes for WebDriver management, date handling, etc.
- **tests:** Test classes containing test methods, listeners for reporting and screenshots.
- **pages:** Base page object class reading JSON locators and implementing common actions (click, fill, wait).
- **resources:** Locator JSON files and TestNG configuration files.
- **target:** Maven build output, Allure reports, screenshots saved here.

---

## Dependencies

- **Java 17+**
- **Selenium WebDriver 4.x**
- **TestNG** for test execution and parallelism
- **WebDriverManager** for automatic browser driver setup
- **Allure Report 2.29.x** for rich HTML test reports
- **SLF4J + Logback** for logging
- **Apache Commons IO** for file utilities (screenshots)

All dependencies are managed via Maven in the `pom.xml`.

---

## Prerequisites

- JDK 17 or higher installed and configured in PATH
- Maven 3.6+ installed
- Internet connection for WebDriverManager to download drivers
- Browsers installed: Chrome and Edge (latest versions recommended)

---

## Browser Configuration

- Tests can run on **Chrome** or **Edge** controlled via `testng.xml` parameters.
- Headless mode is enabled by default for faster CI runs.
- Browser options configured to disable notifications, popups, and use incognito/private mode.
- Parallel execution enabled in TestNG XML with thread count of 2 for Chrome and Edge simultaneously.

---

## How to Run Tests

1. Run tests with Maven:

mvn clean test

2. To specify browser via TestNG XML:
  
mvn clean test -DsuiteXmlFile=testng.xml

3. Generate and open Allure report:

mvn allure:serve

---

## Logging and Reporting
- Logging: SLF4J + Logback is configured. Logs contain info and error messages for each action (click, fill, wait, etc.).

- Screenshots: Captured automatically on test failure and test success, attached to Allure reports.

- Allure Reports: Rich interactive HTML reports with step annotations, severity levels, and attachments.

---

## Test Steps and Design
- Elements are stored in JSON files per page (homepage.json) with keys, locator types, and values.

- BasePage class loads JSON and provides generic methods: click(), fill(), waitForElementVisible(), getAttribute().

- Tests are written with TestNG and ordered using @Test(priority = x).

- Parallel browser runs configured with TestNG XML.

- TestListener captures screenshots and logs step results for Allure.

- Date utilities provide dynamic date calculation for calendar selections.


