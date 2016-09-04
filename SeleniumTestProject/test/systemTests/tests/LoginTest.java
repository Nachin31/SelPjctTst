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
import pageObjects.CommonElementsPage;
import pageObjects.LoginPage;

/**
 *
 * @author Nacho Gómez
 */
public class LoginTest {
    
    private LoginPage loginPage;
    private CommonElementsPage commonElementsPage;
    
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
        
        loginPage = new LoginPage();
        commonElementsPage = new CommonElementsPage();

    }
    
    @Test
    @Parameters({"username"})
    public void test_loginUnsuccessful_EmptyFields(String username){
        
        loginPage.clickButton("IniciarSesion");
        
        //Cantidad de popups
        Assert.assertEquals(loginPage.getToasterMessageCount(),2);
        
        //Estilo y texto de cada mensaje de error
        Assert.assertTrue(loginPage.messageDisplayed("Error","Ingrese el usuario"));
        Assert.assertTrue(loginPage.messageDisplayed("Error","Ingrese la contraseña"));
        
        //Error en los inputs
        Assert.assertEquals(loginPage.getErrorMessage(),"Error al iniciar sesión. Intente nuevamente");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Password","border-bottom"),"1px solid rgb(240, 51, 105)");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Username","border-bottom"),"1px solid rgb(240, 51, 105)");
        
        //Texto en los inputs
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Username","value"),"");
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Password","value"),"");
        
    }
    
    @Test
    @Parameters({"username"})
    public void test_loginUnsuccessful_EmptyField(String username){
        
        loginPage.completeTextField("Username",username);
        loginPage.clickButton("IniciarSesion");
        
        //Cantidad de popups
        Assert.assertEquals(loginPage.getToasterMessageCount(),1);
        
        //Estilo y texto de cada mensaje de error
        Assert.assertTrue(loginPage.messageDisplayed("Error","Ingrese la contraseña"));
        
        //Error en los inputs
        Assert.assertEquals(loginPage.getErrorMessage(),"Error al iniciar sesión. Intente nuevamente");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Username","border-bottom"),"1px solid rgb(176, 190, 197)");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Password","border-bottom"),"1px solid rgb(240, 51, 105)");
 
        //Texto en los inputs
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Username","value"),username);
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Password","value"),"");
        
    }

    @Test
    @Parameters({"wrongUsername","password"})
    public void test_loginUnsuccessful_UnexistentUser(String wrongUsername,String password){
        
        loginPage.completeTextField("Username",wrongUsername);
        loginPage.completeTextField("Password",password);
        loginPage.clickButton("IniciarSesion");
        
        //Cantidad de popups
        Assert.assertEquals(loginPage.getToasterMessageCount(),0);
        
        //Error en los inputs
        Assert.assertEquals(loginPage.getErrorMessage(),"Error al iniciar sesión. Intente nuevamente");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Username","border-bottom"),"1px solid rgb(3, 169, 244)");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Password","border-bottom"),"1px solid rgb(176, 190, 197)");
 
        //Texto en los inputs
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Username","value"),wrongUsername);
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Password","value"),"");
        
    }
    
    @Test
    @Parameters({"username","wrongPassword"})
    public void test_loginUnsuccessful_WrongPassword(String username,String wrongPassword){
        
        loginPage.completeTextField("Username",username);
        loginPage.completeTextField("Password",wrongPassword);
        loginPage.clickButton("IniciarSesion");
        
        //Cantidad de popups
        Assert.assertEquals(loginPage.getToasterMessageCount(),0);
        
        //Error en los inputs
        Assert.assertEquals(loginPage.getErrorMessage(),"Error al iniciar sesión. Intente nuevamente");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Username","border-bottom"),"1px solid rgb(3, 169, 244)");
        Assert.assertEquals(loginPage.getCssAttribute("TextField","Password","border-bottom"),"1px solid rgb(176, 190, 197)");
 
        //Texto en los inputs
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Username","value"),username);
        Assert.assertEquals(loginPage.getHtmlAttribute("TextField","Password","value"),"");
        
    }
    
    @Test
    @Parameters({"username","password"})
    public void test_loginSuccessful(String username,String password){
        
        loginPage.completeTextField("Username",username);
        loginPage.completeTextField("Password",password);
        loginPage.clickButton("IniciarSesion");
        
        Assert.assertEquals(commonElementsPage.getUserName(),username);
        Assert.assertEquals(commonElementsPage.getPageMenuItems(),"Home>Agenda");
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite(){
        
    }
    
}
