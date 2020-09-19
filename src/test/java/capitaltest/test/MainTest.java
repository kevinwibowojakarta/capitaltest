package capitaltest.test;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class MainTest {
	public AndroidDriver<MobileElement> driver;
	public WebDriverWait wait;
	public int screenHeight;
	public int screenWidth;
	
	@BeforeSuite
	public void initiate() throws IOException {
		//Initiate the Android Emulator
		//please make sure the emulator command is from ${ANDROID_SDK}/emulator not ${ANDROID_SDK_ROOT}/tools
		Runtime.getRuntime().exec(new String[]{"bash", "-l", "-c", "emulator -avd capital_test -no-snapshot-load -no-window"});
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
		System.out.println("Test 1 Start");
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
		//Move the slider to 0
		ts.tap(PointOption.point(slider_location_x, (slider_location_y+(slider_size_y/2)))).perform();
		//Move the slider from 0 to 100 in 10 second
		ts.longPress(PointOption.point(slider_location_x, (slider_location_y+(slider_size_y/2))))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(10)))
		.moveTo(PointOption.point(slider_location_x+slider_size_x, (slider_location_y+(slider_size_y/2))))
		.release().perform();
		//Close the Quick Setting
		endX = (int) (this.screenWidth * 0.5);
		endY = (int) (this.screenHeight -1);
		ts.tap(PointOption.point(endX, endY)).perform();
		
		System.out.println("Test 1 Done");
		
	}
	@Test(description="Turn on Airplane Mode")
	public void test2() throws InterruptedException, IOException {
		
		System.out.println("Test 2 Start");
		// Since Android 7 Airplane mode Toggle can't be accessed by non-system app (except on rooted phone)
		//to replicate airplane mode (on non rooted phone) following steps is reproduced:
		
		//Toggle Airplane mode icon
		Runtime.getRuntime().exec(new String[]{"bash", "-l", "-c", "adb shell settings put global airplane_mode_on 1"});
		//Turn Wifi Off
		Runtime.getRuntime().exec(new String[]{"bash", "-l", "-c", "adb shell svc data disable"});
		//Turn Data Off
		Runtime.getRuntime().exec(new String[]{"bash", "-l", "-c", "adb shell svc wifi disable"});
		System.out.println("Test 2 Done");
		
	}
	
	@Test(description="Add Emergency Contact")
	public void test3() throws InterruptedException, IOException {
		System.out.println("Test 3 Start");
		//Close the driver from previous App
		driver.closeApp();
		driver.quit();
		//Re-initiate driver with new phonebook app
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "capital_test");
		
		caps.setCapability("platformName","Android");
		caps.setCapability("platformVersion", "7.0");
		caps.setCapability("skipUnlock", "true");
		caps.setCapability("appPackage", "com.android.contacts");
		caps.setCapability("appActivity", "com.android.contacts.activities.PeopleActivity");
		caps.setCapability("automationName", "uiautomator2");
		
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
		wait = new WebDriverWait(driver, 30);
		//Click Add new Contact Button
		waitUntilVisible(By.id("com.android.contacts:id/floating_action_button"));
		driver.findElementById("com.android.contacts:id/floating_action_button").click();
		//Click Keep Local Button
		waitUntilVisible(By.id("com.android.contacts:id/left_button"));
		driver.findElementById("com.android.contacts:id/left_button").click();
		//Fill Name (first Edit Text Box)
		waitUntilVisible(By.className("android.widget.EditText"));
		
		driver.findElements(By.className("android.widget.EditText")).get(0).sendKeys("Emergency");;
		//Fill phone
		
		driver.findElements(By.className("android.widget.EditText")).get(1).sendKeys("911");
		//click save
		driver.findElementById("com.android.contacts:id/menu_save").click();
		
	
		System.out.println("Test 3 Done");
		
	}
	
	@Test(description="Change Date")
	public void test4() throws InterruptedException, IOException {
		//Setting Date via adb is only available to rooted phone
		//this test assume date is change via settings
		//There is also a limit of date by the OS, so it can't be changed to 1900, instead this will change to 1990
		
		System.out.println("Test 4 Start");
		//Close the driver from previous App
		driver.closeApp();
		driver.quit();
		//Re-initiate driver with setting
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "capital_test");
		
		caps.setCapability("platformName","Android");
		caps.setCapability("platformVersion", "7.0");
		caps.setCapability("skipUnlock", "true");
		caps.setCapability("appPackage", "com.android.settings");
		caps.setCapability("appActivity", "com.android.settings.Settings");
		caps.setCapability("automationName", "uiautomator2");
		
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
		wait = new WebDriverWait(driver, 30);
		
		//Click Search bar
		waitUntilVisible(By.id("com.android.settings:id/search"));
		driver.findElementById("com.android.settings:id/search").click();
		//Search for Date and Time Setting
		waitUntilVisible(By.id("android:id/search_src_text"));
		driver.findElementById("android:id/search_src_text").sendKeys("Date");
		//Click "Set Date"
		waitUntilVisible(By.xpath("//*[@text='Set date']"));
		driver.findElementByXPath("//*[@text='Set date']").click();
		//Check if Automatic time is on, if it is on, toggle it off
		waitUntilVisible(By.className("android.widget.Switch"));	
		WebElement switch1 = driver.findElements(By.className("android.widget.Switch")).get(0);
		if(switch1.getAttribute("checked").equals("true")) {
			switch1.click();
		}
		//Click "Set date" options
		waitUntilVisible(By.xpath("//*[@text='Set date']"));
		driver.findElementByXPath("//*[@text='Set date']").click();
		
		//Click the year and change it to 1900
		waitUntilVisible(By.id("android:id/date_picker_header_year"));
		driver.findElementById("android:id/date_picker_header_year").click();
		
		if(Integer.parseInt(driver.findElementById("android:id/date_picker_header_year").getText())>1990)
		{swipeDownUntilElementFound(By.xpath("//*[@text='1990']"));}
		else if(Integer.parseInt(driver.findElementById("android:id/date_picker_header_year").getText())<1990)
		{swipeUpUntilElementFound(By.xpath("//*[@text='1990']"));}
		
		
		driver.findElementByXPath("//*[@text='1990']").click();
		
		//Change the month by clicking previous until january Shows
		while (this.driver.findElements(By.xpath("//android.view.View[@content-desc=\"01 January 1990\"]")).size() == 0) {
			int iter = 0;
			driver.findElementById("android:id/prev").click();
			iter++;
			if (iter > 19) {
				new Exception("Element not found after 20 time clicking");
				break;
			}
		}
		driver.findElementByXPath("//android.view.View[@content-desc=\"01 January 1990\"]").click();
		//Click Ok
		driver.findElementById("android:id/button1").click();
		
		//Click "Set time" options
		waitUntilVisible(By.xpath("//*[@text='Set time']"));
		driver.findElementByXPath("//*[@text='Set time']").click();
		//Click the time and send keyboard to change it to 11.59
		waitUntilVisible(By.id("android:id/hours"));
		driver.findElementById("android:id/hours").click();
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_1));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_1));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_5));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_9));
		
		//Change to PM
		waitUntilVisible(By.id("android:id/pm_label"));
		driver.findElementById("android:id/pm_label").click();
		//Click OK
		driver.findElementById("android:id/button1").click();
		

		System.out.println("Test 4 Done");
	}
	
	
	@AfterSuite
	public void cleanup() {
		try {
			Thread.sleep(5000);
			driver.closeApp();
			driver.quit();}
		catch (Exception e) {}
	}
	
	//method for waiting an element until visible
	public void waitUntilVisible(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	//method for searching the scrolling screen
	public void swipeDownUntilElementFound(By by) {
		TouchAction ts = new TouchAction(this.driver);
		int startX = (int) (this.screenWidth * 0.5);
		int startY = (int) (this.screenHeight * 0.4);
		int endX = (int) (this.screenWidth * 0.5);
		int endY = (int) (this.screenHeight * 0.75);
		int iter = 0;
		while (this.driver.findElements(by).size() == 0) {
			ts.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX, endY)).release().perform();
			iter++;
			if (iter > 19) {
				new Exception("Element not found after 20 time scrolling or Something wrong happened with scrolling");
				break;
			}
		}
	}
	
	//method for searching the scrolling screen
		public void swipeUpUntilElementFound(By by) {
			TouchAction ts = new TouchAction(this.driver);
			int startX = (int) (this.screenWidth * 0.5);
			int startY = (int) (this.screenHeight * 075);
			int endX = (int) (this.screenWidth * 0.5);
			int endY = (int) (this.screenHeight * 0.4);
			int iter = 0;
			while (this.driver.findElements(by).size() == 0) {
				ts.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX, endY)).release().perform();
				iter++;
				if (iter > 19) {
					new Exception("Element not found after 20 time scrolling or Something wrong happened with scrolling");
					break;
				}
			}
		}
		
	
	
}
