package appmain;

import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.Assert;
import java.io.IOException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import pageObjects.app.pinCodePage;
import pageObjects.app.resourceAccessPage;
import pageObjects.app.homePage;
import pageObjects.app.registerPage;
import pageObjects.web.loginPage;
import java.lang.reflect.Method;


public class signInTests extends Base{
    AndroidDriver<AndroidElement> appDriver;
    AndroidDriver<AndroidElement> webDriver;
    pinCodePage pinCodePage;
    loginPage loginPage;
    resourceAccessPage resourceAccessPage;
    homePage homePage;
    registerPage registerPage;
    

	@BeforeMethod
	public void setUp(Method method) throws IOException {
		logger.info("Running Test : " + method.getName());
		appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE);
        pinCodePage = new pinCodePage(appDriver);
        resourceAccessPage = new resourceAccessPage(appDriver);
        registerPage = new registerPage(appDriver);
        homePage = new homePage(appDriver);
		}
	
	@AfterMethod
	public void tearDown(Method method) {
        logger.info("End of Test : " + method.getName());
        appiumService.stop();
		}

	public void signInThroughWebFirstSteps() throws IOException {

        String website = (String) config.get("website");
        logger.info("Get Website: " + website);
        webDriver = Capabilities(Boolean.FALSE);
        loginPage = new loginPage(webDriver);
        webDriver.get(website);

        logger.info("Click Sign in/up button, should succeed");
        loginPage.signInButton.click();

        logger.info("Click 3bot Login option, should succeed");
        loginPage._3botLoginOption.click();

        logger.info("Provide 3bot name then press Sign in, should succeed");
        loginPage.nameField.sendKeys((String) config.get("registeredUser"));
        waitAndClick(loginPage._3botSignInButton);

    }

    @Test
    public void test1_webSignInRegistered() throws IOException {

	    // this test doesn't always pass because of this issue
        // https://github.com/3botlogin/3botlogin/issues/55
        signInThroughWebFirstSteps();

        logger.info("Provide username pin code and press OK, should succeed");
        pinCodePage.oneButton.click();
        pinCodePage.twoButton.click();
        pinCodePage.threeButton.click();
        pinCodePage.fourButton.click();
        pinCodePage.OKButton.click();

        logger.info("Press Accept for the website to access the app , should succeed");
        resourceAccessPage.acceptButton.click();

        logger.info("Verify that you are logged in by checking if there " +
                    "is My Spaces menu, should be found" + "\n");
        Assert.assertTrue(loginPage.mySpacesMenu.isDisplayed());

    }

    @Test
    public void test2_webSignInRegistered() throws IOException {

	    if (Boolean.TRUE) {
            throw new SkipException("https://github.com/3botlogin/3botlogin/issues/56");
        }

        signInThroughWebFirstSteps();

        logger.info("Provide wrong pin code and press OK, should fail");
        for (int i=0; i<4; i++) {
            pinCodePage.nineButton.click();
        }
        pinCodePage.OKButton.click();

        logger.info("Make sure you are still in pin code page");
        // write code here after issue is fixed


    }

    @Test
    public void test3_appSignInRegistered() throws IOException {

	    if (Boolean.TRUE) {
            throw new SkipException("https://github.com/3botlogin/3botlogin/issues/53");
        }
	    //make sure you are on registered user home page
	    logger.info("Press on the FreeFlowPages window, should succeed");
	    homePage.freeFlowWindow.click();

        logger.info("Verify that you are logged in by checking if there " +
                "is My Spaces menu, should be found" + "\n");
        Assert.assertTrue(loginPage.mySpacesMenu.isDisplayed());

    }


}
