package appmain;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.app.HomePage;
import pageObjects.app.PinCodePage;
import pageObjects.app.SettingsPage;
import utils.TestsUtils;
import java.io.IOException;
import java.lang.reflect.Method;


public class settingsTests extends Base{

    private AppiumDriver<MobileElement> appDriver;
    private HomePage homePage;
    private TestsUtils testsUtils;


    @BeforeClass
    public void settingsClassSetup() throws Exception {
        logger.info("Settings Class Setup");
        if (config.get("registeredUser").toString().isEmpty()){
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
    }


    @BeforeMethod
    public void setUp(Method method) throws Exception {
        logger.info("Running Test : " + method.getName());
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.TRUE);
        homePage = new HomePage(appDriver);
        testsUtils = new TestsUtils(appDriver);
    }

    @AfterMethod
    public void tearDown(Method method) {
        logger.info("End of Test : " + method.getName());
        appiumService.stop();
    }

    @Test
    public void test1_changePinCode(){
        // TBL-009

        logger.info("Go to settings page");
        SettingsPage settingPage = homePage.clickSettingButton();

        logger.info("Press 'Change pincode', should be redirected to change pincode page");
        PinCodePage pinCodePage =  settingPage.clickChangePinCode();
        Assert.assertTrue(pinCodePage.checkEnterOldPinTextDisplayed());

        logger.info("Enter wrong old pin code, should still see message saying 'Enter old pincode'");
        pinCodePage.enterPinCode("8888");
        Assert.assertTrue(pinCodePage.checkEnterOldPinTextDisplayed());

        logger.info("Enter correct old pin code, should succeed");
        pinCodePage.enterPinCode("1234");
        Assert.assertTrue(pinCodePage.checkEnterNewPinTextDisplayed());

        logger.info("Enter new pincode, should succeed");
        pinCodePage.enterPinCode("8888");
        Assert.assertTrue(pinCodePage.checkConfirmNewPinTextDisplayed());

        logger.info("Confirm new pincode by entering a wrong pincode. should fail");
        pinCodePage.enterPinCode("1111");
        Assert.assertTrue(pinCodePage.checkConfirmNewPinTextDisplayed());

        logger.info("Confirm new pincode, should succeed");
        pinCodePage.enterPinCode("8888");
        Assert.assertTrue(pinCodePage.checkPinChangedSuccessfullyTextDisplayed());

        logger.info("Make sure pincode has actually changed by using the " +
                    "new pin, should succeed");
        appDriver.navigate().back();
        settingPage.clickChangePinCode();
        pinCodePage.enterPinCode("8888");
        Assert.assertTrue(pinCodePage.checkEnterNewPinTextDisplayed());
        pinCodePage.enterPinCode("1234");
        pinCodePage.confirmPinCode("1234");

    }

    @Test
    public void test2_enableAndDisableFingerPrint(){
        // TBL-010

        logger.info("Go to settings page");
        SettingsPage settingPage =  homePage.clickSettingButton();

        logger.info("Press fingerprint, then cancel, Fingerprint checkbox shouldn't be enabled");
        settingPage.enableFingerPrintCheckbox();
        settingPage.clickCancelButton();
        Assert.assertEquals(settingPage.isFingerPrintBoxChecked(), "false");

        logger.info("Enable fingerprint, should succeed");
        settingPage.enableFingerPrintCheckbox();
        settingPage.clickYesButton();
        Assert.assertEquals(settingPage.isFingerPrintBoxChecked(), "true");

        logger.info("Press 'show phrase' and make sure a FingerPrint " +
                    "authentication will be required");
        settingPage.clickShowPhrase();
        Assert.assertTrue(settingPage.isFingerPrintMessageDisplayed());
        settingPage.clickFingerPrintCancelButton();
        settingPage.clickCancelButton();

        logger.info("Disable Fingerprint with providing wrong pin, " +
                    "Fingerprint should be still enabled");
        PinCodePage pinCodePage =  settingPage.disableFingerPrintCheckbox();
        pinCodePage.enterPinCode("8888");
        Assert.assertEquals(settingPage.isFingerPrintBoxChecked(), "true");

        logger.info("Disable Fingerprint with providing correct pin, " +
                    "then press cancel, Fingerprint should be still enabled");
        settingPage.disableFingerPrintCheckbox();
        pinCodePage.enterPinCode("1234");
        settingPage.clickCancelButton();
        Assert.assertEquals(settingPage.isFingerPrintBoxChecked(), "true");

        logger.info("Disable Fingerprint with providing correct pin, " +
                    "then press yes, Fingerprint should disabled");
        settingPage.disableFingerPrintCheckbox();
        pinCodePage.enterPinCode("1234");
        settingPage.clickYesButton();
        Assert.assertEquals(settingPage.isFingerPrintBoxChecked(),"false");

    }

    @Test
    public void test3_checkOnSettingsPageElements(){
        // TBL-011

        logger.info("Go to settings page");
        SettingsPage settingPage = homePage.clickSettingButton();

        logger.info("Make sure user can see his username");
        Assert.assertEquals(settingPage.get3botUserName(),
                    (String) config.get("registeredUser") + ".3bot");

        logger.info("Make sure user can see his email");
        Assert.assertEquals(settingPage.getEmail(), (String) config.get("email"));

        logger.info("Press 'Show Phrase' and provide wrong pin code, shouldn't show phrase");
        PinCodePage pinCodePage = settingPage.clickShowPhrase();
        pinCodePage.enterPinCode("8888");
        Assert.assertTrue(settingPage.isShowPhraseDisplayed());

        logger.info("Press 'Show Phrase' and provide right pin code, phrase should be shown");
        settingPage.clickShowPhrase();
        pinCodePage.enterPinCode("1234");
        Assert.assertEquals(settingPage.getPhrase(), (String) config.get("accountPhrase"));

        logger.info("Press 'Close' Button, should get back to settings page");
        settingPage.clickCloseButton();
        Assert.assertTrue(settingPage.isShowPhraseDisplayed());

        logger.info("Make sure Version field exists");
        Assert.assertEquals(settingPage.getVersion(), "Version:");
    }


    @Test
    public void test4_removeAccount() throws IOException {
        // TBL-012

        logger.info("Go to settings page");
        SettingsPage settingPage = homePage.clickSettingButton();

        logger.info("Press Advanced settings option then click on " +
                    "'remove account option', should succeed");
        settingPage.clickAdvancedSetting();
        settingPage.clickRemoveAccount();

        logger.info("Press 'Cancel' button, should be redirected back to setting page");
        settingPage.clickCancelButton();
        Assert.assertTrue(settingPage.isChangePinCodeDisplayed());

        logger.info("Click on 'remove account option', then Press 'Yes' button," +
                    " should be redirected back to home page");
        settingPage.clickRemoveAccount();
        settingPage.clickYesButton();
        Assert.assertTrue(homePage.isRegisterNowButtonDisplayed());

        config.setProperty("registeredUser", "");
        saveConfig();
    }



}