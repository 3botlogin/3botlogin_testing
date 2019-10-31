package appmain;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import static io.appium.java_client.touch.offset.ElementOption.element;
import static java.time.Duration.ofSeconds;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import pageObjects.app.SettingsPage;
import pageObjects.web.LoginPage;
import utils.Email;
import pageObjects.app.PinCodePage;
import pageObjects.app.RecoverAccountPage;
import pageObjects.app.HomePage;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.lang.reflect.Method;
import utils.TestsUtils;


public class recoverAccountTests extends Base{

    AppiumDriver<MobileElement> appDriver;
    AppiumDriver<MobileElement> webDriver;
    HomePage homePage;
    LoginPage loginPage;
    TestsUtils testsUtils;
    Email gmail;
    String email;

    @BeforeClass
    public void recoverAccountClassSetup() throws Exception {
        logger.info("Recover Account Class Setup");
        email = (String) config.get("email");
        String email_password = (String) config.get("email_password");
        gmail = new Email(email, email_password, "smtp.gmail.com", Email.EmailFolder.INBOX);
        if (config.get("registeredUser").toString().isEmpty()){
            // register only if there is no registeredUSer in the file to recover the account with
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
    public void setUp(Method method) throws IOException, MessagingException {
        logger.info("Running Test : " + method.getName());
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.FALSE);
        homePage = new HomePage(appDriver);
        testsUtils = new TestsUtils(appDriver);

    }

    @AfterMethod
    public void tearDown(Method method) {
        logger.info("End of Test : " + method.getName());
        appiumService.stop();
    }

    @AfterClass
    public void recoverAccountClassTearDown() throws IOException {
        //saveconfig to make sure it
        logger.info("Recover Account Class tearDown");
        config.setProperty("registeredUser", "");
        config.setProperty("accountPhrase", "");
        saveConfig();
    }

    @Test
    public void test1_recoverAccountWithRightData() throws Exception {
        // TBL-006

        logger.info("Open the app without a registered user");
        logger.info("Press 'Recover Account', should be directed to recover account page");
        RecoverAccountPage recoverAccPage = homePage.clickRecoverAccountButton();

        // In the future 3botname shouldn't be required
        logger.info("Enter a valid 3botName");
        recoverAccPage.enter3botName((String) config.get("registeredUser"));

        logger.info("Enter user's email");
        recoverAccPage.enterEmail((String) config.get("email"));

        logger.info("Copy, then paste correct user's phrase, should succeed");
        recoverAccPage.copyPastePhrase((String) config.get("accountPhrase"));

        logger.info("Press 'Recover Account', should be redirected to pin page");
        PinCodePage pinCodePage = recoverAccPage.clickRecoverAccountButton();

        logger.info("Enter the pin and confirm it, should succeed");
        int emails_num = gmail.getNumberOfMessages();
        pinCodePage.enterRightPinCode();
        pinCodePage.confirmRightPin();

        logger.info("Wait for the email to be received within 30 seconds");
        Boolean email_received = gmail.waitForNewMessage(emails_num);
        Assert.assertTrue(email_received, "Verification mail hasn't been received");

        logger.info("Open the email and click the verification link, email should be verified");
        Message lastEmailMessage = gmail.getLatestMessage();
        String verificationLink = gmail.getURl(gmail.getMessageContent(lastEmailMessage));
        webDriver = Capabilities(Boolean.FALSE, Boolean.TRUE);
        loginPage = new LoginPage(webDriver);
        webDriver.get(verificationLink);
        waitTillTextBePresent(loginPage.emailValidatedText, "Email validated");

        logger.info("Switch to the app and make sure the email is verified");
        switchToApp(appDriver);
        homePage.clickSettingButton();
        appDriver.navigate().back();
        SettingsPage settingPage = homePage.clickSettingButton();
        Assert.assertEquals(settingPage.getEmailVerificationStatus(), "Verified");
    }

    @Test
    public void test2_recoverAccountWithInvalidEmail() {
        // TBL-007

        if (Boolean.TRUE) {
            throw new SkipException("This test will pass when username " +
                                    "field is removed during recovering");
        }

        logger.info("Open the app without a registered user");
        logger.info("Press 'Recover Account', should be directed to recover account page");
        RecoverAccountPage recoverAccPage =  homePage.clickRecoverAccountButton();

        logger.info("Leave email field empty");
        logger.info("Press 'Recover Account', should get message saying 'Enter Valid Email'" +
                    "and another message saying 'Please enter an email.'");
        recoverAccPage.clickRecoverAccountButton();
        Assert.assertTrue(recoverAccPage.isEmailFieldErrorMessageDisplayed());
        Assert.assertEquals(recoverAccPage.getGeneralErrorMessage(),
                    "Please enter an email.");

        logger.info("Enter invalid email");
        recoverAccPage.enterEmail(randString());


        logger.info("Press 'Recover Account', should get message saying 'Enter Valid Email'" +
                    "and another message saying 'Please enter an email.'");
        recoverAccPage.clickRecoverAccountButton();
        Assert.assertTrue(recoverAccPage.isEmailFieldErrorMessageDisplayed());
        Assert.assertEquals(recoverAccPage.getGeneralErrorMessage(),
                    "Please enter an email.");

    }

    @Test
    public void test3_recoverAccountWithInvalidPhrase(){
        // TBL-008

        logger.info("Open the app without a registered user");
        logger.info("Press 'Recover Account', should be directed to recover account page");
        RecoverAccountPage recoverAccPage =  homePage.clickRecoverAccountButton();

        logger.info("Enter a valid email");
        recoverAccPage.enterEmail((String) config.get("email"));


        logger.info("Leave phrase field empty");
        logger.info("Press 'Recover Account', should get message saying 'Seed phrase is too short'" +
                    "and another message saying 'Enter your Seedphrase'");
        recoverAccPage.clickRecoverAccountButton();
        Assert.assertEquals(recoverAccPage.getGeneralErrorMessage(),
                    "Seed phrase is too short");
        Assert.assertTrue(recoverAccPage.isPhraseFieldErrorMessage());


        logger.info("Copy, then paste short phrase");
        recoverAccPage.copyPastePhrase(generateStringOfWords(2));
        logger.info("Press 'Recover Account', should get message saying 'Seed phrase is too short'");
        recoverAccPage.clickRecoverAccountButton();
        Assert.assertEquals(recoverAccPage.getGeneralErrorMessage(),
                    "Seed phrase is too short");

        logger.info("Copy, then paste wrong phrase with 24 words");
        recoverAccPage.copyPastePhrase(" " + generateStringOfWords(22));

        logger.info("Press 'Recover Account', should get message saying 'Invalid mnemonic'");
        appDriver.hideKeyboard();
        recoverAccPage.clickRecoverAccountButton();
        Assert.assertEquals(recoverAccPage.getGeneralErrorMessage(),
                    "Invalid mnemonic");

        logger.info("Copy, then paste long phrase");
        recoverAccPage.copyPastePhrase(" " + generateStringOfWords(10));

        logger.info("Press 'Recover Account', should get message saying 'Seed phrase is too long'");
        appDriver.hideKeyboard();
        recoverAccPage.clickRecoverAccountButton();
        Assert.assertEquals(recoverAccPage.getGeneralErrorMessage(),
                    "Seed phrase is too long");


    }
}
