/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.agendaPageObjects;

import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$;
import java.util.Calendar;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class AgendaPage extends Page {
    
    private By listOfHours_locator = By.cssSelector(".fc-axis.fc-time span");
    private By listOfDays_locator = By.cssSelector(".fc-day-header");
    
    public AgendaPage(){
        super();
        add("TextField","Fecha",By.id("AgendaForm:sesionDateHiddenInput"));
        add("Button","Nueva Sesion",By.id("AgendaForm:createSesionFromAgendaHiddenBtn"));
    }
    
}
