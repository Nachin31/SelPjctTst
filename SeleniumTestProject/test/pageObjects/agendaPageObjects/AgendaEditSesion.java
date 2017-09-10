/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.agendaPageObjects;

import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class AgendaEditSesion extends Page{
    
    private By cuentaChckbox_locator = By.cssSelector("SesionEditForm:cuentaCheckBox");
    
    public AgendaEditSesion(){
        super();
        add("Label","Paciente",By.id("SesionEditForm:pacienteLblEditSesion"));
        add("Label","Tratamiento",By.id("SesionEditForm:tratamientoLblEditSesion"));
        add("Label","Numero de Sesion",By.id("SesionEditForm:numeroDeSesion"));
        add("Label","Transcurrida",By.id("SesionEditForm:transcurridaLblEditSesion"));
        add("TextField","Fecha",By.id("SesionEditForm:fechaHoraInicio_input"));
        add("Button","Eliminar",By.id("SesionEditForm:eliminarBtnEditSesion"));
        add("Button","Repetir Sesion",By.id("SesionEditForm:repetirBtnEditSesion"));
        add("Button","Guardar",By.id("SesionEditForm:guardarBtnEditSesion"));
        setCloseArrowLocator(By.cssSelector("#SesionEditDlg a"));
    }
    
    public void setCheckboxSelected(boolean state){
        SelenideElement checkboxCuenta = $(cuentaChckbox_locator);
        SelenideElement checkboxInput = checkboxCuenta.$(By.cssSelector("input"));
        if((checkboxInput.isSelected() && !state)
                || (!checkboxInput.isSelected() && state))
            checkboxCuenta.click();
            
    }
    
}
