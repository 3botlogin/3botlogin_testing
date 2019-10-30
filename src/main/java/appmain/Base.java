package appmain;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;


public class Base {
	
	public static AppiumDriverLocalService appiumService;
	public static AppiumDriver<MobileElement> driver;
	public static Logger logger;
	public static Properties config;
	private File globalPropFile;

	@BeforeSuite
	public void stopAppiumServerBeforeSuite() throws IOException {
		File globalPropDir = new File("src");
		globalPropFile = new File(globalPropDir, "global.properties");
		//delete any local appium instance running before running tests
		boolean flag = checkIfServerIsRunnning(4723);
		if (flag) {
			stopServer();
			sleep(5000);
		}
		getConfig();
		log();
	}

	public void getConfig() throws IOException {
		FileInputStream file = new FileInputStream(globalPropFile.getAbsolutePath());
		config = new Properties();
		config.load(file);
	}

	public void saveConfig() throws IOException {
		File globalPropDir = new File("src");
		globalPropFile = new File(globalPropDir, "global.properties");
		OutputStream output = new FileOutputStream(globalPropFile.getAbsolutePath());
		config.store(output, null);
	}

	public static AppiumDriver<MobileElement> Capabilities(Boolean app, Boolean noReset) throws IOException {
		/* @param app       if True, get app capabilities .. if False, get web capabilities
		 * @param noReset   if True, don't reset app or web
		 */
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.TAKES_SCREENSHOT,"True");
		if (app) {
			File appDir = new File("src");
			File appApk = new File(appDir, (String) config.get("appName"));
			capabilities.setCapability(MobileCapabilityType.APP, appApk.getAbsolutePath());
		}
		else {
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME,"Chrome");

		}

		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, (String) config.get("device"));
		capabilities.setCapability(MobileCapabilityType.NO_RESET, noReset);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2"); //uiautomator2 this gives error understand why
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;

	}

	public void log() {
		logger= Logger.getLogger("Log4j_Demo");
        File appDir = new File("src");
        File app = new File(appDir, "log4j.properties");
        PropertyConfigurator.configure(app.getAbsolutePath());
        System.out.println(app.getAbsolutePath());

	}
	
	public AppiumDriverLocalService startServer() throws IOException {
		// use this when u will just run using mvn test command (not from inside the IDE)
		// appiumService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingPort(4723));
		boolean flag = checkIfServerIsRunnning(4723);
		if (!flag) {
			appiumService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
					.usingDriverExecutable(new File((String) config.get("node_bin")))
					.withAppiumJS(new File((String) config.get("appium_main")))
					.usingPort(4723));
			appiumService.start();
		}
			return appiumService;
	}
	
	public void stopServer() {
	    Runtime runtime = Runtime.getRuntime();
	    try {
	        runtime.exec("killall node");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static boolean checkIfServerIsRunnning(int port) {
		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			
			serverSocket.close(); 
			} 
		catch (IOException e) {
			//If control comes here, then it means that the port is in use
			isServerRunning = true;
			} 
		finally {
			serverSocket = null;
			}
		return isServerRunning;
		}

	public static void waitAndClick(WebElement ele){
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(ele));
		ele.click();

	}

	public void waitTillTextBePresent(WebElement ele, String text){
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.textToBePresentInElement(ele, text));
		ele.click();

	}


	public static void sleep(int ms) {
	    try {
	        Thread.sleep(ms);
	    } catch (InterruptedException e) {
	        System.err.format("IOException: %s%n", e);
	    }
	}

	public static String generateStringOfWords(int wordsNumbers){
		String s = RandomStringUtils.randomAlphanumeric(5);
		for (int i=1; i < wordsNumbers; i++){
			s += " ";
			s += RandomStringUtils.randomAlphanumeric(5);
		}
		return s;
	}

	public static String randString(){
		return RandomStringUtils.randomAlphanumeric(10);
	}

	public static void switchToApp(AppiumDriver<MobileElement> appDriver){
		String appPackage = (String) config.get("appPackage");
		String appActivity = (String) config.get("appActivity");
		((AndroidDriver) appDriver).startActivity(new Activity(appPackage,appActivity));
	}

}