package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

        private AppiumDriver<MobileElement> driver;

        public HomePage(AppiumDriver<MobileElement> driver) {
            this.driver = driver;
            PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        }

        // elements for unregistered user home page
        @FindBy(xpath = "//android.widget.Button[@text='Register Now!']")
        private WebElement registerNowButton;

        @FindBy(xpath = "//android.widget.Button[@text='Recover account']")
        private WebElement recoverAccountButton;

        @FindBy(xpath = "//android.widget.Button[@text='Scan QR']")
        private WebElement scanQRButton;

        // This element shouldn't be in this page.. however it is common element
        @FindBy(xpath = "//android.widget.Button[@text='Back']")
        private WebElement backButton;

        // elements for registered user home page
        @FindBy(xpath = "//android.widget.Button[@text='Settings']")
        private WebElement settingsButton;

        @FindBy(xpath = "//android.widget.Button[@text='FreeFlowPages\n" +
                "Where privacy and social media co-exist.']")
        private WebElement freeFlowWindow;

        @FindBy(className = "android.webkit.WebView")
        private WebElement freeFlowWebView;

        //elements for ffp

        @FindBy(xpath = "//android.widget.Button[@text='ALLOW']")
        private WebElement permissionAllow;


        public RegisterPage clickRegisterNowButton(){
                registerNowButton.click();
                return new RegisterPage(driver);
        }

        public Boolean isRegisterNowButtonDisplayed(){
                return registerNowButton.isDisplayed();
        }

        public void clickFreeFlowWindow(){
                try{
                    freeFlowWindow.click();
                }
                catch (NoSuchElementException e) {
                    driver.navigate().back();
                    freeFlowWindow.click();
                }
        }

        public void clickPermissionAllowButton(){
                permissionAllow.click();
        }

        public Boolean checkIfFreeFlowWebviewDisplayed(){
                return freeFlowWebView.isDisplayed();
        }

        public SettingsPage clickSettingButton(){
                settingsButton.click();
                return new SettingsPage(driver);
        }

        public RecoverAccountPage clickRecoverAccountButton(){
                recoverAccountButton.click();
                return new RecoverAccountPage(driver);
        }


}

