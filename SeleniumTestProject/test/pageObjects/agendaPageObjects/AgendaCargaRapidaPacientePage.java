/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.agendaPageObjects;

import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class AgendaCargaRapidaPacientePage extends Page{
    
    public AgendaCargaRapidaPacientePage(){
        super();
        add("Label","Apellido",By.id("PacienteCreateCargaRapidaForm:apellidoLabel"));
        add("Label","Nombre",By.id("PacienteCreateCargaRapidaForm:nombreLabel"));
        add("Label","Telefono",By.id("PacienteCreateCargaRapidaForm:telefonoLabel"));
        add("Label","Celular",By.id("PacienteCreateCargaRapidaForm:celularLabel"));
        add("TextField","Apellido",By.id("PacienteCreateCargaRapidaForm:apellido"));
        add("TextField","Nombre",By.id("PacienteCreateCargaRapidaForm:nombre"));
        add("TextField","Telefono",By.id("PacienteCreateCargaRapidaForm:telefono"));
        add("TextField","Celular",By.id("PacienteCreateCargaRapidaForm:celular"));
        add("Button","Cancelar",By.id("PacienteCreateCargaRapidaForm:cancelarPacienteButton"));
        add("Button","Guardar",By.id("PacienteCreateCargaRapidaForm:guardarPacienteButton"));
        setCloseArrowLocator(By.cssSelector("#PacienteCreateCargaRapidaDlg a"));
    }
    
}
