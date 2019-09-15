package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class homePage {

        public homePage(AppiumDriver<MobileElement> driver) {
            PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        }

        // elements for unregistered user home page
        @FindBy(xpath = "//android.widget.Button[@text='Register Now!']")
        public WebElement registerNowButton;

        @FindBy(xpath = "//android.widget.Button[@text='Recover account']")
        public WebElement recoverAccountButton;

        @FindBy(xpath = "//android.widget.Button[@text='Scan QR']")
        public WebElement scanQRButton;

        // This element shouldn't be in this page.. however it is common element
        @FindBy(xpath = "//android.widget.Button[@text='Back']")
        public WebElement backButton;

        // elements for registered user home page
        @FindBy(xpath = "//android.widget.Button[@text='Settings']")
        public WebElement settingsButton;

        @FindBy(xpath = "//android.widget.Button[@text='FreeFlowPages\n" +
                "Where privacy and social media co-exist.']")
        public WebElement freeFlowWindow;

        @FindBy(className = "android.webkit.WebView")
        public WebElement freeFlowWebView;


}

