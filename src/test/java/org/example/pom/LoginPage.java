package org.example.pom;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    // Ищем нужные поля и кнопки через Selenide
    private final SelenideElement usernameField = $("form#login input[type='text']");
    private final SelenideElement passwordField = $("form#login input[type='password']");
    private final SelenideElement loginButton = $("form#login button");
    private final SelenideElement errorBlock = $("div.error-block");

    // Авторизация
    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    // Ввод логина с дополнительной проверкой и очисткой
    public void typeUsernameInField(String username) {
        usernameField.shouldBe(visible).clear(); // Очистка поля перед вводом
        usernameField.setValue(username);
    }

    // Ввод пароля с дополнительной проверкой и очисткой
    public void typePasswordInField(String password) {
        passwordField.shouldBe(visible).clear(); // Очистка поля перед вводом
        passwordField.setValue(password);
    }

    // Клик на кнопку
    public void clickLoginButton() {
        loginButton.shouldBe(visible).click();
    }

    // Метод для получения текста ошибки
    public String getErrorBlockText() {
        return errorBlock.shouldBe(visible).getText().replace("\n", " ");
    }
}

