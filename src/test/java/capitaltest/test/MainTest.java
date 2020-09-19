package capitaltest.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class MainTest {
	public AndroidDriver<MobileElement> driver;
	public WebDriverWait wait;
	
	@BeforeSuite
	public void initiate() throws IOException {
		//Initiate the AndroidDriver
		Runtime.getRuntime().exec(new String[]{"bash", "-l", "-c", "emulator -avd capital_test"});
		
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "capital_test");
		
		caps.setCapability("platformName","Android");
		caps.setCapability("platformVersion", "7.0");
		caps.setCapability("skipUnlock", "true");
		caps.setCapability("appPackage", "com.android.calculator2");
		caps.setCapability("appActivity", "com.android.calculator2.Calculator");
		caps.setCapability("automationName", "uiautomator2");
		
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
		wait = new WebDriverWait(driver, 30);

	}
	
	@Test
	public void singleTest() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("opened the app");
		driver.quit();
	}
	
	
	@AfterSuite
	public void cleanup() {}
	
	
	
	
}
