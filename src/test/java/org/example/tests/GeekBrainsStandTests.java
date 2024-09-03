package org.example.tests;

import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

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

//    // ДЗ 1
//    @Test
//    public void testAddDummy() throws InterruptedException {
//        driver.get("https://test-stand.gb.ru/login");
//        // Логинимся
//        wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.cssSelector("form#login input[type='text']"))).sendKeys(USERNAME);
//        wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.cssSelector("form#login input[type='password']"))).sendKeys(PASSWORD);
//        WebElement loginButton = driver.findElement(By.cssSelector("form#login button"));
//        loginButton.click();
//        wait.until(ExpectedConditions.invisibilityOf(loginButton));
//        // Нажимаем на кнопку '+' для добавления dummy
//        Thread.sleep(1500);
//        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("//*[@id='create-btn']")));
//        addButton.click();
//        // Ждём открытия модального окна
//        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//*[@id=\"titleId-title\"]/span")));
//        // Вводим имя dummy и логин
//        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//*[@id='upsert-item']/div[1]/label/input")));
//        nameInput.sendKeys("dummy");
//        WebElement loginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//*[@id=\"upsert-item\"]/div[5]/label/input")));
//        loginInput.sendKeys("dummyLogin" + System.currentTimeMillis());
//        // Нажимаем кнопку SAVE и Close
//        WebElement saveButton = modal.findElement(
//                By.xpath("//*[@id=\"upsert-item\"]/div[8]/button"));
//        saveButton.click();
//        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("//*[@id='app']/main/div/div/div[3]/div[2]/div/div[1]/button")));
//        closeButton.click();
//        // Проверяем, что нужный элемент (title) появился в таблице
//        WebElement textElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//*[@id=\"app\"]/main/div/div/div[1]/div[1]/table/tbody/tr[1]/td[2]")
//        ));
//        // Проверяем видимость элемента
//        String expectedText = "dummy"; // Ожидаемый текст
//        Assertions.assertEquals(expectedText, textElement.getText().trim(), "Текст в ячейке не совпадает с ожидаемым.");
//        // Сохраняем скриншот
//        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        try {
//            FileUtils.copyFile(srcFile, new File("src/main/resources/screenshot_" + System.currentTimeMillis() + ".png"));
//            System.out.println("Screenshot saved successfully.");
//        } catch (IOException e) {
//            System.err.println("Failed to save screenshot: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}