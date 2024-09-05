package org.example.pom.elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;

public class GroupTableRow {
    private final SelenideElement root;

    public GroupTableRow(SelenideElement root) {
        this.root = root;
    }

    // Для получения имени группы в строке
    public String getTitle() {
        return root.$x("./td[2]").shouldBe(visible).getText();
    }

    // Для получения статуса группы в строке
    public String getStatus() {
        return root.$x("./td[4]").shouldBe(visible).getText();
    }

    // Для получения ID группы в строке
    public String getId() {
        return root.$x("./td[1]").shouldBe(visible).getText();
    }

    // Клик на иконку с ключом
    public void clickKeyIcon() {
        root.$x(".//td/button[text()='key']").shouldBe(visible).click();
    }

    // Клик на иконку редактирования
    public void clickEditIcon() {
        root.$x(".//td/button[text()='edit']").shouldBe(visible).click();
    }

    // Клик на иконку корзины
    public void clickTrashIcon() {
        root.$x(".//td/button[text()='delete']").shouldBe(visible).click();
        // Ожидание появления иконки восстановления
        root.$x(".//td/button[text()='restore_from_trash']").shouldBe(visible);
    }

    // Клик на иконку восстановления из корзины
    public void clickRestoreFromTrashIcon() {
        root.$x(".//td/button[text()='restore_from_trash']").shouldBe(visible).click();
        // Ожидание появления иконки удаления
        root.$x(".//td/button[text()='delete']").shouldBe(visible);
    }
}




