package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
        System.out.println("Scrapping " + driver.getTitle());
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        WebElement txt = driver.findElement(By.name("my-text"));
        txt.sendKeys("I love Nan Yin!!!!!");

        WebElement button = driver.findElement(By.cssSelector("button"));
        button.click();

        WebElement message = driver.findElement(By.id("message"));
        System.out.println(message.getText());
        driver.quit();

    }
}