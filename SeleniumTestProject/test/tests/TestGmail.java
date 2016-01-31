package tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import pages.GmailPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 *
 * @author Nacho Gómez
 */
public class TestGmail {
    
    private WebDriver driver;
    
    @BeforeSuite
    public void beforeSuite() throws Exception {
        //Open the url which we want in firefox
        System.setProperty("webdriver.chrome.driver","src\\drivers\\chromedriver.exe");
        //Here we initialize the firefox webdriver
        driver=new ChromeDriver();
        
        driver.get("https://mail.google.com/mail/u/0/#inbox");
    }

    @Test
    public void test_checkName(){
        
        GmailPage page = new GmailPage();
        
        page.setDriver(driver);
        page.setEmail("TU EMAIL");
        page.clickNext();
        page.setPassword("TU PASS");
        page.clickSignIn();
        
        Assert.assertEquals(page.getUserName(),"NOMBRE ESPERADO");
        
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite(){
        driver.quit();
    }
    
}
