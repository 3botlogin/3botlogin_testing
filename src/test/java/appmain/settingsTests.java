package appmain;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.app.homePage;
import pageObjects.app.pinCodePage;
import pageObjects.app.settingsPage;
import utils.testsUtils;
import java.io.IOException;
import java.lang.reflect.Method;


public class settingsTests extends Base{

    AppiumDriver<MobileElement> appDriver;
    homePage homePage;
    testsUtils testsUtils;


    @BeforeClass
    public void settingsClassSetup() throws Exception {
        logger.info("Settings Class Setup");
        if (config.get("registeredUser").toString().isEmpty()){
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
    }


    @BeforeMethod
    public void setUp(Method method) throws Exception {
        logger.info("Running Test : " + method.getName());
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.TRUE);
        homePage = new homePage(appDriver);
        testsUtils = new testsUtils(appDriver);
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
        settingsPage setPage = homePage.clickSettingButton();

        logger.info("Press 'Change pincode', should be redirected to change pincode page");
        pinCodePage pinPage =  setPage.clickChangePinCode();
        Assert.assertTrue(pinPage.checkEnterOldPinTextDisplayed());

        logger.info("Enter wrong old pin code, should still see message saying 'Enter old pincode'");
        for (int i=0; i<4; i++) {
            pinPage.clickNumber("8");
        }
        pinPage.clickOkButton();
        Assert.assertTrue(pinPage.checkEnterOldPinTextDisplayed());

        logger.info("Enter correct old pin code, should succeed");
        pinPage.enterRightPinCode();
        Assert.assertTrue(pinPage.checkEnterNewPinTextDisplayed());

        logger.info("Enter new pincode, should succeed");
        for (int i=0; i<4; i++) {
            pinPage.clickNumber("8");
        }
        pinPage.clickOkButton();
        Assert.assertTrue(pinPage.checkConfirmNewPinTextDisplayed());

        logger.info("Confirm new pincode by entering a wrong pincode. should fail");
        for (int i=0; i<4; i++) {
            pinPage.clickNumber("1");
        }
        pinPage.clickOkButton();
        Assert.assertTrue(pinPage.checkConfirmNewPinTextDisplayed());

        logger.info("Confirm new pincode, should succeed");
        for (int i=0; i<4; i++) {
            pinPage.clickNumber("8");
        }
        pinPage.clickOkButton();
        Assert.assertTrue(pinPage.checkPinChangedSuccessfullyTextDisplayed());

        logger.info("Make sure pincode has actually changed by using the " +
                    "new pin, should succeed");
        appDriver.navigate().back();
        setPage.clickChangePinCode();
        for (int i=0; i<4; i++) {
            pinPage.clickNumber("8");
        }
        pinPage.clickOkButton();
        Assert.assertTrue(pinPage.checkEnterNewPinTextDisplayed());
        pinPage.enterRightPinCode();
        pinPage.confirmRightPin();

    }

    @Test
    public void test2_enableAndDisableFingerPrint(){
        // TBL-010

        logger.info("Go to settings page");
        settingsPage setPage =  homePage.clickSettingButton();

        logger.info("Press fingerprint, then cancel, Fingerprint checkbox shouldn't be enabled");
        setPage.enableFingerPrintCheckbox();
        setPage.clickCancelButton();
        Assert.assertEquals(setPage.isFingerPrintBoxChecked(), "false");

        logger.info("Enable fingerprint, should succeed");
        setPage.enableFingerPrintCheckbox();
        setPage.clickYesButton();
        Assert.assertEquals(setPage.isFingerPrintBoxChecked(), "true");

        logger.info("Press 'show phrase' and make sure a FingerPrint " +
                    "authentication will be required");
        setPage.clickShowPhrase();
        Assert.assertTrue(setPage.isFingerPrintMessageDisplayed());
        setPage.clickFingerPrintCancelButton();
        setPage.clickCancelButton();

        logger.info("Disable Fingerprint with providing wrong pin, " +
                    "Fingerprint should be still enabled");
        pinCodePage pinPage =  setPage.disableFingerPrintCheckbox();
        for (int i=0; i<4; i++) {
            pinPage.clickNumber("8");
        }
        pinPage.clickOkButton();
        Assert.assertEquals(setPage.isFingerPrintBoxChecked(), "true");

        logger.info("Disable Fingerprint with providing correct pin, " +
                    "then press cancel, Fingerprint should be still enabled");
        setPage.disableFingerPrintCheckbox();
        pinPage.enterRightPinCode();
        setPage.clickCancelButton();
        Assert.assertEquals(setPage.isFingerPrintBoxChecked(), "true");

        logger.info("Disable Fingerprint with providing correct pin, " +
                    "then press yes, Fingerprint should disabled");
        setPage.disableFingerPrintCheckbox();
        pinPage.enterRightPinCode();
        setPage.clickYesButton();
        Assert.assertEquals(setPage.isFingerPrintBoxChecked(),"false");

    }

    @Test
    public void test3_checkOnSettingsPageElements(){
        // TBL-011

        logger.info("Go to settings page");
        settingsPage setPage = homePage.clickSettingButton();

        logger.info("Make sure user can see his username");
        Assert.assertEquals(setPage.get3botUserName(),
                    (String) config.get("registeredUser") + ".3bot");

        logger.info("Make sure user can see his email");
        Assert.assertEquals(setPage.getEmail(), (String) config.get("email"));

        logger.info("Press 'Show Phrase' and provide wrong pin code, shouldn't show phrase");
        pinCodePage pinPage = setPage.clickShowPhrase();
        for (int i=0; i<4; i++) {
            pinPage.clickNumber("8");
        }
        pinPage.clickOkButton();
        Assert.assertTrue(setPage.isShowPhraseDisplayed());

        logger.info("Press 'Show Phrase' and provide right pin code, phrase should be shown");
        setPage.clickShowPhrase();
        pinPage.enterRightPinCode();
        Assert.assertEquals(setPage.getPhrase(), (String) config.get("accountPhrase"));

        logger.info("Press 'Close' Button, should get back to settings page");
        setPage.clickCloseButton();
        Assert.assertTrue(setPage.isShowPhraseDisplayed());

        logger.info("Make sure Version field exists");
        Assert.assertEquals(setPage.getVersion(), "Version:");
    }


    @Test
    public void test4_removeAccount() throws IOException {
        // TBL-012

        logger.info("Go to settings page");
        settingsPage setPage = homePage.clickSettingButton();

        logger.info("Press Advanced settings option then click on " +
                    "'remove account option', should succeed");
        setPage.clickAdvancedSetting();
        setPage.clickRemoveAccount();

        logger.info("Press 'Cancel' button, should be redirected back to setting page");
        setPage.clickCancelButton();
        Assert.assertTrue(setPage.isChangePinCodeDisplayed());

        logger.info("Click on 'remove account option', then Press 'Yes' button," +
                    " should be redirected back to home page");
        setPage.clickRemoveAccount();
        setPage.clickYesButton();
        Assert.assertTrue(homePage.isRegisterNowButtonDisplayed());

        config.setProperty("registeredUser", "");
        saveConfig();
    }



}