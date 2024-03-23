package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.*;


public final class ScrapperBuilder {
    private static final String INITIALIZED = "initialized";
    private static final String LISTINGS = "listings";
    private static final String FINISHED = "finished";
    private final WebDriver driver;
    private final List<Map<String, String>> data;
    private String step;
    public int pages;
    private int offset = 0;

    public ScrapperBuilder() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        data = new ArrayList<>();
        step = INITIALIZED;
    }

    public ScrapperBuilder doTheFollowing(SeleniumTask task) {
        task.piwi(driver);

        return this;
    }

    public ScrapperBuilder search(String what, String where) {
        driver.get("https://www.yellowpages.ca/");
        WebElement txtWhat = driver.findElement(By.id("whatwho"));
        txtWhat.sendKeys(what);
        WebElement txtWhere = driver.findElement(By.id("where"));
        txtWhere.sendKeys(where);
        txtWhere.sendKeys(Keys.ENTER);
        /** now, the page should be loaded, get available pages */
        WebElement el = driver.findElement(By.xpath("//span[contains(@class, 'pageCount')]"));
        List<WebElement> spans = el.findElements(By.tagName("span"));
        pages = Integer.parseInt(spans.get(1).getText());
        step = LISTINGS;

        return this;
    }

    private void comprobations() throws Exception {
        if (!Objects.equals(step, LISTINGS)) {
            throw new Exception("call search() first!!!");
        }
        if (Objects.equals(step, FINISHED)) {
            throw new Exception("The scrapper has ben finished!!!!");
        }
    }

    public ScrapperBuilder extractNames() throws Exception {
        comprobations();
        String namesXpath = "//div[contains(@class, 'listing__title--wrap')]//h3[contains(@class, 'listing__name')]//a[contains(@class, 'listing__name--link')]";
        List<WebElement> names = driver.findElements(By.xpath(namesXpath));
        fillDataWith(names, "name");

        return this;
    }

    public ScrapperBuilder extractAddresses() throws Exception {
        comprobations();
        String addressesXpath = "//div[contains(@class, 'listing__address')]//span[contains(@class, 'listing__address--full')]";
        List<WebElement> addresses = driver.findElements(By.xpath(addressesXpath));
        fillDataWith(addresses, "address");

        return this;
    }

    public ScrapperBuilder extractPhones() throws Exception {
        comprobations();
        // the phones are into a menu, therefore, i must click in each one for display them (otherwise throws "")
        String btnPhonesXpath = "//li[contains(@class, 'mlr__item--phone')]";
        List<WebElement> btnsPhones = driver.findElements(By.xpath(btnPhonesXpath));
        for (WebElement btnPhone: btnsPhones) {
            btnPhone.click();
        }
        // now, the same as before....
        String phonesXpath = "//li[contains(@class, 'mlr__item--phone')]//ul[contains(@class, 'mlr__submenu')]";
        List<WebElement> phones = driver.findElements(By.xpath(phonesXpath));
        fillDataWith(phones, "phone");

        return this;
    }

    public ScrapperBuilder nextPage() {
        List<WebElement> btn = driver.findElements(By.xpath("//a[contains(@class, 'ypbtn') and contains(@class, 'btn-theme') and contains(@class, 'pageButton')]"));
        if (btn.size() == 1) {
            btn.get(0).click();
        } else {
            btn.get(1).click();
        }
        offset = data.size();

        return this;
    }

    public List<Map<String, String>> finish() {
        driver.quit();
        step = FINISHED;

        return data;
    }

    private void fillDataWith(List<WebElement> els, String key) {
        int i = offset;
        for (WebElement el: els) {
            if (!Piwi.indexExists(data, i)) {
                data.add(new HashMap<>());
            }
            data.get(i).put(key, el.getText());
            i++;
        }
    }
}

interface SeleniumTask {
    void piwi(WebDriver driver);
}
