package testcases;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.BaseTest;
import utilities.ExcelUtility;

public class LoginPage extends BaseTest {
	
	// DataProvider to load data from Excel
    @DataProvider(name = "loginData")
    public Object[][] loginData() throws IOException {
        // Load the Excel file
        ExcelUtility.loadExcel("E:\\OrengeHRM.xlsx");

        int rowCount = ExcelUtility.getRowCount(0) - 1;  // Subtract 1 to skip the header row
        Object[][] data = new Object[rowCount][2]; // Two columns for username and password

        // Load data for the test cases, starting from the second row (index 1)
        for (int i = 1; i <= rowCount; i++) { // Start from index 1 to skip the header row
            data[i - 1][0] = ExcelUtility.getCellData(0, i, 0); // Username column
            data[i - 1][1] = ExcelUtility.getCellData(0, i, 1); // Password column
        }

        return data;
    }
    
    @BeforeMethod
    public void initializeBrowser() throws IOException {
        // Initialize the browser before each test case
        if (driver == null) {
            setUp();  // Calling the setUp() method from BaseTest to initialize the WebDriver
        }
    }

    // Test method that will run for each set of data provided by the DataProvider
    @Test(dataProvider = "loginData")
    public void validLoginTest(String username, String password) {
        // Locate username and password fields
        test.info("Entering username: " + username);
        WebElement ele1 = driver.findElement(By.xpath(loc.getProperty("UserName_Field")));
        
        test.info("Entering password: " + password);
        WebElement ele2 = driver.findElement(By.xpath(loc.getProperty("Password_Field")));

        // Enter credentials from Excel
        ele1.sendKeys(username);
        ele2.sendKeys(password);

        // Locate and click the login button 
        WebElement loginButton = driver.findElement(By.xpath(loc.getProperty("Login_Button")));
        loginButton.click();
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // First, check if the dashboard header is displayed (for valid users)
        WebElement dashboardHeader = null;
        try {
            dashboardHeader = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loc.getProperty("Dashboard_text"))));
        } catch (Exception e) {
            // This will be null if login fails (invalid user), handle this case later
        }

        // **Assertion for Valid User**: Check page title and URL for valid login
        if (username.equals("validUser")) {
            // Check the title for successful login
            String actualTitle = driver.getTitle();
            String expectedTitle = "OrangeHRM"; // Adjust this according to your application
            Assert.assertEquals(actualTitle, expectedTitle, "Title mismatch: Expected '" + expectedTitle + "' but found '" + actualTitle + "'");
            test.pass("Page title is correct for valid user.");

            // Assert that the dashboard header is displayed
            Assert.assertTrue(dashboardHeader != null && dashboardHeader.isDisplayed(), "Dashboard header is not displayed!");
            test.pass("Dashboard header is displayed for valid user.");
        }
        // **Assertion for Invalid User**: Check error message or URL change for invalid login
        else if (username.equals("invalidUser")) {
            // For an invalid user, the login will fail, so verify the error message or page behavior
            WebElement errorMessage = driver.findElement(By.xpath(loc.getProperty("Error_Message")));
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed for invalid login!");
            test.pass("Error message displayed for invalid user.");

            // Optionally, check if the URL has not changed to the dashboard
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("dashboard"), "URL should not contain 'dashboard' for invalid login.");
            test.pass("URL is correct (not dashboard) for invalid user.");
        }
       
     }
        
        @AfterMethod
        public void tearDown() {
            // Quit the browser after each test
            if (driver != null) {
                driver.quit();
                driver = null; 
            }
        }

    }
