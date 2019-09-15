package utils;

import appmain.Base;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.interactions.Actions;
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
    private String userName;
    private String phrase;

    public testsUtils(AppiumDriver<MobileElement> appDriver, Email gmail) {
        this.appDriver = appDriver;
        this.gmail = gmail;
        pinCodePage = new pinCodePage(appDriver);
        homePage = new homePage(appDriver);
        recoverAccountPage = new recoverAccountPage(appDriver);
        registerPage = new registerPage(appDriver);
    }

    public void registeringUSerCommonSteps() throws MessagingException, IOException {

        logger.info("open the app and press register now");
        homePage.registerNowButton.click();

        logger.info("Provide random 3bot name then press continue, should succeed");
        userName = RandomStringUtils.randomAlphanumeric(10);
        Actions a = new Actions(appDriver);
        a.sendKeys(userName);
        a.perform();
        // 'registerPage.doubleNameField.sendKeys(randUser)'not working.have to uer actions
        registerPage.continueButton.click();

        logger.info("Provide email then press continue, should succeed");
        registerPage.emailField.click();
        String email = (String) config.get("email");
        a.sendKeys(email);
        a.perform();
        registerPage.continueButton.click();

        logger.info("Copy phrase if needed, then click continue");
        String text = registerPage.PhrasePageText.getText();
        phrase = text.split("\n")[1];
        registerPage.continueButton.click();

        logger.info("At the Finishing page, press continue ");
        registerPage.continueButton.click();

        logger.info("Check number of messages in the gmail");
        int emails_num = gmail.getNumberOfMessages();

        enterAndConfirmPinCode();

        logger.info("Wait for the email to be received within 30 seconds");
        Boolean email_received = gmail.waitForNewMessage(emails_num);
        Assert.assertTrue(email_received, "Verification mail hasn't been received");

    }

    public void enterAndConfirmPinCode(){

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
