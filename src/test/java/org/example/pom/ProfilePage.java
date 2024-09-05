package org.example.pom;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.visible;

public class ProfilePage {
    @FindBy(xpath = "//h3/following-sibling::div" + "//div[contains(text(), 'Full name')]/following-sibling::div")
    private SelenideElement fullNameAdditionalInfo;

    @FindBy(css = "div.mdc-card h2")
    private SelenideElement fullNameInAvatarSection;

    @FindBy(css = "button[title=\"More options\"]")
    private SelenideElement editProfileButton;

    @FindBy(xpath = "//input[@type='date' and contains(@class, 'mdc-text-field__input')]")
    private SelenideElement birthdateInput;

    @FindBy(xpath = "//button[contains(@class, 'mdc-button') and .//span[contains(@class, 'mdc-button__label') and text()='Save']]\n")
    private SelenideElement saveButton;

    @FindBy(xpath = "//button[contains(@class, 'mdc-icon-button') and .//text()[contains(., 'close')]]\n")
    private SelenideElement closeButton;

    @FindBy(xpath = "//div[@class='label svelte-vyyzan' and text()='Date of birth']/following-sibling::div[@class='content svelte-vyyzan']")
    private SelenideElement birthdateAdditionalInfo;

    public void openEditProfileModal() {
        editProfileButton.shouldBe(visible).click();
    }

    public void setBirthdate(String birthdate) {
        birthdateInput.shouldBe(visible).setValue(birthdate);
    }

    public void saveChanges() {
        saveButton.shouldBe(visible).click();
    }

    public void closeButtonModal() {
        closeButton.shouldBe(visible).click();
    }

    public String getBirthdateAdditionalInfo() {
        return birthdateAdditionalInfo.shouldBe(visible).getText();
    }

    public String getFullNameAdditionalInfo() {
        return fullNameAdditionalInfo.shouldBe(visible).text();
    }

    public String getFullNameInAvatarSection() {
        return fullNameInAvatarSection.shouldBe(visible).text();
    }
}
