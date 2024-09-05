package org.example.pom;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.example.pom.elements.GroupTableRow;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private SelenideElement usernameLinkInNavBar;
    @FindBy(css = "nav li.mdc-menu-surface--anchor div li")
    private SelenideElement profileLinkInNavBar;
    @FindBy(css = "table tbody tr")
    private SelenideElement tableRow;
    @FindBy(id = "create-btn")
    private SelenideElement createDummyButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Fist Name')]/following-sibling::input")
    private SelenideElement dummyFirstNameField;
    @FindBy(xpath = "//form//span[contains(text(), 'Last Name')]/following-sibling::input")
    private SelenideElement dummyLastNameField;
    @FindBy(xpath = "//form//span[contains(text(), 'Login')]/following-sibling::input")
    private SelenideElement loginField;
    @FindBy(css = "form div.submit button")
    private SelenideElement submitButtonOnModalWindow;
    @FindBy(xpath = "//span[text()='Creating Dummy']//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private SelenideElement closeCreateDummyIcon;
    @FindBy(xpath = "//span[contains(text(), 'Editing Dummy')]//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private SelenideElement closeEditingDummyIcon;
    // Добавление списков элементов (ряды в таблице)
    private final ElementsCollection rowsInDummyTable = $$x("//table[@aria-label='Dummies list']/tbody/tr");
    @FindBy(css = "div.mdc-dialog.mdc-dialog--open h2")
    private SelenideElement popupHeader;
    @FindBy(css = "div.mdc-dialog.mdc-dialog--open h2 + div")
    private SelenideElement dummyCredentialsPopupContent;
    @FindBy(xpath = "//div[@class='mdc-linear-progress mdc-linear-progress--indeterminate mdc-data-table__linear-progress mdc-linear-progress--animation-ready']")
    private SelenideElement progressBar;

    public SelenideElement waitAndGetDummyTitleByText(String title) {
        return $x(String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", title)).shouldBe(visible);
    }

    // Метод для получения логина авторизированного пользователя
    public String getUsernameLabelText() {
        return usernameLinkInNavBar.shouldBe(visible).getText().replace("\n", " ");
    }

    public void clickTrashIconOnDummyWithName(String name) {
        getDummyRowByName(name).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnDummyWithName(String name) {
        getDummyRowByName(name).clickRestoreFromTrashIcon();
    }

    public void clickKeyIconOnDummyWithName(String name) {
        getDummyRowByName(name).clickKeyIcon();
    }

    public String getDummyCredentialsPopupHeaderText() {
        return popupHeader.shouldBe(visible).getText();
    }

    public String getDummyCredentialsPopupContentText() {
        return dummyCredentialsPopupContent.shouldBe(visible).getText();
    }

    public void createDummyWithLogin(String dummyName, String login) {
        createDummyButton.shouldBe(visible).click();
        dummyFirstNameField.shouldBe(visible).setValue(dummyName);
        loginField.shouldBe(visible).setValue(login);
        submitButtonOnModalWindow.shouldBe(visible).click();
        waitAndGetDummyTitleByText(dummyName);
    }

    public void setDummyFirstNameOnPopup(String firstName) {
        WebElement field = dummyFirstNameField.shouldBe(visible);
        field.clear();
        field.sendKeys(firstName);
    }

    public void setDummyLastNameField(String lastName) {
        WebElement field = dummyLastNameField.shouldBe(visible);
        field.clear();
        field.sendKeys(lastName);
    }

    public void clickSaveOnEditingDummyModalWindow() throws InterruptedException {
        submitButtonOnModalWindow.shouldBe(visible).click();
        progressBar.shouldBe(visible);
        /*
        Использование Sleep - не лучшая практика,
        но иногда если иное решение занимает много времени и стоит больше, то небольшие ожидания имеют смысл.
        */
        Thread.sleep(500);
    }

    public void closeEditingDummyModalWindow() {
        closeEditingDummyIcon.click();
        closeEditingDummyIcon.shouldBe(visible);
    }

    public void closeCreateDummyModalWindow() {
        closeCreateDummyIcon.click();
        closeCreateDummyIcon.shouldBe(visible);
    }

    public void clickEditIconOnDummyWithName(String name) {
        getDummyRowByName(name).clickEditIcon();
    }

    public String getStatusOfDummyWithName(String name) {
        return getDummyRowByName(name).getStatus();
    }

    public String getIdOfDummyWithName(String name) {
        return getDummyRowByName(name).getId();
    }

    public String getNameOfDummyWithId(String id) {
        return getDummyRowById(id).getTitle();
    }

    public String getEditingDummyPopupHeaderText() {
        return popupHeader.shouldBe(visible).getText();
    }

    // Ищем строку в таблице, используя Stream
    private GroupTableRow getDummyRowByName(String name) {
        return rowsInDummyTable.shouldHave(sizeGreaterThan(0)).stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getTitle().equals(name))
                .findFirst().orElseThrow();
    }

    // Ищем по ID
    private GroupTableRow getDummyRowById(String id) {
        return rowsInDummyTable.shouldHave(sizeGreaterThan(0)).stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getId().equals(id))
                .findFirst().orElseThrow();
    }

    public void clickUsernameLabel() {
        usernameLinkInNavBar.shouldBe(visible).click();
    }

    public void clickProfileLink() {
        profileLinkInNavBar.shouldBe(visible).click();
    }
}





