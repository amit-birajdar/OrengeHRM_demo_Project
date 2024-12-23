package testcases;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import base.BaseTest;

public class MyFirstTestFw extends BaseTest {

    @Test
    public void LoginTest() throws InterruptedException {
        // Create a test in the Extent Report
        test = extent.createTest("Login Test");
        
        // Log the start of the test
        test.info("Starting the Login Test");
        
        Thread.sleep(2000);
        test.info("Entering username");
        driver.findElement(By.id(loc.getProperty("email_field"))).sendKeys("ABC");
        
        Thread.sleep(2000);
        test.info("Entering password");
        driver.findElement(By.name(loc.getProperty("password_field"))).sendKeys("ABC");
        
        test.pass("Login Test executed successfully");
    }
}
