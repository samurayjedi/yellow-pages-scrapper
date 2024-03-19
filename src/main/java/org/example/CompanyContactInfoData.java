package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.*;


public final class CompanyContactInfoData {
    private static final String INITIALIZED = "initialized";
    private static final String LISTINGS = "listings";
    private final WebDriver driver;
    private final List<Map<String, String>> data;
    private String step;
    public static CompanyContactInfoData builder() {
        return new CompanyContactInfoData();
    }
    public CompanyContactInfoData() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        data = new ArrayList<>();
        step = INITIALIZED;
    }

    public CompanyContactInfoData search(String what, String where) {
        driver.get("https://www.yellowpages.ca/");
        WebElement txtWhat = driver.findElement(By.id("whatwho"));
        txtWhat.sendKeys(what);
        WebElement txtWhere = driver.findElement(By.id("where"));
        txtWhere.sendKeys(where);
        txtWhere.sendKeys(Keys.ENTER);
        step = LISTINGS;

        return this;
    }

    public CompanyContactInfoData extractNames() throws Exception {
        if (!Objects.equals(step, LISTINGS)) {
            throw new Exception("call search() first!!!");
        }
        String namesXpath = "//div[contains(@class, 'listing__title--wrap')]//h3[contains(@class, 'listing__name')]//a[contains(@class, 'listing__name--link')]";
        List<WebElement> names = driver.findElements(By.xpath(namesXpath));
        fillDataWith(names, "name");

        return this;
    }

    public CompanyContactInfoData extractAddresses() throws Exception {
        if (!Objects.equals(step, LISTINGS)) {
            throw new Exception("call search() first!!!");
        }
        String addressesXpath = "//div[contains(@class, 'listing__address')]//span[contains(@class, 'listing__address--full')]";
        List<WebElement> addresses = driver.findElements(By.xpath(addressesXpath));
        fillDataWith(addresses, "address");

        return this;
    }

    public CompanyContactInfoData extractPhones() throws Exception {
        if (!Objects.equals(step, LISTINGS)) {
            throw new Exception("call search() first!!!");
        }
        // the phones are into a menu, therefore, i must click in each one for display them (otherwise throws "")
        String btnPhonesXpath = "//li[contains(@class, 'mlr__item--phone')]";
        List<WebElement> btnsPhones = driver.findElements(By.xpath(btnPhonesXpath));
        for (WebElement btnPhone: btnsPhones) {
            btnPhone.click();
        }
        // now, the same as before....
        String phonesXpath = "//li[contains(@class, 'mlr__item--phone')]//ul[contains(@class, 'mlr__submenu')]//li[contains(@class, 'mlr__submenu__item')]";
        List<WebElement> phones = driver.findElements(By.xpath(phonesXpath));
        fillDataWith(phones, "phone");

        return this;
    }

    public List<Map<String, String>> finish() {
        return data;
    }

    private void fillDataWith(List<WebElement> els, String key) {
        int i = 0;
        for (WebElement el: els) {
            if (!Piwi.indexExists(data, i)) {
                data.add(new HashMap<>());
            }
            data.get(i).put(key, el.getText());
            i++;
        }
    }
}
