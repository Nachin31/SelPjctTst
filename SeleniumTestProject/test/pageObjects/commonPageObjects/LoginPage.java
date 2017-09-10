/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.commonPageObjects;

import pageObjects.commonPageObjects.Page;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import org.openqa.selenium.By;

/**
 *
 * @author Nacho Gómez
 */
public class LoginPage extends Page {
    
    private By toasterPopupDivs_locator = By.cssSelector("#growl_container > div");
    private By errorMessageLabel_locator = By.id("loginErrorMessage");
    
    public LoginPage(){
        super();
        add("TextField","Username",By.id("j_username"),null);
        add("TextField","Password",By.id("j_password"),null);
        add("Button","IniciarSesion",By.id("submit"),null);
    }
    
    public String getErrorMessage(){
        return $(errorMessageLabel_locator).getText();
    }
    
}
