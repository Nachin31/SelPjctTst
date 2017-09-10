/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemTests.tests;

import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.open;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

/**
 *
 * @author Nacho Gómez
 */
public abstract class AutomatedTest {
    
    @BeforeSuite
    public void beforeSuite(){
        
    }
    
    @BeforeTest
    @Parameters({"browser","startUrl","reportsFolder","driverPath"})
    public void beforeTest(String browser,String startUrl,String reportsFolder,String driverPath) throws Exception {
        setUpBrowser(browser,driverPath);
        
        //General parameters
        Configuration.timeout = 20000;
        Configuration.reportsFolder = reportsFolder;
        
        //Open the url which we want in Chrome
        open(startUrl);
    }
    
    @AfterTest(alwaysRun = true)
    public void afterTest(){
        
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite(){
        
    }
 
    protected void setUpBrowser(String browser,String driverPath){
        if(browser.equals("Chrome")){
            System.setProperty("webdriver.chrome.driver",driverPath);
            System.setProperty("selenide.browser",browser);
        }
        //FUTURE IMPROVEMENTS: Support multiple  browsers
//        else if(browser.equals("Firefox")){
//            System.setProperty("webdriver.gecko.driver",driverPath);
//            System.setProperty("selenide.browser",browser);
//        }
        
    }
    
}
