package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class registerPage {

    public registerPage(AppiumDriver<MobileElement> driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//android.widget.Button[@text='CONTINUE']")
    public WebElement continueButton;

    @FindBy(className = "android.widget.EditText")
    public WebElement emailField;

    @FindBy(xpath = "//android.view.View[contains(@text,'write')]")
    public WebElement PhrasePageText;

}


