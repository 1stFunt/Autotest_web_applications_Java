package org.example.pom.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class GroupTableRow {
    private final WebElement root;

    public GroupTableRow(WebElement root) {
        this.root = root;
    }

    // Для получения имени группы в строке
    public String getTitle() {
        return root.findElement(By.xpath("./td[2]")).getText();
    }

    // Для получения статуса группы в строке
    public String getStatus() {
        return root.findElement(By.xpath("./td[4]")).getText();
    }

    public String getId() {
        return root.findElement(By.xpath("./td[1]")).getText();
    }

    public void clickKeyIcon() {
        root.findElement(By.xpath("./td/button[text()='key']")).click();
    }

    public void clickEditIcon() {
        root.findElement(By.xpath("./td/button[text()='edit']")).click();
    }

    public void clickTrashIcon() {
        root.findElement(By.xpath(".//td/button[text()='delete']")).click();
        ((Wait<WebElement>) new FluentWait<>(root)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class))
                .until(root -> root.findElement(By.xpath(".//td/button[text()='restore_from_trash']")));
    }

    public void clickRestoreFromTrashIcon() {
        root.findElement(By.xpath("./td/button[text()='restore_from_trash']")).click();
        ((Wait<WebElement>) new FluentWait<>(root)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class))
                .until(root -> root.findElement(By.xpath("./td/button[text()='delete']")));
    }
}



