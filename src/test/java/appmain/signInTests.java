package appmain;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import java.io.IOException;
import io.appium.java_client.MobileElement;
import pageObjects.app.PinCodePage;
import pageObjects.app.ResourceAccessPage;
import pageObjects.app.HomePage;
import pageObjects.web.LoginPage;
import utils.TestsUtils;
import java.lang.reflect.Method;
import org.testng.annotations.*;


public class signInTests extends Base{
    AppiumDriver<MobileElement> appDriver;
    AppiumDriver<MobileElement> webDriver;
    PinCodePage pinCodePage;
    LoginPage loginPage;
    HomePage homePage;
    TestsUtils testsUtils;


    @BeforeClass
    public void signInClassSetup() throws Exception {
        logger.info("Sign In Class Setup");
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.FALSE);
        testsUtils = new TestsUtils(appDriver);
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
        pinCodePage = new PinCodePage(appDriver);
        homePage = new HomePage(appDriver);
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
        loginPage = new LoginPage(webDriver);
        webDriver.get(website);

        logger.info("Click Sign in/up button, should succeed");
        loginPage.clickSignInButton();

        logger.info("Choose 3bot Login option, should succeed");
        loginPage.clickthreebotLoginOption();

        logger.info("Provide 3bot name then press Sign in, should be redirected to the app");
        loginPage.enter3botName((String) config.get("registeredUser"));
        loginPage.click3botSignInButton();
    }

    @Test
    public void test1_webSignInRightPin() throws IOException {
        // TBL-003

        signInThroughWebCommonSteps();

        logger.info("Provide username correct pin code and press OK, should succeed");
        ResourceAccessPage resAccessPage = pinCodePage.enterPinCode("1234");

        logger.info("Press Accept for the website to access the app , should succeed");
        resAccessPage.clickAccept();

        logger.info("Verify if the user is logged in by checking if there " +
                    "is My Spaces menu, should be found");
        Assert.assertTrue(loginPage.checkIfMySpaceMenuDisplayed());
    }

    @Test
    public void test2_webSignInWrongPin() throws IOException {
        // TBL-004

        signInThroughWebCommonSteps();

        logger.info("Provide wrong pin code and press OK, Sign in should fail");
        for (int i=0; i<4; i++) {
            pinCodePage.clickNumber("8");
        }
        pinCodePage.clickOkButton();

        logger.info("Make sure you are still in pin code page");
        Assert.assertEquals(pinCodePage.getLoginText(), "Login");


    }

    @Test
    public void test3_appSignIn() throws IOException {
        // TBL-005

        logger.info("Press on the FreeFlowPages window, should succeed");
        homePage.clickFreeFlowWindow();

        // if permission question exists, press allow
        try {
            homePage.clickPermissionAllowButton();
        }
        catch (NoSuchElementException e) {}

        logger.info("Verify that the web view is displayed inside the app");
        Assert.assertTrue(homePage.checkIfFreeFlowWebviewDisplayed());

    }


}
