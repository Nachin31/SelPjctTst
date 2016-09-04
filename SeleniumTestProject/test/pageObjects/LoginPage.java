/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
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
        add("TextField","Username",By.id("j_username"));
        add("TextField","Password",By.id("j_password"));
        add("Button","IniciarSesion",By.id("submit"));
    }
    
    public int getToasterMessageCount(){     
        int count = 0;
        count += $$(toasterPopupDivs_locator).size();
        
        return count;
    }
    
    public boolean messageDisplayed(String messageType, String message){
        
        boolean displayed = false;
        String color = "";
        switch(messageType){
            case "Error":
                color = "rgba(239, 83, 80, 1)";
        }
        
        List<SelenideElement> toasterMessages = $$(toasterPopupDivs_locator);
        
        for(SelenideElement toasterMessage : toasterMessages){
            String actualMessage = toasterMessage.getText();
            String backgroundColor = toasterMessage.getCssValue("background-color");
            displayed = displayed || (actualMessage.equals(message) && backgroundColor.equals(color));
        }
        
        return displayed;
    }
    
    public String getErrorMessage(){
        return $(errorMessageLabel_locator).getText();
    }
    
}
