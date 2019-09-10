package pageObjects.app;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class homePage {

        public homePage(AndroidDriver<AndroidElement> driver) {
            PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        }

        // elements for unregistered user home page
        @AndroidFindBy(xpath = "//android.widget.Button[@text='Register Now!']")
        public WebElement registerNowButton;

        @AndroidFindBy(xpath = "//android.widget.Button[@text='Recover account']")
        public WebElement recoverAccountButton;

        @AndroidFindBy(xpath = "//android.widget.Button[@text='Scan QR']")
        public WebElement scanQRButton;

        // This element shouldn't be in this page.. however it is common element
        @AndroidFindBy(xpath = "//android.widget.Button[@text='Back']")
        public WebElement backButton;

        // elements for registered user home page

        @AndroidFindBy(xpath = "//android.widget.Button[@text='Settings']")
        public WebElement settingsButton;

        @AndroidFindBy(xpath = "//android.widget.Button[@text='FreeFlowPages\n" +
                "Where privacy and social media co-exist.']")
        public WebElement freeFlowWindow;



}

