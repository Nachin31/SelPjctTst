/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.sesionPageObjects;

import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class SesionCreatePage extends Page{
    
    public SesionCreatePage(){
        super();
        add("Label","Numero de sesion",By.id("SesionCreateForm:numeroLblCreateSesion"));
        add("Label","Fecha",By.id("SesionCreateForm:fechaLabel"));        
        add("TextField","Fecha",By.id("SesionCreateForm:fechaHoraInicio_input"));
        add("Button","Guardar",By.id("SesionCreateForm:guardarBtnCreateSesion"));
        add("Button","Cancelar",By.id("SesionCreateForm:cancelarBtnCreateSesion"));
        setCloseArrowLocator(By.cssSelector("#SesionCreateDlg a"));
    }
    
}
