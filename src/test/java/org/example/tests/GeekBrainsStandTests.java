package org.example.tests;

import org.apache.commons.io.FileUtils;
import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Путь до драйвера
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        // В переменных окружения логин и пароль geekbrains_password=PASS; geekbrains_username=USER
        USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
        PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }

    @BeforeEach
    public void setupTest() {
        // Создаём экземпляр драйвера
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // Растягиваем окно браузера
        driver.manage().window().maximize();
        driver.get("https://test-stand.gb.ru/login");
        // Объект созданного Page Object для взаимодействия со страницей
        loginPage = new LoginPage(driver, wait);
    }

    // ДЗ 1. Залогиниться, создать группу и проверить её наличие, сделать скрин
    @Test
    public void testAddingDummyOnMainPage() throws IOException {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Создание dummy. Даём уникальное имя, чтобы в каждом запуске была проверка нового имени
        long currentTimeMillis = System.currentTimeMillis();
        String dummyTestName = "New Dummy " + currentTimeMillis;
        mainPage.createDummyWithLogin(dummyTestName, String.valueOf(currentTimeMillis));
        // Проверка, что dummy создан и находится в таблице
        assertTrue(mainPage.waitAndGetDummyTitleByText(dummyTestName).isDisplayed());
        // Сохраняем скриншот
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File screenshotFile = screenshot.getScreenshotAs(OutputType.FILE);
        Path destinationPath = Path.of("src/test/resources/screenshot_" + currentTimeMillis + ".png");
        Files.copy(screenshotFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    // Сделать созданную группу активной/неактивно и проверить статусы
    @Test
    void testArchiveDummyOnMainPage() {
        // Обычный логин + создание dummy
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        long currentTimeMillis = System.currentTimeMillis();
        String dummyTestName = "New Dummy " + currentTimeMillis;
        mainPage.createDummyWithLogin(dummyTestName, String.valueOf(currentTimeMillis));
        // Требуется закрыть модальное окно
        mainPage.closeCreateDummyModalWindow();
        // Изменение созданной группы с проверками
        assertEquals("active", mainPage.getStatusOfDummyWithName(dummyTestName));
        mainPage.clickTrashIconOnDummyWithName(dummyTestName);
        assertEquals("inactive", mainPage.getStatusOfDummyWithName(dummyTestName));
        mainPage.clickRestoreFromTrashIconOnDummyWithName(dummyTestName);
        assertEquals("active", mainPage.getStatusOfDummyWithName(dummyTestName));
    }


    // ДЗ 2.1. Проверить текст ошибки при вводе пустых значений логина и пароля
    @Test
    public void testLoginWithEmptyFields() {
        // Клик на кнопку LOGIN без ввода данных в поля
        loginPage.clickLoginButton();
        // Проверка, что появился блок с ожидаемой ошибкой
        assertEquals("401 Invalid credentials.", loginPage.getErrorBlockText());
    }

    @Test
    void testEditingModalWindowAndEditedDummy() throws InterruptedException {
        // Обычный логин + создание dummy + сохранение ID созданной сущности
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        long currentTimeMillis = System.currentTimeMillis();
        String dummyTestName = "New Dummy " + currentTimeMillis;
        mainPage.createDummyWithLogin(dummyTestName, String.valueOf(currentTimeMillis));
        String idOfDummyWithName = mainPage.getIdOfDummyWithName(dummyTestName);
        // Требуется закрыть модальное окно
        mainPage.closeCreateDummyModalWindow();
        // Открыть Editing Popup
        mainPage.clickEditIconOnDummyWithName(dummyTestName);
        // Проверить заголовок Popup
        assertEquals(String.format("Editing Dummy %s", idOfDummyWithName),
                mainPage.getEditingDummyPopupHeaderText());
        // Ввести новые значения в First Name и Last Name
        mainPage.setDummyFirstNameOnPopup("FirstName");
        mainPage.setDummyLastNameField("LastName");
        mainPage.clickSaveOnEditingDummyModalWindow();
        mainPage.closeEditingDummyModalWindow();
        // Проверить результат изменения в таблице по ID
        assertEquals("LastName FirstName", mainPage.getNameOfDummyWithId(idOfDummyWithName));
    }

    @Test
    void testCredentialsPopupForDummies() {
        // Обычный логин + создание dummy
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        long currentTimeMillis = System.currentTimeMillis();
        String dummyTestName = "New Dummy " + currentTimeMillis;
        mainPage.createDummyWithLogin(dummyTestName, String.valueOf(currentTimeMillis));
        // Требуется закрыть модальное окно
        mainPage.closeCreateDummyModalWindow();
        // Открыть Credentials Popup
        mainPage.clickKeyIconOnDummyWithName(dummyTestName);
        // Проверка содержимого Popup
        assertEquals("Dummy credentials", mainPage.getDummyCredentialsPopupHeaderText());
        assertTrue(mainPage.getDummyCredentialsPopupContentText().contains("Login:"));
        assertTrue(mainPage.getDummyCredentialsPopupContentText().contains("PW:"));
    }

    // Закрываем браузер и драйвер
    @AfterEach
    public void teardown() {
        driver.quit();
    }
}