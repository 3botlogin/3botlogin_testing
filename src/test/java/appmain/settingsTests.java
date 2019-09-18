package appmain;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.homePage;
import pageObjects.app.pinCodePage;
import pageObjects.app.recoverAccountPage;
import pageObjects.app.settingsPage;
import utils.testsUtils;

import java.io.IOException;
import java.lang.reflect.Method;


public class settingsTests extends Base{

    AppiumDriver<MobileElement> appDriver;
    homePage homePage;
    settingsPage settingsPage;


    @BeforeMethod
    public void setUp(Method method) throws IOException {
        logger.info("Running Test : " + method.getName());
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.TRUE);
        homePage = new homePage(appDriver);
        settingsPage = new settingsPage(appDriver);
    }

    @Test
    public void test1_removeAccount(){

        logger.info("Go to settings page");
        homePage.settingsButton.click();

        logger.info("Press Advanced settings option then click on " +
                    "'remove account option', should succeed");
        settingsPage.advancedSettingsDropDown.click();
        settingsPage.removeAccountoption.click();

        logger.info("Press 'Cancel' button, should be redirected back to setting page");
        settingsPage.cancelButton.click();
        Assert.assertTrue(settingsPage.changePinCode.isDisplayed());

        logger.info("Click on 'remove account option', then Press 'Yes' button," +
                    " should be redirected back to home page");
        settingsPage.removeAccountoption.click();
        settingsPage.yesButton.click();
        Assert.assertTrue(homePage.registerNowButton.isDisplayed());
    }


}