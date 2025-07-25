package tests;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.DriverFactory;

import java.io.ByteArrayInputStream;


public class TestListener implements ITestListener {


    @Override
    public void onTestFailure(ITestResult result) {
        takeScreenshot("FAILED_" + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        takeScreenshot("PASSED_" + result.getName());
    }

    private void takeScreenshot(String name) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            try {
                TakesScreenshot ts = (TakesScreenshot) driver;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
                System.out.println("Screenshot taken: " + name);
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        } else {
            System.err.println("Driver is null, cannot take screenshot.");
        }
    }

    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
