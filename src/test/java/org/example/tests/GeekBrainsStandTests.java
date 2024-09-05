package org.example.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.example.pom.ProfilePage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeekBrainsStandTests {
    private MainPage mainPage;
    private LoginPage loginPage;
    private static String USERNAME;
    private static String PASSWORD;
    private ProfilePage profilePage;

    @BeforeAll
    public static void setupClass() {
        // В переменных окружения логин и пароль geekbrains_password=PASS, geekbrains_username=USER
        USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
        PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }

    @BeforeEach
    public void setupTest() {
        // Открываем страницу с помощью Selenide
        Selenide.open("https://test-stand.gb.ru/login");
        // Инициализация объекта LoginPage
        loginPage = new LoginPage();
    }

    // ДЗ 1. Залогиниться, создать группу и проверить её наличие, сделать скрин
    @Test
    public void testAddingDummyOnMainPage() throws IOException {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Создание dummy. Даём уникальное имя, чтобы в каждом запуске была проверка нового имени
        long currentTimeMillis = System.currentTimeMillis();
        String dummyTestName = "New Dummy " + currentTimeMillis;
        mainPage.createDummyWithLogin(dummyTestName, String.valueOf(currentTimeMillis));
        // Проверка, что dummy создан и находится в таблице
        assertTrue(mainPage.waitAndGetDummyTitleByText(dummyTestName).isDisplayed());
        // Сохраняем скриншот
        File screenshotFile = Selenide.screenshot(OutputType.FILE); // Получаем скриншот как файл
        Path destinationPath = Path.of("src/test/resources/screenshot_" + currentTimeMillis + ".png");
        Files.copy(screenshotFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    // Сделать созданную группу активной/неактивно и проверить статусы
    @Test
    void testArchiveDummyOnMainPage() {
        // Обычный логин + создание dummy
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
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
        mainPage = Selenide.page(MainPage.class);
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
        Thread.sleep(500);
        // Проверить результат изменения в таблице по ID
        assertEquals("LastName FirstName", mainPage.getNameOfDummyWithId(idOfDummyWithName));
    }

    @Test
    void testCredentialsPopupForDummies() {
        // Обычный логин + создание dummy
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
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

    @Test
    void testFullNameOnProfilePage() {
        // Логин в систему с помощью метода Page Object
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Навигация на Profile page
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        // Инициализация ProfilePage с помощью Selenide
        profilePage = Selenide.page(ProfilePage.class);
        assertEquals("Nochevnoy Sergey", profilePage.getFullNameAdditionalInfo());
        assertEquals("Nochevnoy Sergey", profilePage.getFullNameInAvatarSection());
    }

    // ДЗ 4.2. Написать тест на Profile Page
    @Test
    void testChangeBirthdayData() throws InterruptedException {
        // Логин в систему с помощью метода Page Object
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Навигация на Profile page
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        // Инициализация ProfilePage с помощью Selenide
        profilePage = Selenide.page(ProfilePage.class);
        // Открыть модальное окно редактирования
        profilePage.openEditProfileModal();
        // Изменить значение Birthdate
        String newBirthdate = "12.12.2022";
        profilePage.setBirthdate(newBirthdate);
        // Нажать на кнопку Save и закрыть модальное окно
        profilePage.saveChanges();
        // Проверить, что изменения применились в поле Date of Birth в секции Additional Info
        profilePage.closeButtonModal();
        Thread.sleep(2000);
        String updatedBirthdate = profilePage.getBirthdateAdditionalInfo();
        assertEquals(newBirthdate, updatedBirthdate);
    }

    // Закрываем браузер и драйвер
    @AfterEach
    public void teardown() {
        WebDriverRunner.closeWebDriver();
    }
}