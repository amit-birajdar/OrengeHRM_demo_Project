package testcases;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.Assert;
import base.BaseTest;
import utilities.ExcelUtility;

public class VerifyDashboardElements extends BaseTest {
	
	 @Test
	    public void verifyDashboardElements() throws IOException {
	        // Load credentials from Excel
	        ExcelUtility.loadExcel("E:\\OrengeHRM.xlsx");
	        String username = ExcelUtility.getCellData(0, 1, 0); // First row, first column
	        String password = ExcelUtility.getCellData(0, 1, 1); // First row, second column

	        // Log in with the credentials
	        test.info("Entering username: " + username);
	        WebElement ele1 = driver.findElement(By.xpath(loc.getProperty("UserName_Field")));
	        
	        test.info("Entering password: " + password);
	        WebElement ele2 = driver.findElement(By.xpath(loc.getProperty("Password_Field")));

	        ele1.sendKeys(username);
	        ele2.sendKeys(password);

	        WebElement loginButton = driver.findElement(By.xpath(loc.getProperty("Login_Button")));
	        loginButton.click();

	        new WebDriverWait(driver, Duration.ofSeconds(30));
	        
	     // Verify "Quick Launch" widget
	        test.info("Verifying visibility of 'Quick Launch' widget...");
	        WebElement quickLaunch = driver.findElement(By.xpath(loc.getProperty("Quick_Launch_Text")));
	        Assert.assertTrue(quickLaunch.isDisplayed(), "Quick Launch widget is not visible.");
	        test.info("'Quick Launch' widget is visible.");
	        test.pass("'Quick Launch' widget is visible.");

	        // Verify "Employee Distribution" widget
	        test.info("Verifying visibility of 'Employee Distribution' widget...");
	        WebElement employeeDistribution = driver.findElement(By.xpath(loc.getProperty("Employee_Distribution_Text")));
	        Assert.assertTrue(employeeDistribution.isDisplayed(), "Employee Distribution widget is not visible.");
	        test.info("'Employee Distribution' widget is visible.");
	        test.pass("'Employee Distribution' widget is visible.");


	        
	    }
	 

}
