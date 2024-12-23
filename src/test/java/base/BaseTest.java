package base;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.lang.reflect.Method;

public class BaseTest {

    public static WebDriver driver;
    public static Properties prop = new Properties();
    public static Properties loc = new Properties();
    public static FileReader fr;
    public static FileReader fr1;

    // Extent Reports instances
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeTest
    public void setUp() throws IOException {
        // Initialize Extent Reports
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Automation Test Report");
        spark.config().setDocumentTitle("Test Report");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        
  

        // Load properties files
        fr = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\configfiles\\config.properties");
        fr1 = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\configfiles\\locators.properties");
        prop.load(fr);
        loc.load(fr1);

        // Browser setup
        String browser = prop.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(prop.getProperty("headless", "false")); // Default is non-headless

        if (browser.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            if (headless) {
                chromeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
            }
            driver = new ChromeDriver(chromeOptions);
        } else if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if (headless) {
                firefoxOptions.addArguments("--headless");
            }
            driver = new FirefoxDriver(firefoxOptions);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        // Set the implicit wait from config properties
        int implicitWait = Integer.parseInt(prop.getProperty("implicit_wait", "10")); // Default to 10 seconds if not specified
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // Navigate to the test URL
        driver.get(prop.getProperty("testurl"));
    }

        
	@BeforeMethod
    public void startTest(Method method) {
        // Initialize ExtentTest for each test method
        test = extent.createTest(method.getName());  // Create a test with the method name
    }

 
    @AfterMethod
    public void logTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            test.fail("Test Failed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test Skipped");
        }
    }


    @AfterTest
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }

        // Flush Extent Reports
        if (extent != null) {
            extent.flush();
        }

        System.out.println("Teardown completed");
    }
}
