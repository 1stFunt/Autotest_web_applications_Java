package org.example.tests;

import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeekBrainsStandTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private MainPage mainPage;
    private LoginPage loginPage;
    private static String USERNAME;
    private static String PASSWORD;

    @BeforeAll
    public static void setupClass() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
        PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }

    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://test-stand.gb.ru/login");
        loginPage = new LoginPage(driver, wait);
    }

    @Test
    public void testStandGeekBrains() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUserNameLabelText().contains(USERNAME));
    }

    // ДЗ 1
    @Test
    public void testAddDummy() throws IOException {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUserNameLabelText().contains(USERNAME));
        String groupTestName = "dummy_" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        assertTrue(mainPage.waitAndGetGroupTitleByText(groupTestName).isDisplayed());
        // Сохраняем скриншот
        byte [] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Files.write(Path.of(
                "src/test/resources/screenshot_" + System.currentTimeMillis() + ".png"), screenshotBytes);
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}