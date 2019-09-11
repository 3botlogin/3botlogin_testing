package appmain;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.interactions.Actions;
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
    Email gmail;
    String email;

    @BeforeClass
    public void registerTestsSetup() throws MessagingException {
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

    }

    @AfterMethod
    public void tearDown(Method method) {
        logger.info("End of Test : " + method.getName());
        appiumService.stop();
    }

    public void registeringUSerCommonSteps() throws MessagingException {

        logger.info("open the app and press register now");
        homePage.registerNowButton.click();

        logger.info("Provide random 3bot name then press continue, should succeed");
        String randUser = RandomStringUtils.randomAlphanumeric(10);
        Actions a = new Actions(appDriver);
        a.sendKeys(randUser);
        a.perform();
        // 'registerPage.doubleNameField.sendKeys(randUser)'not working.have to uer actions
        registerPage.continueButton.click();

        logger.info("Provide email then press continue, should succeed");
        registerPage.emailField.click();
        a.sendKeys(email);
        a.perform();
        registerPage.continueButton.click();

        logger.info("Copy phrase if needed, then click continue");
        registerPage.continueButton.click();

        logger.info("At the Finishing page, press continue ");
        registerPage.continueButton.click();

        logger.info("Check number of messages in the gmail");
        int emails_num = gmail.getNumberOfMessages();

        logger.info("Provide username pin code, confirm it then press OK, should succeed");
        pinCodePage.oneButton.click();
        pinCodePage.twoButton.click();
        pinCodePage.threeButton.click();
        pinCodePage.fourButton.click();
        pinCodePage.OKButton.click();

        pinCodePage.oneButton.click();
        pinCodePage.twoButton.click();
        pinCodePage.threeButton.click();
        pinCodePage.fourButton.click();
        pinCodePage.OKButton.click();

        logger.info("Wait for the email to be received within 30 seconds");
        Boolean email_received = gmail.waitForNewMessage(emails_num);
        Assert.assertTrue(email_received, "Verification mail hasn't been received");
    }

    @Test
    public void test1_registeringUser() throws Exception {

        registeringUSerCommonSteps();

        logger.info("Open the email and click the verification link, email should be verified");
        Message lastEmailMessage = gmail.getLatestMessage();
        String verificationLink = gmail.getURl(gmail.getMessageContent(lastEmailMessage));
        webDriver = Capabilities(Boolean.FALSE, Boolean.TRUE);
        loginPage = new loginPage(webDriver);
        webDriver.get(verificationLink);
        waitTillTextBePresent(loginPage.emailValidatedText, "Email validated");

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

        registeringUSerCommonSteps();

        logger.info("Don't verify the email");
        int emails_num = gmail.getNumberOfMessages();
        appDriver.navigate().back();

        logger.info("Go to app preferences and resend verification email," +
                    " should get a message 'email has been resend'");
        homePage.settingsButton.click();
        settingsPage.settingViewElements.get(4).click();
        Assert.assertTrue(settingsPage.emailResentText.isDisplayed(),
                  "Pop ip message is not displayed");
        settingsPage.OkButton.click();

        logger.info("Check if you received another verification email, should be received");
        Boolean email_received = gmail.waitForNewMessage(emails_num);
        Assert.assertTrue(email_received, "Verification mail hasn't been received");

        logger.info("Open the email and click the verification link, email should be verified");
        Message lastEmailMessage = gmail.getLatestMessage();
        String verificationLink = gmail.getURl(gmail.getMessageContent(lastEmailMessage));
        webDriver = Capabilities(Boolean.FALSE, Boolean.TRUE);
        loginPage = new loginPage(webDriver);
        webDriver.get(verificationLink);
        waitTillTextBePresent(loginPage.emailValidatedText, "Email validated");

    }

}
