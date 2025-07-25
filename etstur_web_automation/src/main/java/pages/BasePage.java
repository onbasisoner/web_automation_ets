package pages;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected String pageJsonFileName;
    protected WebDriverWait wait;
    private JSONArray elements;
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    public BasePage(WebDriver driver, String pageJsonFileName) {
        this.driver = driver;
        this.pageJsonFileName = pageJsonFileName;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loadElements(pageJsonFileName);
    }


    private void loadElements(String jsonFileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(jsonFileName))
        {
            if (is == null) {
                throw new RuntimeException("Resource file not found: " + jsonFileName);
            }
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            elements = new JSONArray(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load element JSON from resource: " + jsonFileName);
        }
    }


    private By getBy(String key) {
        for (int i = 0; i < elements.length(); i++) {
            JSONObject elem = elements.getJSONObject(i);
            if (elem.getString("key").equals(key)) {
                String type = elem.getString("type");
                String value = elem.getString("value");
                switch (type.toLowerCase()) {
                    case "id":
                        return By.id(value);
                    case "classname":
                        return By.className(value);
                    case "xpath":
                        return By.xpath(value);
                    case "css":
                        return By.cssSelector(value);
                    default:
                        throw new RuntimeException("Locator type not supported: " + type);
                }
            }
        }
        throw new RuntimeException("Element key not found in JSON: " + key);
    }



    public void click(String elementKey) {
        try {
            WebElement element = driver.findElement(getBy(elementKey));
            element.click();
            logger.info("Clicked on element: {}", elementKey);
        } catch (Exception e) {
            logger.error("Failed to click on element '{}': {}", elementKey, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void fill(String elementKey, String text) {
        try {
            WebElement element = driver.findElement(getBy(elementKey));
            element.clear();
            element.sendKeys(text);
            logger.info("Filled element '{}' with text: {}", elementKey, text);
        } catch (Exception e) {
            logger.error("Failed to fill element '{}': {}", elementKey, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void waitForElementVisible(String key, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            By locator = getBy(key);
            customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element '{}' is visible within {} seconds", key, timeoutSeconds);
        } catch (Exception e) {
            logger.error("Element '{}' not visible within {} seconds: {}", key, timeoutSeconds, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void selectDate(int daysFromToday) {
        try {
            DateUtil.DateInfo dateInfo = DateUtil.getPlusDay(daysFromToday);
            String xpath = String.format("//td[@data-month='%s' and @data-year='%s']/a[text()='%s']", dateInfo.month, dateInfo.year, dateInfo.day);
            WebElement dateElement = driver.findElement(By.xpath(xpath));
            dateElement.click();
            logger.info("Selected date: {}-{}-{}", dateInfo.day, dateInfo.month, dateInfo.year);
        } catch (Exception e) {
            logger.error("Failed to select date after {} days: {}", daysFromToday, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public String getLocatorValue(String key) {
        try {
            for (int i = 0; i < elements.length(); i++) {
                JSONObject elem = elements.getJSONObject(i);
                if (elem.getString("key").equals(key)) {
                    return elem.getString("value");
                }
            }
            throw new RuntimeException("Element key not found in JSON: " + key);
        } catch (Exception e) {
            logger.error("Error while getting locator value for '{}': {}", key, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void clickByXPath(String xpath) {
        try {
            By locator = By.xpath(xpath);
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            driver.findElement(locator).click();
            logger.info("Clicked on element by XPath: {}", xpath);
        } catch (Exception e) {
            logger.error("Failed to click on XPath '{}': {}", xpath, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.info("Waited for {} seconds", seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting for {} seconds: {}", seconds, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clearAndFill(String key, String text) {
        try {
            By locator = getBy(key);
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Cleared and filled '{}' with '{}'", key, text);
        } catch (Exception e) {
            logger.error("Failed to clear and fill '{}': {}", key, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getAttribute(String key, String attributeName) {
        try {
            By locator = getBy(key);
            WebElement element = driver.findElement(locator);
            String value = element.getAttribute(attributeName);
            logger.info("Attribute '{}' of element '{}' is '{}'", attributeName, key, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute '{}' from '{}': {}", attributeName, key, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void decreaseUntilZero(String checkboxKey, String decreaseButtonKey) {
        try {
            while (true) {
                String value = driver.findElement(getBy(checkboxKey)).getAttribute("value");
                int count = Integer.parseInt(value);
                if (count <= 0) break;
                click(decreaseButtonKey);
                logger.info("Decreased '{}'. Current value: {}", checkboxKey, count);
            }
        } catch (Exception e) {
            logger.error("Error while decreasing '{}': {}", checkboxKey, e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void increaseUntilTarget(String counterElementKey, String increaseButtonKey, int target) {
        try {
            while (true) {
                String value = driver.findElement(getBy(counterElementKey)).getAttribute("value");
                int count = Integer.parseInt(value);
                if (count >= target) break;
                click(increaseButtonKey);
                logger.info("Increased '{}'. Current value: {}", counterElementKey, count);
            }
        } catch (Exception e) {
            logger.error("Error while increasing '{}': {}", counterElementKey, e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
