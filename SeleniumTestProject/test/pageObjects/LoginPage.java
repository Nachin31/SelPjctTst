/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

/**
 *
 * @author Nacho Gómez
 */
public class LoginPage extends Page {
    
    private By toasterPopupDivs_locator = By.className("ui-growl-message");
    
    public LoginPage(){
        super();
        add("TextField","Username",By.id("j_username"));
        add("TextField","Password",By.id("j_password"));
        add("Button","IniciarSesion",By.id("submit"));
    }
    
    
}
