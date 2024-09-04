package org.example.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private final WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkInNavBar;

    @FindBy(id = "create-btn")
    private WebElement createGroupButton;

    @FindBy(xpath = "//form//span[contains(text(), 'Fist Name')]/following-sibling::input")
    private WebElement groupNameField;

    @FindBy(xpath = "//form//span[contains(text(), 'Login')]/following-sibling::input")
    private WebElement groupLoginField;

    @FindBy(css = "form div.submit button")
    private WebElement submitButtonOnModalWindow;

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public WebElement waitAndGetGroupTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", title);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void createGroup(String groupName) {
        wait.until(ExpectedConditions.visibilityOf(createGroupButton)).click();
        wait.until(ExpectedConditions.visibilityOf(groupNameField)).sendKeys((groupName));
        wait.until(ExpectedConditions.visibilityOf(groupLoginField)).sendKeys((groupName));
        submitButtonOnModalWindow.click();
        waitAndGetGroupTitleByText(groupName);
    }

    public String getUserNameLabelText() {
        return wait.until(ExpectedConditions.visibilityOf(usernameLinkInNavBar))
                .getText().replace("\n", " ");
    }
}