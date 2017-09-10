/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.tratamientoPageObjects;

import pageObjects.commonPageObjects.Page;
import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

/**
 *
 * @author Nacho Gómez
 */
public class TratamientoEditPage extends Page {
    
    private By checkboxFinalizado_Locator = By.id("TratamientoEditForm:finalizado");
    private By noOSWarningMessage_locator = By.cssSelector("#messages > div");
    
    public TratamientoEditPage(){
        super();
        add("TextField","Diagnostico",By.id("TratamientoEditForm:diagnostico"));
        add("TextField","MedicoDerivante",By.id("TratamientoEditForm:medicoderivante"));
        add("TextField","CantidadDeSesiones",By.id("TratamientoEditForm:cantidadDeSesiones"));
        add("TextField","Observaciones",By.id("TratamientoEditForm:observaciones"));
        add("Label","TipoTratamiento",By.id("TratamientoEditForm:tipoTratamiento"));
        add("Label","Particular",By.id("TratamientoEditForm:particular"));
        add("Label","CantidadDeSesiones",By.id("TratamientoEditForm:cantidadDeSesionesLbl"));
        add("Label","Finalizado",By.id("TratamientoEditForm:labelFinalizado"));
        add("Button","Cancelar",By.id("TratamientoEditForm:cancelEditTratamiento"));
        add("Button","Guardar",By.id("TratamientoEditForm:confirmEditTratamiento"));
        
        add("Button","Nueva Orden Medica",By.id("OrdenTratamientoListForm:ordenMedicasTratamientoList:ordenMedicaCreateButton"));
        add("Button","Nueva Sesion",By.id("SesionListForm:datalist:createButton"));
        add("Table","Ordenes",null,"OrdenTratamientoListForm","ordenMedicasTratamientoList");
        add("Table","Sesiones",null,"SesionListForm");
    }
    
    public boolean checkMensajeNoObraSocial(String message){
        boolean correctMessage;
        boolean correctColor;
        SelenideElement messageDiv = $(noOSWarningMessage_locator);
        if(messageDiv.exists()){
            String divMessage = messageDiv.getText();
            String divColor = messageDiv.getCssValue("background-color");
            correctMessage = divMessage.equals(message);
            correctColor = divColor.equals("rgba(255, 167, 38, 1)");
            return (correctMessage && correctColor);
        }
        else
            return false;
    }
    
    public boolean isOrdenMedicaListDisplayed(){
        return $(By.id("OrdenTratamientoListForm")).exists();
    }
    
    public void setFinalizadoCheckboxSelected(boolean state){
        SelenideElement checkboxCuenta = $(checkboxFinalizado_Locator);
        SelenideElement checkboxInput = checkboxCuenta.$(By.cssSelector("input"));
        if((checkboxInput.isSelected() && !state)
                || (!checkboxInput.isSelected() && state))
            checkboxCuenta.click();
            
    }
    
    public boolean getFinzalizadoCheckboxEnabled(){
        return $(checkboxFinalizado_Locator).$(By.cssSelector("input")).isEnabled();
    }
    
}
