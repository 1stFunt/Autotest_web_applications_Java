package org.example.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriverWait wait;
    // Ищем нужные поля и кнопки
    @FindBy(css = "form#login input[type='text']")
    private WebElement usernameField;
    @FindBy(css = "form#login input[type='password']")
    private WebElement passwordField;
    @FindBy(css = "form#login button")
    private WebElement loginButton;
    @FindBy(css = "div.error-block")
    private WebElement errorBlock;

    // Конструктор для инициализации элементов (ленивая инициализация)
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.wait = wait;
    }

    // Метод для авторизации
    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    // Ввод логина
    public void typeUsernameInField(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username);
    }

    // Ввод пароля
    public void typePasswordInField(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
    }

    // Клик на кнопку
    public void clickLoginButton() {
        wait.until(ExpectedConditions.visibilityOf(loginButton)).click();
    }

    // ДЗ 2.1 Метод для получения текста ошибки
    public String getErrorBlockText() {
        return wait.until(ExpectedConditions.visibilityOf(errorBlock))
                .getText().replace("\n", " ");
    }
}


