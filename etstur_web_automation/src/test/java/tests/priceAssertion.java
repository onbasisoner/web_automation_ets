package tests;

import io.qameta.allure.*;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.BasePage;
import utils.DateUtil;
import utils.DriverFactory;

@Listeners(TestListener.class)
@Epic("Flight Search")
@Feature("Ucuzabilet Test")
@Test(groups = {"smoke"})
public class priceAssertion {

    private WebDriver driver;
    private BasePage homePage;
    private String priceOnTheList;
    private  String priceOnTheInfoPage;

    @Parameters("browser")
    @BeforeClass
    @Description("Setup browser and initialize BasePage")
    public void setup(String browser) {
        DriverFactory.initDriver(browser);
        driver = DriverFactory.getDriver();
        homePage = new BasePage(driver, "homepage.json");
    }
    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Open ucuzabilet homepage and verify title")
    public void openHomePage() {
        driver.get("https://www.ucuzabilet.com");
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Ucuzabilet"), "Title does not contain Ucuzabilet");
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Select airport location")
    public void fillWhereFromAndSelect() {
        homePage.click("whereFrom");
        homePage.fill("whereFrom", "Anta");
        homePage.waitForElementVisible("searchListFrom", 10);
        homePage.click("antaAirport");

    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Sselect destination airport")
    public void fillWhereToAndSelect() {

        homePage.click("whereTo");
        homePage.fill("whereTo", "Ankara");
        homePage.waitForElementVisible("searchListTo", 10);
        homePage.click("esbAirport");
    }


    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("Select tomorrow's date from date picker")
    public void selectTomorrowDate() {
        homePage.click("datePickerButton");
        homePage.waitForElementVisible("calendar",10);
        DateUtil.DateInfo tomorrow = DateUtil.getPlusDay(1);
        int monthForXpath = Integer.parseInt(tomorrow.month) - 1;

        String dateXpath = String.format(homePage.getLocatorValue("selectDate"), monthForXpath, tomorrow.year, tomorrow.day);
        homePage.clickByXPath(dateXpath);
    }
    @Test(priority = 5)
    @Description("Select passengers: 1 student, 0 adult")
    public void selectPassenger() {
        homePage.click("passengerChoiceList");
        homePage.increaseUntilTarget("studentCheckbox","studentIncrease",1);
        homePage.decreaseUntilZero("adultCheckbox","adultDecrease");
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Click on search button to fetch flight list")
    public void clickSearchButton() {
        homePage.click("searchButton");

    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Capture price from flight list")
    public void captureListPrice() {
        homePage.waitForElementVisible("priceLabel", 10);
        priceOnTheList = homePage.getAttribute("priceLabel", "data-price");
        System.out.println(priceOnTheList);
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Select flight and proceed to details")
    public void clickSelectFlightButton() {
        homePage.click("chooseFirstFlight");
        homePage.waitForElementVisible("basicSelectionButton",10);
        homePage.click("basicSelectionButton");
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Capture price from detail page")
    public void capturePriceOnInfoPage() {
        homePage.waitForElementVisible("totalPriceLabel", 20);
        priceOnTheInfoPage = homePage.getAttribute("totalPriceLabel", "data-price");
        System.out.println(priceOnTheInfoPage);

    }

    @Test(priority = 10)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify if both list and detail page prices are equal")
    public void verifyPricesAreEquals(){
        Assert.assertEquals(priceOnTheList,priceOnTheInfoPage, "Prices should match!");
    }

    @AfterClass
    @Description("Quit browser after test execution")
    public void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }
}
