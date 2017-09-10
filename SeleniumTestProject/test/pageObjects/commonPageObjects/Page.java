/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.commonPageObjects;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.SelenideElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nacho Gómez
 */
public class Page {
    
    protected Map<String,By> buttons;
    protected Map<String,By> inputs;
    protected Map<String,By> labels;
    protected Map<String,UITable> tables;
    private By confirmDeleteBtn_locator = By.cssSelector(".ui-confirm-dialog button");
    protected By toasterPopupDivs_locator = By.cssSelector("#growl_container > div");
    protected By insideToasterPopupDivIcon_locator = By.cssSelector("div > span");
    protected By popupCloseCross_locator;
    
    public Page(){
        buttons = new HashMap<String,By>();
        inputs = new HashMap<String,By>();
        labels = new HashMap<String,By>();
        tables = new HashMap<String,UITable>();
    }
    
    protected void setCloseArrowLocator(By crossLocator){
        popupCloseCross_locator = crossLocator;
    }
    
    protected void add(String elementType,String elementName,By elementLocator){
        add(elementType,elementName,elementLocator,null);
    }
    
    protected void add(String elementType,String elementName,By elementLocator,String... additionalParams){
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
            case "Table":
                if(additionalParams!=null){
                    if(additionalParams.length==2)
                        tables.put(elementName,new UITable(additionalParams[0],additionalParams[1]));
                    else if(additionalParams.length==1)
                        tables.put(elementName,new UITable(additionalParams[0],"datalist"));
                }
                break;
        }
    }
    
    public void completeTextField(String inputName,String text){
        SelenideElement input = $(inputs.get(inputName));
        input.clear();
        input.sendKeys(text);
        Selenide.sleep(500);
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
    
    public List<String[]> obtenerListaDeValoresTabla(String tableName,String ... columnasDeseadas){
       return tables.get(tableName).getValuesList(columnasDeseadas);
    }
    
    public boolean validarAccionHabilitada(String tableName,String action,String... columnasKeysAndValues){
        return tables.get(tableName).obtenerOperationEnabled(action,columnasKeysAndValues);
    }
    
    public String[] buscarElementoEnTabla(String tableName,String action,String... columnasKeysAndValues){
        return tables.get(tableName).buscarElemento(action,columnasKeysAndValues);
    }
    
    public String obtenerMensajeTablaVacia(String tableName){
        return tables.get(tableName).obtenerMensajeListaVacia();
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
        if($(popupCloseCross_locator).isDisplayed())
            $(popupCloseCross_locator).click();
    }
    
    public void confirmDelete(boolean confirm){
        if(confirm)
            $$(confirmDeleteBtn_locator).get(0).click();
        else
            $$(confirmDeleteBtn_locator).get(1).click();
    }
    
}
