/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Nacho Gómez
 */
public class GmailPage {
    
    private WebDriver driver;
    
    public void setDriver(WebDriver driver){
        this.driver = driver;
    }
    
    public String getUserName(){
        WebElement we = driver.findElement(By.cssSelector(".gb_P.gb_R"));
        return we.getText();
    }
    
    public void setEmail(String email){   
        driver.findElement(By.id("Email")).sendKeys(email);
    }
    
    public void clickNext(){
        driver.findElement(By.id("next")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GmailPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPassword(String password){
        driver.findElement(By.id("Passwd")).sendKeys(password);
    }
    
    public void clickSignIn(){
        driver.findElement(By.id("signIn")).click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GmailPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
