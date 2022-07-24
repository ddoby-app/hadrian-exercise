package Base;

import PageObjects.LaserMeasurementObjects;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.log.Log;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.Optional;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class TestMethods {

    public ExtentReports report = new ExtentReports();
    public ExtentTest test;
    public ExtentTest node;
    public RemoteWebDriver driver;
    public BaseMethods baseMethods = new BaseMethods();
    public LaserMeasurementObjects lmo;
    public ExtentHtmlReporter extentHtmlReporter;
    public WebDriverWait wait;
    public SoftAssert sa = new SoftAssert();
    public TestrailMethods tm = new TestrailMethods();

    public static final int TRstatusCasePassed = new Integer(1);
    public static final int TRstatusCaseFailed = new Integer(5);


    @Parameters({"environment", "environmentLabel", "reportType", "extension"})
    @BeforeClass
    public void ReportSetup(@Optional("https://www.ophiropt.com/laser--measurement/laser-power-energy-meters/services/peak-power-calculator") String environment, @Optional ("QA") String environmentLabel, @Optional ("OC Links") String reportType, @Optional (".html") String extension) {
        extentHtmlReporter = new ExtentHtmlReporter(baseMethods.reportFilePath(baseMethods.project, environmentLabel, reportType, extension));
        System.setProperty("webdriver.chrome.driver", baseMethods.driverFilePath(baseMethods.project, "chromedriver.exe"));

        driver = new ChromeDriver();
        lmo = new LaserMeasurementObjects(driver);
        report.attachReporter(extentHtmlReporter);
        //driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @Parameters({"environmentLabel"})
    @AfterMethod(alwaysRun=true)
    public void afterMethod(ITestResult testResult, @Optional ("QA") String environmentLabel) {

        /*if (testResult.getStatus() == ITestResult.SUCCESS) {
            test.pass(testResult.getName() + " returns the correct response code.");*/
        if (testResult.getStatus() == ITestResult.FAILURE) {
            test.fail(MarkupHelper.createLabel(testResult.getName() + " - Test Case Failed", ExtentColor.RED));
            test.fail(MarkupHelper.createLabel(testResult.getThrowable() + " - Test Case Failed", ExtentColor.RED));
        } else if (testResult.getStatus() == ITestResult.SKIP) {
            test.warning(MarkupHelper.createLabel(testResult.getName() + " - Test Case should be taken into Consideration, but not Failed", ExtentColor.ORANGE));
        }

    }

    @AfterTest
    public void endReport(){
        report.flush();
    }

    @AfterClass
    public void closeDriver() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }


    // Test Methods: Pass specific Web Element for "element" and the element name for "elementName"
    public void clickElement (WebElement element, String elementName, String trid, String tcid) throws APIException, IOException {
        node = test.createNode("Verify ability to click " + elementName);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            sa.assertTrue(element.isSelected());
            node.pass("Able to click " + elementName + " successfully.");
            System.out.println("Test Passed for " + elementName);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, "Able to click " + elementName + " successfully.");
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("Unable to click the " + elementName + "web element");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, "Unable to click the " + elementName + "web element");
            System.out.println("Test Failed for " + elementName);
        }
    }

    public void sendTextToInputField (WebElement element, String textSent, String elementName, String trid, String tcid) throws APIException, IOException {
        node = test.createNode("Verify ability to send " + textSent + " to " + elementName);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.click();
            element.clear();
            element.sendKeys(textSent);
            sa.assertEquals(textSent, element.getText());
            node.pass("Able to click " + elementName + " successfully and type \"" + textSent + "\" in input Field.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, "Able to click " + elementName + " successfully and type \"" + textSent + "\" in input Field.");
            System.out.println("Test Passed for " + elementName + " and able to enter " + textSent);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("Unable to send keys " + textSent + " to the " + elementName + " input field.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, "Unable to send keys " + textSent + " to the " + elementName + " input field.");
            System.out.println("Test Failed for " + elementName + " and able to enter " + textSent);
        } catch (APIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectElementFromDropdown (WebElement element, String textSelect, String elementName, String trid, String tcid) throws APIException, IOException {
        node = test.createNode("Verify ability to select " + textSelect + " from " + elementName + " dropdown");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            Select select = new Select (element);
            select.selectByVisibleText(textSelect);
            sa.assertEquals(textSelect, element.getText());
            node.pass("Able to select \"" + textSelect + "\" successfully from the \"" + elementName + "\" dropdown.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, "Able to select \"" + textSelect + "\" successfully from the \"" + elementName + "\" dropdown.");
            System.out.println("Test Passed for " + elementName);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("Unable to select " + textSelect + "from the " + elementName + " dropdown.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, "Unable to select " + textSelect + "from the " + elementName + " dropdown.");
            System.out.println("Test Failed for " + elementName);
        }
    }

    public void elementSelected (WebElement element, String elementName, String trid, String tcid) throws APIException, IOException{
        node = test.createNode("Verify ability to check that " + elementName + " is selected");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            sa.assertTrue(element.isSelected());
            node.pass(elementName + " is Selected correctly");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, elementName + " is correctly selected in the UI.");
            System.out.println("Test Passed: " + elementName + " is selected.");
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail(elementName + " is NOT selected in the UI.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, elementName + " is NOT selected in the UI.");
            System.out.println("Test Failed for " + elementName);
        }

    }

    public void elementIsNotSelected (WebElement element, String elementName, String trid, String tcid) throws APIException, IOException{
        node = test.createNode("Verify ability to check that " + elementName + " is NOT selected");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            sa.assertFalse(element.isSelected());
            node.pass(elementName + " is NOT Selected correctly");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, elementName + " is correctly NOT selected in the UI.");
            System.out.println("Test Passed: " + elementName + " is NOT selected.");
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail(elementName + " is incorrectly selected in the UI.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, elementName + " is incorrectly selected in the UI.");
            System.out.println("Test Failed for " + elementName);
        }

    }

    public void elementIsDisplayed (WebElement element, String elementName, String trid, String tcid) throws APIException, IOException {
        node = test.createNode("Verify ability to check that " + elementName + " is displayed");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            sa.assertTrue(element.isDisplayed());
            node.pass(elementName + " is correctly displayed in the UI.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, elementName + " is correctly displayed in the UI.");
            System.out.println("Test Passed for " + elementName);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail(elementName + " is NOT  displayed in the UI.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, elementName + " is NOT  displayed in the UI.");
            System.out.println("Test Failed for " + elementName);

        }
    }

    public void elementIsNotDisplayed (WebElement element, String elementName, String trid, String tcid) throws APIException, IOException {
        node = test.createNode("Verify ability to check that " + elementName + " is NOT displayed");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
            sa.assertFalse(element.isDisplayed());
            node.pass(elementName + " is correctly NOT displayed in the UI.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, elementName + " is correctly NOT displayed in the UI.");
            System.out.println("Test Passed for " + elementName);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail(elementName + " is incorrectly displayed in the UI.");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, elementName + " is incorrectly displayed in the UI.");
            System.out.println("Test Failed for " + elementName);

        }
    }

    public void textDisplayedInElement (WebElement element, String textDisplayed, String elementName, String trid, String tcid) throws APIException, IOException{
        node = test.createNode("Verify ability to check that " + textDisplayed + " is Displayed in the " + elementName + " textbox");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(element, textDisplayed));
            sa.assertEquals(element.getText(), textDisplayed);
            node.pass("The text \"" + textDisplayed + "\" is displayed correctly in " + elementName);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, textDisplayed + " is correctly displayed in the UI.");
            System.out.println("Test Passed for " + elementName);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("The text \"" + textDisplayed + "\" is NOT displayed correctly in " + elementName);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, textDisplayed + " is NOT  displayed in the UI.");
            System.out.println("Test Failed for " + elementName);
        }
    }

    public void getAttributeFromElement (WebElement element, String attribute, String attributeText, String elementName, String trid, String tcid) throws APIException, IOException{
        node = test.createNode("Verify ability to check that " + attribute + " of " + elementName + " is correct.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            sa.assertTrue(element.isDisplayed());
            sa.assertEquals(element.getAttribute(attribute), attributeText);
            node.pass("The text \"" + attributeText + "\" is displayed correctly in " + elementName);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, attributeText + " is correctly displayed in the UI.");
            System.out.println("Test Passed for " + elementName);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("The text \"" + attributeText + "\" is NOT displayed correctly in " + elementName);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, attributeText + " is NOT  displayed in the UI.");
            System.out.println("Test Failed for " + elementName);
        }
    }

    public void verifyURL (String currentURL, String trid, String tcid) throws APIException, IOException  {
        node = test.createNode("Verify that " + currentURL + " is the current URL");
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlToBe(currentURL));
            sa.assertEquals(driver.getCurrentUrl(), currentURL);
            node.pass("The current correct URL is \"" + currentURL + "\"");
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, "The current correct URL is \"" + currentURL + "\"");
            System.out.println("Test Passed for " + currentURL);
            //consoleLogTest2(currentURL);
        } catch (TimeoutException ex){
            node.fail("Failed to verify current URL:" + currentURL);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, "Failed to verify current URL:" + currentURL);
            System.out.println("Test Failed for " + currentURL);
        }
    }

    public void verifyElementText (WebElement element, String elementName, String enteredText, String trid, String tcid) throws APIException, IOException {
        node = test.createNode("Verify that text in " + elementName + " is " + enteredText);
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(element, enteredText));
            sa.assertEquals(element.getText(), enteredText);
            node.pass("The text \"" + enteredText + "\" is displayed correctly in " + elementName);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCasePassed, "The text \"" + enteredText + "\" is displayed correctly in " + elementName);
            System.out.println("Test Passed for " + elementName);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("The text \"" + enteredText + "\" is NOT displayed correctly in " + elementName);
            tm.addTestrailResultForTestCase(trid,tcid, TRstatusCaseFailed, "The text \"" + enteredText + "\" is NOT displayed correctly in " + elementName);
            System.out.println("Test Failed for " + elementName);
        }
    }

    public void verifyTextMatch (WebElement elementOne, WebElement elementTwo, String elementTwoURL, String elementOneName, String elementTwoName) {
        node = test.createNode("Verify that the text from " + elementOneName + " matches the text from " +  elementTwoName + " in the URL: " + elementTwoURL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldTab = driver.getWindowHandle();
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(elementOne, elementOne.getText()));
            String elementOneText = elementOne.getText();
            System.out.println(elementOneText);
            RemoteWebDriver newTab = (RemoteWebDriver) driver.switchTo().newWindow(WindowType.TAB);
            newTab.get(elementTwoURL);
            wait.until(ExpectedConditions.textToBePresentInElement(elementTwo, elementTwo.getText()));
            String elementTwoText = elementTwo.getText();
            System.out.println(elementTwoText);
            sa.assertEquals(elementOneText,elementTwoText);
            node.pass("The text in \"" + elementOneName + "\" is the same as the text in \"" + elementTwoName + "\".");
            System.out.println("Test Passed for " + elementOneName + " matching " + elementTwoName + ".");
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("Failed to verify that the text in " + elementOneName + " matches the text in " + elementTwoName + ".");
            System.out.println("Test Failed for " + elementOneName + " matching " + elementTwoName + ".");
        } finally {
            driver.switchTo().window(oldTab);
        }
    }

    public void verifyTextMatch (WebElement elementOne, WebElement elementTwo, WebElement optionalStep, String elementTwoURL, String elementOneName, String elementTwoName) {
        node = test.createNode("Verify that the text from " + elementOneName + " matches the text from " +  elementTwoName + " in the URL: " + elementTwoURL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldTab = driver.getWindowHandle();

        try {
            String elementOneText = elementOne.getText();
            RemoteWebDriver newTab = (RemoteWebDriver) driver.switchTo().newWindow(WindowType.TAB);
            newTab.get(elementTwoURL);
            optionalStep.click();
            String elementTwoText = elementTwo.getText();
            sa.assertEquals(elementOneText,elementTwoText);
            node.pass("The text in \"" + elementOneName + "\" is the same as the text in \"" + elementTwoName + "\".");
            System.out.println("Test Passed for " + elementOneName + " matching " + elementTwoName + ".");
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ex) {
            node.fail("Failed to verify that the text in " + elementOneName + " matches the text in " + elementTwoName + ".");
            System.out.println("Test Failed for " + elementOneName + " matching " + elementTwoName + ".");
        } finally {
            driver.switchTo().window(oldTab);
        }
        driver.switchTo().window(oldTab);
    }


    public void consoleLogTest2(String url) {
        ChromeDriver driver = new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Log.enable());

        devTools.addListener(Log.entryAdded(), logEntry -> {
            System.out.println("-------------------------------------------");
            System.out.println("Request ID = " + logEntry.getNetworkRequestId());
            System.out.println("URL = " + logEntry.getUrl());
            System.out.println("Source = " + logEntry.getSource());
            System.out.println("Level = " + logEntry.getLevel());
            System.out.println("Text = " + logEntry.getText());
            System.out.println("Timestamp = " + logEntry.getTimestamp());
            System.out.println("-------------------------------------------");
        });
        driver.get(url);
    }


}
