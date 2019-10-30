package utils;

import appmain.Base;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import pageObjects.app.homePage;
import pageObjects.app.pinCodePage;
import pageObjects.app.recoverAccountPage;
import pageObjects.app.registerPage;
import pageObjects.web.loginPage;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;


public class testsUtils extends Base {

    AppiumDriver<MobileElement> appDriver;
    AppiumDriver<MobileElement> webDriver;
    loginPage loginPage;
    pinCodePage pinCodePage;
    homePage homePage;
    registerPage registerPage;
    recoverAccountPage recoverAccountPage;
    Email gmail;
    private String userName = randString();
    private String phrase;

    public testsUtils(AppiumDriver<MobileElement> appDriver) throws MessagingException {
        this.appDriver = appDriver;
        gmail = new Email((String) config.get("email"), (String) config.get("email_password"),
                    "smtp.gmail.com", Email.EmailFolder.INBOX);
        pinCodePage = new pinCodePage(appDriver);
        homePage = new homePage(appDriver);
        recoverAccountPage = new recoverAccountPage(appDriver);
        registerPage = new registerPage(appDriver);
    }

    public void registeringUSerCommonSteps() throws MessagingException, IOException {

        logger.info("open the app and press register now");
        registerPage regPage =  homePage.clickRegisterNowButton();

        logger.info("Provide random 3bot name then press continue, should succeed");
        regPage.enterUserName(userName);
        // 'registerPage.doubleNameField.sendKeys(randUser)'not working.have to use actions
        regPage.clickContinueButton();

        logger.info("Provide email then press continue, should succeed");
        regPage.clickEmailField();
        regPage.enterEmail((String) config.get("email"));
        regPage.clickContinueButton();

        logger.info("Copy phrase, then click continue");
        phrase = regPage.getPhrase();
        regPage.clickContinueButton();

        logger.info("At the Finishing page, press continue ");
        regPage.clickContinueButton();

        logger.info("Check number of messages in the gmail");
        int emails_num = gmail.getNumberOfMessages();

        pinCodePage.enterRightPinCode();
        pinCodePage.confirmRightPin();

        logger.info("Wait for the email to be received within 30 seconds");
        Boolean email_received = gmail.waitForNewMessage(emails_num);
        Assert.assertTrue(email_received, "Verification mail hasn't been received");

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

    public String getUserName(){
        return userName;
    }

    public String getPhrase(){
        return phrase;
    }


}
