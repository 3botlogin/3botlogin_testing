package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class RegisterPage {

    private AppiumDriver<MobileElement> driver;

    public RegisterPage(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//android.widget.Button[@text='CONTINUE']")
    private WebElement continueButton;

    @FindBy(className = "android.widget.EditText")
    private WebElement emailField;

    @FindBy(xpath = "//android.view.View[contains(@text,'write')]")
    private WebElement PhrasePageText;

    private void enterString(String word){
        Actions a = new Actions(driver);
        a.sendKeys(word);
        a.perform();
    }

    public void enterUserName(String userName){
        enterString(userName);
    }

    public void enterEmail(String email){
        enterString(email);
    }

    public void clickContinueButton(){
        continueButton.click();
    }

    public void clickEmailField(){
        emailField.click();
    }

    public String getPhrase(){
        String text = PhrasePageText.getText();
        String phrase = text.split("\n")[1];
        return phrase;
    }

}


