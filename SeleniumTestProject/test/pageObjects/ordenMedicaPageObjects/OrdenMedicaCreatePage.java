/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.ordenMedicaPageObjects;

import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class OrdenMedicaCreatePage extends Page {
    
    public OrdenMedicaCreatePage(){
        super();
        add("TextField","Cantidad de Sesiones",By.id("ordenMedicaCreateForm:cantidadDeSesiones"));
        add("Button","Cancelar",By.id("ordenMedicaCreateForm:btnCancelar"));
        add("Button","Guardar",By.id("ordenMedicaCreateForm:btnGuardar"));
        setCloseArrowLocator(By.cssSelector("#ordenMedicaCreateDlg a"));
    }

    
    
}
