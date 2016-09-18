/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.SelenideElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nacho Gómez
 */
public class Page {
    
    protected Map<String,By> buttons;
    protected Map<String,By> inputs;
    protected Map<String,By> labels;
    protected By toasterPopupDivs_locator = By.cssSelector("#growl_container > div");
    protected By insideToasterPopupDivIcon_locator = By.cssSelector("div > span");
    protected By popupCloseCross_locator;
    
    public Page(){
        buttons = new HashMap<String,By>();
        inputs = new HashMap<String,By>();
        labels = new HashMap<String,By>();
    }
    
    protected void setCloseArrowLocator(By crossLocator){
        popupCloseCross_locator = crossLocator;
    }
    
    protected void add(String elementType,String elementName,By elementLocator){
        switch(elementType){
            case "Button":
                buttons.put(elementName,elementLocator);
                break;
            case "TextField":
                inputs.put(elementName,elementLocator);
                break;
            case "Label":
                labels.put(elementName,elementLocator);
                break;                
        }
    }
    
    public void completeTextField(String inputName,String text){
        SelenideElement input = $(inputs.get(inputName));
        input.clear();
        input.sendKeys(text);
    }
    
    public void clickButton(String buttonName){
        SelenideElement button = $(buttons.get(buttonName));
        button.click();
    }
    
    public String getTextInTextField(String textFieldName){
        return getHtmlAttribute("TextField",textFieldName,"value");
    }
    
    public String getTextInLabel(String labelName){
        SelenideElement label = $(labels.get(labelName));
        return label.getText();
    }
    
    public String getCssAttribute(String type,String name,String property){
        SelenideElement element = null;
        
        switch(type){
            case "Button":
                element = $(buttons.get(name));
                break;
            case "TextField":
                element = $(inputs.get(name));
                break;
            case "Label":
                element = $(labels.get(name));
                break;  
        }
        
        return element.getCssValue(property);
        
    }
    
    public String getHtmlAttribute(String type,String name,String property){
        SelenideElement element = null;
        
        switch(type){
            case "Button":
                element = $(buttons.get(name));
                break;
            case "TextField":
                element = $(inputs.get(name));
                break;
            case "Label":
                element = $(labels.get(name));
                break;
        }
        
        return element.getAttribute(property);
        
    }
    
    public boolean toasterMessageDisplayed(String messageType, String message){
        return toasterMessageDisplayed(messageType,message,0);
    }
    
    public boolean toasterMessageDisplayed(String messageType, String message,int wait){
        
        Selenide.sleep(wait*1000);
        
        boolean displayed = false;
        String color = "";
        switch(messageType){
            case "Error":
                color = "rgba(239, 83, 80, 1)";
                break;
            case "Success":
                color = "rgba(156, 204, 101, 1)";
                break;
        }
        
        List<SelenideElement> toasterMessages = $$(toasterPopupDivs_locator);
        
        for(SelenideElement toasterMessage : toasterMessages){
            String actualMessage = toasterMessage.getText();
            String backgroundColor = toasterMessage.getCssValue("background-color");
            displayed = displayed || (actualMessage.equals(message) && backgroundColor.equals(color));
        }
        
        return displayed;
    }
    
    public int getToasterMessageCount(){     
        return getToasterMessageCount(0);
    }
    
    public int getToasterMessageCount(int wait){     
        int count = 0;
        Selenide.sleep(wait*1000);
        count += $$(toasterPopupDivs_locator).size();
        
        return count;
    }
    
    public void cerrarPopup(){
        $(popupCloseCross_locator).click();
    }
    
}
