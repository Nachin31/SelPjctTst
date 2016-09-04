/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects;

import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.SelenideElement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nacho Gómez
 */
public class Page {
    
    protected Map<String,By> buttons;
    protected Map<String,By> inputs;
    
    public Page(){
        buttons = new HashMap<String,By>();
        inputs = new HashMap<String,By>();
    }
    
    protected void add(String elementType,String elementName,By elementLocator){
        switch(elementType){
            case "Button":
                buttons.put(elementName,elementLocator);
                break;
            case "TextField":
                inputs.put(elementName,elementLocator);
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
    
    public String getCssAttribute(String type,String name,String property){
        SelenideElement element = null;
        
        switch(type){
            case "Button":
                element = $(buttons.get(name));
                break;
            case "TextField":
                element = $(inputs.get(name));
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
        }
        
        return element.getAttribute(property);
        
    }
    
}
