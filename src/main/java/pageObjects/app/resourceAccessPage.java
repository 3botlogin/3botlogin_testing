package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class resourceAccessPage {

    public resourceAccessPage(AppiumDriver<MobileElement> driver)
    {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    //access to username and email
    @AndroidFindBy(xpath = "//android.widget.Button[@text='Accept']")
    public WebElement acceptButton;



}
