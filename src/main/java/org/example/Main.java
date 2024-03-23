package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        ScrapperBuilder piwi = new ScrapperBuilder();
        piwi.search("Restaurants", "Colorado");
        for (int i = 0; i < piwi.pages; i++) {
            piwi
                .doTheFollowing(driver -> {
                    // move to end of page to load all listings
                    // WebElement el = driver.findElement(By.xpath("//span[contains(@class, 'pageCount')]"));
                    // String script = "arguments[0].scrollIntoView(true);";
                    JavascriptExecutor js = ((JavascriptExecutor)driver);
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    // js.executeScript(script, el);
                    try {
                        Thread.sleep(2000);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // move to start of page, otherwise, click in each btn phone throws errors (not is posible click the button blah blah)
                    js.executeScript("window.scrollTo(0, 0);");
                })
                .extractNames()
                .extractAddresses()
                .extractPhones();
            if (i != piwi.pages - 1) {
                piwi.nextPage();
            }
        }
        List<Map<String, String>> data =  piwi.finish();
        /** construct the csv */
        CSVBuilder piyoe = new CSVBuilder();
        piyoe.addColumn("Name");
        piyoe.addColumn("Address");
        piyoe.addColumn("Phone");
        piyoe.endRow();
        for (Map<String, String> record: data) {
            piyoe.addColumn(record.containsKey("name") ? '"'+record.get("name")+'"' : "Unavailable");
            piyoe.addColumn(record.containsKey("address") ? '"'+record.get("address")+'"' : "Unavailable");
            piyoe.addColumn(record.containsKey("phone") ? '"'+record.get("phone")+'"' : "Unavailable");
            piyoe.endRow();
        }
        /** save csv into a file */
        String filename = UUID.randomUUID().toString() + ".csv";
        String path = "/home/samurayjedi";
        File file = new File(path+'/'+filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(piyoe.toString());
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}