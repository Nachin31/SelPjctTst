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
public class SesionRepeatPage extends Page{
    
    private final int LUNES = 0;
    private final int MARTES = 1;
    private final int MIERCOLES = 2;
    private final int JUEVES = 3;
    private final int VIERNES = 4;
    private By checkboxesList_locator = By.cssSelector("#CargaMasivaForm\\:chckboxRepetirSesion .ui-chkbox-box.ui-widget");
    private By numerosVecesList_locator = By.cssSelector("#CargaMasivaForm\\:cantidadDeSesionesRepetirSesion_items li");
    
    public SesionRepeatPage(){
        super();
        add("TextField","Lunes",By.id("CargaMasivaForm:horaLunes_input"));
        add("TextField","Martes",By.id("CargaMasivaForm:horaMartes_input"));
        add("TextField","Miercoles",By.id("CargaMasivaForm:horaMiercoles_input"));
        add("TextField","Jueves",By.id("CargaMasivaForm:horaJueves_input"));
        add("TextField","Viernes",By.id("CargaMasivaForm:horaViernes_input"));
        add("Button","Numero de Veces",By.id("CargaMasivaForm:cantidadDeSesionesRepetirSesion"));
        add("Button","Cancelar",By.id("CargaMasivaForm:cancelarBtnRepetirSesion"));
        add("Button","Guardar",By.id("CargaMasivaForm:guardarBtnRepetirSesion"));
        setCloseArrowLocator(By.cssSelector("#SesionEditDlg a"));
    }
    
}
