package appmain;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import java.io.IOException;
import io.appium.java_client.MobileElement;
import pageObjects.app.pinCodePage;
import pageObjects.app.resourceAccessPage;
import pageObjects.app.homePage;
import pageObjects.app.registerPage;
import pageObjects.web.loginPage;
import utils.testsUtils;
import java.lang.reflect.Method;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class signInTests extends Base{
    AppiumDriver<MobileElement> appDriver;
    AppiumDriver<MobileElement> webDriver;
    pinCodePage pinCodePage;
    loginPage loginPage;
    resourceAccessPage resourceAccessPage;
    homePage homePage;
    registerPage registerPage;
    testsUtils testsUtils;


    @BeforeClass
    public void signInClassSetup() throws Exception {
        logger.info("Sign In Class Setup");
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.FALSE);
        testsUtils = new testsUtils(appDriver);
        testsUtils.registeringUSerCommonSteps();
        testsUtils.verifyEmail();
        //saveconfig
        config.setProperty("registeredUser", testsUtils.getUserName());
        config.setProperty("accountPhrase", testsUtils.getPhrase());
        saveConfig();
        appiumService.stop();
    }

	@BeforeMethod
	public void setUp(Method method) throws IOException {

		logger.info("Running Test : " + method.getName());
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.TRUE);
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

	public void signInThroughWebCommonSteps() throws IOException {

        String website = (String) config.get("website");
        logger.info("Get Website: " + website);
        webDriver = Capabilities(Boolean.FALSE, Boolean.TRUE);
        loginPage = new loginPage(webDriver);
        webDriver.get(website);

        logger.info("Click Sign in/up button, should succeed");
        loginPage.signInButton.click();

        logger.info("Choose 3bot Login option, should succeed");
        loginPage._3botLoginOption.click();

        logger.info("Provide 3bot name then press Sign in, should be redirected to the app");
        loginPage.nameField.sendKeys((String) config.get("registeredUser"));
        waitAndClick(loginPage._3botSignInButton);
    }

    @Test
    public void test1_webSignInRightPin() throws IOException {
        // TBL-003

        signInThroughWebCommonSteps();

        logger.info("Provide username correct pin code and press OK, should succeed");
        pinCodePage.oneButton.click();
        pinCodePage.twoButton.click();
        pinCodePage.threeButton.click();
        pinCodePage.fourButton.click();
        pinCodePage.OKButton.click();

        logger.info("Press Accept for the website to access the app , should succeed");
        resourceAccessPage.acceptButton.click();

        logger.info("Verify if the user is logged in by checking if there " +
                    "is My Spaces menu, should be found");
        Assert.assertTrue(loginPage.mySpacesMenu.isDisplayed());
    }

    @Test
    public void test2_webSignInWrongPin() throws IOException {
        // TBL-004

        signInThroughWebCommonSteps();

        logger.info("Provide wrong pin code and press OK, Sign in should fail");
        for (int i=0; i<4; i++) {
            pinCodePage.nineButton.click();
        }
        pinCodePage.OKButton.click();

        logger.info("Make sure you are still in pin code page");
        Assert.assertEquals(pinCodePage.loginText.getText(), "Login");


    }

    @Test
    public void test3_appSignIn() throws IOException {
        // TBL-005

        logger.info("Press on the FreeFlowPages window, should succeed");
        try{
            homePage.freeFlowWindow.click();
        }
        catch (NoSuchElementException e) {
            appDriver.navigate().back();
            homePage.freeFlowWindow.click();
        }

        // if permission question exists, press allow
        try {
            homePage.permissionAllow.click();
        }
        catch (NoSuchElementException e) {}

        logger.info("Verify that the web view is displayed inside the app");
        Assert.assertTrue(homePage.freeFlowWebView.isDisplayed());

    }


}
