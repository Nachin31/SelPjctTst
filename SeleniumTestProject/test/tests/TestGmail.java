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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Nacho Gómez
 */
public class TestGmail {
    
    private WebDriver driver;
    
    public TestGmail() {
           
    }

    @Test
    public void test_checkName(){
        
        GmailPage page = new GmailPage();
        
        page.setDriver(driver);
        page.setEmail("gomez.ignacio31@gmail.com");
        page.clickNext();
        page.setPassword("TU_PASS");
        page.clickSignIn();
        
        Assert.assertEquals(page.getUserName(),"Jose Ignacio");
        
    }

    @BeforeTest
    public void beforeTest() throws Exception {
        System.out.println("BeforeTest corrio");
        //Open the url which we want in firefox
        System.setProperty("webdriver.chrome.driver","src\\drivers\\chromedriver.exe");
        //Here we initialize the firefox webdriver
        driver=new ChromeDriver();
        
        driver.get("https://mail.google.com/mail/u/0/#inbox");
    }
    
}
