package capitaltest.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class MainTest {
	public AndroidDriver<MobileElement> driver;
	public WebDriverWait wait;
	public int screenHeight;
	public int screenWidth;
	
	@BeforeSuite
	public void initiate() throws IOException {
		//Initiate the AndroidDriver
		
		
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
		screenHeight = driver.manage().window().getSize().getHeight();
		screenWidth = driver.manage().window().getSize().getWidth();

	}
	
	@Test(description="Adjust Brightness")
	public void test1() {
		//Open Quick Setting
		TouchAction ts = new TouchAction(this.driver);
		int startX = (int) (this.screenWidth * 0.5);
		int startY = (int) (1);
		int endX = (int) (this.screenWidth * 0.5);
		int endY = (int) (this.screenHeight * 0.5);
		ts.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX,endY)).release().perform();
		//Open Expanded Quick Setting
		ts.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX,endY)).release().perform();
		//Get the slider element
		MobileElement brightness_slider = driver.findElementById("com.android.systemui:id/slider");
		//Calculate the distance
		int slider_location_x=brightness_slider.getLocation().getX();
		int slider_location_y=brightness_slider.getLocation().getY();
		int slider_size_x=brightness_slider.getSize().getWidth();
		int slider_size_y=brightness_slider.getSize().getHeight();
		//Calculate the coordinate
		System.out.println(slider_location_x);
		System.out.println(slider_location_y);
		System.out.println(slider_size_x);
		System.out.println((slider_location_y+slider_size_y)/2);
		//Move the slider to 0
		ts.tap(PointOption.point(slider_location_x, (slider_location_y+(slider_size_y/2)))).perform();
		//Move the slider from 0 to 100 in 10 second
		ts.longPress(PointOption.point(slider_location_x, (slider_location_y+(slider_size_y/2)))).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(10)))
		.moveTo(PointOption.point(slider_location_x+slider_size_x, (slider_location_y+(slider_size_y/2)))).release().perform();
		
		
		System.out.println("Test 1 Done");
		driver.quit();
	}
	
	
	@AfterSuite
	public void cleanup() {}
	
	
	
	
}
