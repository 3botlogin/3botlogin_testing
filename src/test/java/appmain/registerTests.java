package appmain;

import org.testng.annotations.*;
import org.testng.Assert;
import java.io.IOException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import pageObjects.app.HomePage;
import pageObjects.app.SettingsPage;
import java.lang.reflect.Method;
import utils.Email;
import utils.TestsUtils;
import javax.mail.MessagingException;


public class registerTests extends Base{
    private AppiumDriver<MobileElement> appDriver;
    private AppiumDriver<MobileElement> webDriver;
    private HomePage homePage;
    private TestsUtils testsUtils;
    private Email gmail;
    private String email;


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
        homePage = new HomePage(appDriver);
        testsUtils = new TestsUtils(appDriver);

    }

    @AfterMethod
    public void tearDown(Method method) {
        logger.info("End of Test : " + method.getName());
        appiumService.stop();
    }

    @AfterClass
    public void registerTestsClassTeardown() throws IOException {
        //saveconfig
        logger.info("Register Class tearDown");
        config.setProperty("registeredUser", "");
        config.setProperty("accountPhrase", "");
        saveConfig();
    }

    @Test
    public void test1_registeringUser() throws Exception {
        // TBL-001

        testsUtils.registeringUSerCommonSteps();

        testsUtils.verifyEmail();

        logger.info("Switch to the app and make sure the email is verified");
        switchToApp(appDriver);
        homePage.clickSettingButton();
        appDriver.navigate().back();
        SettingsPage settingPage = homePage.clickSettingButton();
        String verificationStatus = settingPage.getEmailVerificationStatus();
        Assert.assertEquals(verificationStatus, "Verified");

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
        SettingsPage settingPage = homePage.clickSettingButton();
        settingPage.resendVerificationEmail();
        Assert.assertTrue(settingPage.checkEmailResentTextDisplayed(),
                  "Pop up message is not displayed");
        settingPage.clickOkButton();

        logger.info("Check if you received another verification email, should be received");
        Boolean email_received = gmail.waitForNewMessage(emails_num);
        Assert.assertTrue(email_received, "Verification mail hasn't been received");

        testsUtils.verifyEmail();

    }

}
