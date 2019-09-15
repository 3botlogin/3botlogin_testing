package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;


public class settingsPage {

    public settingsPage(AppiumDriver<MobileElement> driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(className = "android.view.View")
    public List<WebElement> settingViewElements;

    @FindBy(xpath = "//android.view.View[@text='Show Phrase']")
    public WebElement showPhrase;

    @FindBy(className = "android.widget.CheckBox")
    public WebElement fingerPrintCheckbox;

    @FindBy(xpath = "//android.view.View[@text='Change pincode']")
    public WebElement changePinCode;

    @FindBy(xpath = "//android.view.View[@text='Advanced settings']")
    public WebElement advancedSettingsDropDown;

    //popups
    @FindBy(xpath = "//android.view.View[@text='Email has been resent.']")
    public WebElement emailResentText;

    @FindBy(xpath = "//android.widget.Button[@text='Ok']")
    public WebElement OkButton;


}
