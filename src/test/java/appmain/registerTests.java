package appmain;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.Assert;
import java.io.IOException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import pageObjects.web.loginPage;
import pageObjects.app.pinCodePage;
import pageObjects.app.resourceAccessPage;
import pageObjects.app.homePage;
import pageObjects.app.settingsPage;
import pageObjects.app.registerPage;
import java.lang.reflect.Method;
import utils.Email;
import utils.testsUtils;

import javax.mail.Message;
import javax.mail.MessagingException;


public class registerTests extends Base{
    AppiumDriver<MobileElement> appDriver;
    AppiumDriver<MobileElement> webDriver;
    loginPage loginPage;
    pinCodePage pinCodePage;
    resourceAccessPage resourceAccessPage;
    homePage homePage;
    registerPage registerPage;
    settingsPage settingsPage;
    testsUtils testsUtils;
    Email gmail;
    String email;


    @BeforeClass
    public void registerTestsClassSetup() throws MessagingException {
        logger.info("Register Class Setup");
        email = (String) config.get("email");
        String email_password = (String) config.get("email_password");
        gmail = new Email(email, email_password, "smtp.gmail.com", Email.EmailFolder.INBOX);
    }

    @BeforeMethod
    public void setUp(Method method) throws IOException, MessagingException {

        logger.info("Running Test : " + method.getName());
        appiumService = startServer();
        appDriver = Capabilities(Boolean.TRUE, Boolean.FALSE);
        pinCodePage = new pinCodePage(appDriver);
        resourceAccessPage = new resourceAccessPage(appDriver);
        registerPage = new registerPage(appDriver);
        settingsPage = new settingsPage(appDriver);
        homePage = new homePage(appDriver);
        testsUtils = new testsUtils(appDriver);

    }

    @AfterMethod
    public void tearDown(Method method) {
        logger.info("End of Test : " + method.getName());
        appiumService.stop();
    }

    @AfterClass
    public void registerTestsClassTeardown() throws IOException {
        //saveconfig to make sure it
        logger.info("Register Class tearDown");
        config.setProperty("registeredUser", "");
        config.setProperty("accountPhrase", "");
        saveConfig();
    }

    public void verifyEmail() throws Exception {

        logger.info("Open the email and click the verification link, email should be verified");
        Message lastEmailMessage = gmail.getLatestMessage();
        String verificationLink = gmail.getURl(gmail.getMessageContent(lastEmailMessage));
        webDriver = Capabilities(Boolean.FALSE, Boolean.TRUE);
        loginPage = new loginPage(webDriver);
        webDriver.get(verificationLink);
        waitTillTextBePresent(loginPage.emailValidatedText, "Email validated");
    }

    @Test
    public void test1_registeringUser() throws Exception {
        // TBL-001

        testsUtils.registeringUSerCommonSteps();

        verifyEmail();

        logger.info("Switch to the app and make sure the email is verified");
        String appPackage = (String) config.get("appPackage");
        String appActivity = (String) config.get("appActivity");
        ((AndroidDriver) appDriver).startActivity(new Activity(appPackage,appActivity));
        homePage.settingsButton.click();
        appDriver.navigate().back();
        homePage.settingsButton.click();
        String text = settingsPage.settingViewElements.get(4).getText();
        String [] words = text.split("\n");
        String lastWord = words[words.length - 1];
        Assert.assertEquals(lastWord, "Verified");

    }

    @Test
    public void test2_resendEmailWhileRegistering() throws Exception {
        // TBL-002

        testsUtils.registeringUSerCommonSteps();

        logger.info("Don't verify the confirmation email");
        int emails_num = gmail.getNumberOfMessages();
        appDriver.navigate().back();

        logger.info("Go to app preferences and press resend verification email," +
                    " should get a message saying 'email has been resend'");
        homePage.settingsButton.click();
        settingsPage.settingViewElements.get(4).click();
        Assert.assertTrue(settingsPage.emailResentText.isDisplayed(),
                  "Pop ip message is not displayed");
        settingsPage.OkButton.click();

        logger.info("Check if you received another verification email, should be received");
        Boolean email_received = gmail.waitForNewMessage(emails_num);
        Assert.assertTrue(email_received, "Verification mail hasn't been received");

        verifyEmail();

    }

}
