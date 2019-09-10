package appmain;

import io.appium.java_client.android.Activity;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import org.testng.Assert;
import java.io.IOException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import pageObjects.web.loginPage;
import pageObjects.app.pinCodePage;
import pageObjects.app.resourceAccessPage;
import pageObjects.app.homePage;
import pageObjects.app.settingsPage;
import pageObjects.app.registerPage;
import java.lang.reflect.Method;
import utils.Email;
import javax.mail.Message;


public class registerTests extends Base{
    AndroidDriver<AndroidElement> appDriver;
    AndroidDriver<AndroidElement> webDriver;
    loginPage loginPage;
    pinCodePage pinCodePage;
    resourceAccessPage resourceAccessPage;
    homePage homePage;
    registerPage registerPage;
    settingsPage settingsPage;

    @BeforeMethod
    public void setUp(Method method) throws IOException {
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

    @Test
    public void test1_registerUser() throws Exception {

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
        String email = (String) config.get("email");
        a.sendKeys(email);
        a.perform();
        registerPage.continueButton.click();

        logger.info("Copy phrase if needed, then click continue");
        registerPage.continueButton.click();

        logger.info("At the Finishing page, press continue ");
        registerPage.continueButton.click();

        logger.info("Check number of messages in the gmail");
        String email_password = (String) config.get("email_password");
        Email gmail = new Email(email, email_password, "smtp.gmail.com", Email.EmailFolder.INBOX);
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
        gmail.waitForNewMessage(emails_num);

        logger.info("Open the email and click the verification link");
        Message lastEmailMessage = gmail.getLatestMessage();
        String verificationLink = gmail.getURl(gmail.getMessageContent(lastEmailMessage));
        webDriver = Capabilities(Boolean.FALSE, Boolean.TRUE);
        loginPage = new loginPage(webDriver);
        webDriver.get(verificationLink);
        waitTillTextBePresent(loginPage.emailValidatedText, "Email validated");

        logger.info("Switch to the app and make sure the email is verified");
        String appPackage = (String) config.get("appPackage");
        String appActivity = (String) config.get("appActivity");
        appDriver.startActivity(new Activity(appPackage,appActivity));
        homePage.settingsButton.click();
        appDriver.navigate().back();
        homePage.settingsButton.click();
        String text = settingsPage.settingViewElements.get(4).getText();
        String [] words = text.split("\n");
        String lastWord = words[words.length - 1];
        Assert.assertEquals(lastWord, "Verified");

    }
}
