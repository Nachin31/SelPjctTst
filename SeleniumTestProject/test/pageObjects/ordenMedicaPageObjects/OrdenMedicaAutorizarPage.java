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
public class OrdenMedicaAutorizarPage extends Page {
    
    public OrdenMedicaAutorizarPage(){
        super();
        add("TextField","Codigo de autorizacion",By.id("OrdenMedicaAutorizarForm:codigoAutorizacion"));
        add("Label","Codigo de autorizacion",By.id("OrdenMedicaAutorizarForm:lblCodigoAutorizacion"));
        add("Label","Paciente",By.id("OrdenMedicaAutorizarForm:paciente"));
        add("Label","Tratamiento",By.id("OrdenMedicaAutorizarForm:tratamiento"));
        add("Label","Codigo de prestacion",By.id("OrdenMedicaAutorizarForm:codigoPrestacion"));
        add("Label","Obra social",By.id("OrdenMedicaAutorizarForm:obraSocial"));
        add("Label","Numero de afiliado",By.id("OrdenMedicaAutorizarForm:numeroAfiliadoPaciente"));
        add("Label","Cantidad de sesiones",By.id("OrdenMedicaAutorizarForm:cantidadDeSesiones"));
        add("Button","Cancelar",By.id("OrdenMedicaAutorizarForm:btnCancelar"));
        add("Button","Guardar",By.id("OrdenMedicaAutorizarForm:btnGuardar"));
        setCloseArrowLocator(By.cssSelector("#OrdenMedicaAutorizarDlg a"));
    }
    
}
