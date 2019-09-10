package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;


public class settingsPage {

    public settingsPage(AppiumDriver<MobileElement> driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(className = "android.view.View")
    public List<WebElement> settingViewElements;

    @AndroidFindBy(xpath = "//android.view.View[@text='Show Phrase']")
    public WebElement showPhrase;

    @AndroidFindBy(className = "android.widget.CheckBox")
    public WebElement fingerPrintCheckbox;

    @AndroidFindBy(xpath = "//android.view.View[@text='Change pincode']")
    public WebElement changePinCode;

    @AndroidFindBy(xpath = "//android.view.View[@text='Advanced settings']")
    public WebElement advancedSettingsDropDown;




}
