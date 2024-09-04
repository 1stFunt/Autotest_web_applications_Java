package org.example.pom;

import org.example.pom.elements.GroupTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPage {

    private final WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkInNavBar;
    @FindBy(css = "table tbody tr")
    private WebElement tableRow;
    @FindBy(id = "create-btn")
    private WebElement createDummyButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Fist Name')]/following-sibling::input")
    private WebElement dummyFirstNameField;
    @FindBy(xpath = "//form//span[contains(text(), 'Last Name')]/following-sibling::input")
    private WebElement dummyLastNameField;
    @FindBy(xpath = "//form//span[contains(text(), 'Login')]/following-sibling::input")
    private WebElement loginField;
    @FindBy(css = "form div.submit button")
    private WebElement submitButtonOnModalWindow;
    @FindBy(xpath = "//span[text()='Creating Dummy']//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private WebElement closeCreateDummyIcon;
    @FindBy(xpath = "//span[contains(text(), 'Editing Dummy')]//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private WebElement closeEditingDummyIcon;
    // Добавление списков элементов (ряды в таблице)
    @FindBy(xpath = "//table[@aria-label='Dummies list']/tbody/tr")
    private List<WebElement> rowsInDummyTable;
    @FindBy(css = "div.mdc-dialog.mdc-dialog--open h2")
    private WebElement popupHeader;
    @FindBy(css = "div.mdc-dialog.mdc-dialog--open h2 + div")
    private WebElement dummyCredentialsPopupContent;
    @FindBy(xpath = "//div[@class='mdc-linear-progress mdc-linear-progress--indeterminate mdc-data-table__linear-progress mdc-linear-progress--animation-ready']")
    private WebElement progressBar;

    // Конструктор для инициализации элементов (ленивая инициализация)
    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public WebElement waitAndGetDummyTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", title);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    // Метод для получения логина авторизированного пользователя
    public String getUsernameLabelText() {
        return wait.until(ExpectedConditions.visibilityOf(usernameLinkInNavBar))
                .getText().replace("\n", " ");
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
        return wait.until(ExpectedConditions.visibilityOf(popupHeader)).getText();
    }

    public String getDummyCredentialsPopupContentText() {
        return wait.until(ExpectedConditions.visibilityOf(dummyCredentialsPopupContent)).getText();
    }

    public void createDummyWithLogin(String dummyName, String login) {
        wait.until(ExpectedConditions.visibilityOf(tableRow));
        createDummyButton.click();
        wait.until(ExpectedConditions.visibilityOf(dummyFirstNameField)).sendKeys(dummyName);
        wait.until(ExpectedConditions.visibilityOf(loginField)).sendKeys(login);
        submitButtonOnModalWindow.click();
        waitAndGetDummyTitleByText(dummyName);
    }

    public void setDummyFirstNameOnPopup(String firstName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOf(dummyFirstNameField));
        field.clear();
        field.sendKeys(firstName);
    }

    public void setDummyLastNameField(String lastName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOf(dummyLastNameField));
        field.clear();
        field.sendKeys(lastName);
    }

    public void clickSaveOnEditingDummyModalWindow() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(submitButtonOnModalWindow)).click();
        wait.until(ExpectedConditions.invisibilityOf(progressBar));
        /*
        Использование Sleep - не лучшая практика,
        но иногда если иное решение занимает много времени и стоит больше,
        то небольшие ожидания имеют смысл.
        */
        Thread.sleep(500);
    }

    public void closeEditingDummyModalWindow() {
        closeEditingDummyIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeEditingDummyIcon));
    }

    public void closeCreateDummyModalWindow() {
        closeCreateDummyIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeCreateDummyIcon));
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
        return wait.until(ExpectedConditions.visibilityOf(popupHeader)).getText();
    }

    // Ищем строку в таблице, используя Stream
    private GroupTableRow getDummyRowByName(String name) {
        return rowsInDummyTable.stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getTitle().equals(name))
                .findFirst().orElseThrow();
    }

    // Ищем по ID
    private GroupTableRow getDummyRowById(String id) {
        return rowsInDummyTable.stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getId().equals(id))
                .findFirst().orElseThrow();
    }
}





