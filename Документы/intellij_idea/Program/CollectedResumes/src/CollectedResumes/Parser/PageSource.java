package CollectedResumes.Parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.util.List;

/**
 * This class is implemented in order to run phantomJS and get the source code of the page.
 */
class PageSource {

    private static final String URL = "https://zarplata.ru";

//    private WebDriver phantomJSDriver;
    private WebDriver phantomJSDriver;

    /**
     * Returns the source code of the page with the given number
     *
     * @throws IllegalArgumentException - page must not be less than zero
     * @throws IllegalArgumentException - source code not be NULL
     * @param page - the page number
     * @return - source code of the page
     */
    String resume(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must not be less than zero");
        }

        String pageSource = null;

        try {
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");

            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(firefoxBinary);
//            phantomJSDriver = new PhantomJSDriver();
            phantomJSDriver = new FirefoxDriver(firefoxOptions);

            phantomJSDriver.get(URL + "/resume?offset=" + page * 25);

            List<WebElement> webElement = phantomJSDriver.findElements(By.xpath("//button[@class='ui tiny basic button']"));
            webElement.forEach(WebElement::click);

            pageSource = phantomJSDriver.getPageSource();
        } catch (Exception e) {
            close();
        }

        if (pageSource == null || pageSource.length() == 0) {
            throw new IllegalArgumentException("Source code not be NULL");
        }

        return pageSource;
    }

    /**
     * Closes phantomJS for that would not have hung in the process
     */
    void close() {
        try {
            phantomJSDriver.close();
            phantomJSDriver.quit();
        } catch (Exception e) {
            System.out.println(e + " failed quit.");
        }
    }

}
