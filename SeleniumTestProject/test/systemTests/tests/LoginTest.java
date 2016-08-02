package systemTests.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.Parameters;
import pageObjects.LoginPage;

/**
 *
 * @author Nacho Gómez
 */
public class LoginTest {
    
    @BeforeSuite
    public void beforeSuite() throws Exception {
        //We set driver parameters
        System.setProperty("webdriver.chrome.driver","src\\drivers\\chromedriver.exe");
        System.setProperty("selenide.browser", "Chrome");
        
        //General parameters
        Configuration.timeout = 20000;
        Configuration.reportsFolder = "F:\\";
        
        //Open the url which we want in Chrome
        open("http://localhost:40053/pfc-gmz/");

    }

    @Test
    @Parameters({"username","password"})
    public void test_loginSuccessful(String username,String password){
        
        LoginPage page = new LoginPage();
        
        page.completeTextField("Username",username);
        page.completeTextField("Password",password);
        page.clickButton("IniciarSesion");
        
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite(){ }
    
}
