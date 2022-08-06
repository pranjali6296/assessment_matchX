package com.MatchX.BaseClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.internal.TestNGMethod;

import com.MatchX.reports.ExtentReport;
import com.MatchX.utils.TestUtils;
import com.aventstack.extentreports.Status;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class BaseTest {
	protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	protected static ThreadLocal<Properties> props = new ThreadLocal<Properties>();
	protected static ThreadLocal<HashMap<String, String>> strings = new ThreadLocal<HashMap<String, String>>();
	protected static ThreadLocal<String> platform = new ThreadLocal<String>();
	protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
	protected static ThreadLocal<String> deviceName = new ThreadLocal<String>();
	private static AppiumDriverLocalService server;
	TestUtils utils = new TestUtils();

	public AppiumDriver getDriver() {
		return driver.get();
	}

	public void setDriver(AppiumDriver driver2) {//*

		driver.set(driver2);
	}

	public Properties getProps() {
		return props.get();
	}

	public void setProps(Properties props2) { // *
		props.set(props2);
	}

	public HashMap<String, String> getStrings() {
		return strings.get();
	}
	
//string is use to in extent report bcz we need print in log class 
	public void setStrings(HashMap<String, String> strings2) {
		strings.set(strings2);
	}

	public String getPlatform() { //*
		return platform.get();
	}

	public void setPlatform(String platform2) {//*
		platform.set(platform2);
	}

	public String getDateTime() {
		return dateTime.get();
	}

	public void setDateTime(String dateTime2) {
		dateTime.set(dateTime2);
	}

	public String getDeviceName() {
		return deviceName.get();
	}

	public void setDeviceName(String deviceName2) {
		deviceName.set(deviceName2);
	}
//contructor 
	public BaseTest() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}

	@BeforeMethod
	public void beforeMethod() {
		// ((CanRecordScreen) getDriver()).startRecordingScreen();
	}

	// stop video capturing and create *.mp4 file
	@AfterMethod
	public synchronized void afterMethod(ITestResult result) throws Exception {
		String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();

		Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
		String dirPath = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName")
				+ File.separator + getDateTime() + File.separator
				+ result.getTestClass().getRealClass().getSimpleName();

		File videoDir = new File(dirPath);

		synchronized (videoDir) {
			if (!videoDir.exists()) {
				videoDir.mkdirs();
			}
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
			stream.write(Base64.decodeBase64(media));
			stream.close();
			utils.log().info("video path: " + videoDir + File.separator + result.getName() + ".mp4");
		} catch (Exception e) {
			utils.log().error("error during video capture" + e.toString());
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	@BeforeSuite
	public void beforeSuite() throws Exception, Exception {

	}

	public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
		boolean isAppiumServerRunning = false;
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			socket.close();
		} catch (IOException e) {
			System.out.println("1");
			isAppiumServerRunning = true;
		} finally {
			socket = null;
		}
		return isAppiumServerRunning;
	}

	@AfterSuite
	public void afterSuite() {
		
	}

	public AppiumDriverLocalService getAppiumServerDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}

	@Parameters({ "emulator", "platformName", "udid", "deviceName" }) // "chromeDriverPort","systemPort",,"wdaLocalPort",
																		// "webkitDebugProxyPort"
	@BeforeTest
	public void beforeTest(@Optional("androidOnly") String emulator, String platformName, String udid,
			String deviceName) throws Exception {
		setDateTime(utils.dateTime());
		setPlatform(platformName);
		setDeviceName(deviceName);
		URL url;
		InputStream inputStream = null;
		InputStream stringsis = null;
		Properties props = new Properties();
		AppiumDriver driver;

		String strFile = "logs" + File.separator + platformName + "_" + deviceName;
		File logFile = new File(strFile);

		// route logs to separate file for each thread
		ThreadContext.put("ROUTINGKEY", strFile);
		utils.log().info("log path: " + strFile);

		try {
			props = new Properties();
			String propFileName = "config.properties";
			String xmlFileName = "strings/strings.xml";

			utils.log().info("load " + propFileName);
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			props.load(inputStream);
			setProps(props);

			utils.log().info("load " + xmlFileName);
			stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
			setStrings(utils.parseStringXML(stringsis));

			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability("platformName", platformName);
			desiredCapabilities.setCapability("deviceName", deviceName);
			desiredCapabilities.setCapability("udid", udid);

			url = new URL(props.getProperty("appiumURL"));

			switch (platformName) {
			case "Android":
				driver = new AndroidDriver(url, desiredCapabilities);
				desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
				desiredCapabilities.setCapability("autoGrantPermissions", true);
				desiredCapabilities.setCapability("newCommandTimeout", 150000);
				desiredCapabilities.setCapability("androidInstallTimeout", 150000);
				desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));
				desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
				desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
				String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
				utils.log().info("appUrl is" + androidAppUrl);
				desiredCapabilities.setCapability("app", androidAppUrl);

				break;
			default:
				throw new Exception("Invalid platform! - " + platformName);
			}
			setDriver(driver);
			utils.log().info("driver initialized: " + driver);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			utils.log().fatal("driver initialization failure. ABORT!!!\n" + e.toString());
			// e.printStackTrace(); // i m add extra code
			throw e;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (stringsis != null) {
				stringsis.close();
			}
		}
	}

	public void waitForVisibility(MobileElement e) {
		WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.WAIT);
		wait.until(ExpectedConditions.visibilityOf(e));
	}

	public void waitForVisibility(WebElement e) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(120))
				.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);

		Wait<WebDriver> wait1 = new FluentWait<WebDriver>(getDriver()).withTimeout(100, TimeUnit.SECONDS)
				.pollingEvery(600, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOf(e));
	}

	public static void scrollDown() {
		Dimension dimension = driver.get().manage().window().getSize();
		int scrollStart = (int) (dimension.getHeight() * 0.5);
		int scrollEnd = (int) (dimension.getHeight() * 0.2);

		new TouchAction((PerformsTouchActions) driver.get()).press(PointOption.point(0, scrollStart))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(0, scrollEnd))
				.release().perform();
	}

	public static String randomestring() {
		String generatedAlphabetic = RandomStringUtils.randomAlphabetic(4);
		return generatedAlphabetic;
	}

	public static String randomeNum() {
		String generatedrandomNumeric = RandomStringUtils.randomNumeric(5);
		return (generatedrandomNumeric);
	}

	public void clear(MobileElement e) {
		waitForVisibility(e);
		e.clear();
	}

	public void click(MobileElement e) {
		waitForVisibility(e);
		e.click();
	}

	public void click(MobileElement e, String msg) {
		waitForVisibility(e);
		utils.log().info(msg);
		ExtentReport.getTest().log(Status.INFO, msg);
		e.click();
	}

	public void sendKeys(MobileElement e, String txt) {
		waitForVisibility(e);
		e.sendKeys(txt);
	}

	public void sendKeys(MobileElement e, String txt, String msg) {
		waitForVisibility(e);
		utils.log().info(msg);
		ExtentReport.getTest().log(Status.INFO, msg);
		e.sendKeys(txt);
	}

	public String getAttribute(MobileElement e, String attribute) {
		waitForVisibility(e);
		return e.getAttribute(attribute);
	}

	public String getText(MobileElement e, String msg) {
		String txt = null;
		switch (getPlatform()) {
		case "Android":
			txt = getAttribute(e, "text");
			break;
		}
		utils.log().info(msg + txt);
		ExtentReport.getTest().log(Status.INFO, msg);
		return txt;
	}

	public void closeApp() {
		((InteractsWithApps) getDriver()).closeApp();
		getDriver().close();
		
	}

	public void launchApp() {
		getDriver().launchApp();
		
	}

	public MobileElement scrollTo(String text)
	{                
		return (MobileElement) ((FindsByAndroidUIAutomator) getDriver())
				.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""+text+"\").instance(0))");
	}
	
	
	@AfterTest
	public void afterTest() {
		getDriver().quit();
	}
}
