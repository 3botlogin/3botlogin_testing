package appmain;

import org.testng.annotations.*;
import org.testng.Assert;
import java.io.IOException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import pageObjects.app.pinCodePage;
import pageObjects.app.resourceAccessPage;
import pageObjects.web.loginPage;
import java.lang.reflect.Method;

public class signIn extends Base{
    AndroidDriver<AndroidElement> appDriver;
    AndroidDriver<AndroidElement> webDriver;
    pinCodePage pinCodePage;
    loginPage loginPage;
    resourceAccessPage resourceAccessPage;

    @BeforeClass
    public void beforeClass() throws IOException {
        // if u removed the drivers from here i think u will get null pointer

    }

	@BeforeMethod
	public void setUp(Method method) throws IOException {
		logger.info("Running Test : " + method.getName());
		appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE);
        webDriver = Capabilities(Boolean.FALSE);
        pinCodePage = new pinCodePage(appDriver);
        loginPage = new loginPage(webDriver);
        resourceAccessPage = new resourceAccessPage(appDriver);
		}
	
	@AfterMethod
	public void tearDown(Method method) {
        logger.info("End of Test : " + method.getName());
        appiumService.stop();
		}

    @Test
    public void webSignInRegistered() throws IOException {
        String website = (String) config.get("website");
        logger.info("Get Website: " + website);
        webDriver.get(website);

        logger.info("Click Sign in/up button, should succeed");
        loginPage.signInButton.click();

        logger.info("Click 3bot Login option, should succeed");
        loginPage._3botLoginOption.click();

        logger.info("Provide 3bot name then press Sign in, should succeed");
        loginPage.nameField.sendKeys((String) config.get("registeredUser"));
        waitAndClick(loginPage._3botSignInButton);

        logger.info("Provide username pin code and press OK, should succeed");
        pinCodePage.oneButton.click();
        pinCodePage.twoButton.click();
        pinCodePage.threeButton.click();
        pinCodePage.fourButton.click();
        pinCodePage.OKButton.click();

        logger.info("Press Ok, should succeed");
        resourceAccessPage.OkButton.click();

        logger.info("Verify that you are logged in by checking if there " +
                    "is My Spaces menu, should be found" + "\n");
        Assert.assertTrue(loginPage.mySpacesMenu.isDisplayed());

    }


}
