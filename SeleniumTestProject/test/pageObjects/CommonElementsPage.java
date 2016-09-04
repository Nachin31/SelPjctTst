/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects;

import static com.codeborne.selenide.Selenide.$;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.util.List;

/**
 *
 * @author Nacho Gómez
 */
public class CommonElementsPage {
    
    private By userNameLabel_locator = By.className("nombrePerfilUsuario");
    private By userRoleLabel_locator = By.className("rolUsuario");
    private By pageMenuDiv_locator = By.cssSelector(".ui-menuitem-text");
    
    public String getUserName(){
        return $(userNameLabel_locator).getText();
    }
    
    public String getUserRole(){
        return $(userRoleLabel_locator).getText();
    }
    
    public String getPageMenuItems(){
        List<SelenideElement> pageMenuItems = $$(pageMenuDiv_locator);
        String menuItems = "";
        
        for(SelenideElement mi : pageMenuItems){
            menuItems += mi.getText() + ">";
        }
        
        if(!menuItems.equals(""))
            menuItems = menuItems.substring(0,menuItems.length()-1);
        
        return menuItems;
    }
    
}
