/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects;

import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import org.openqa.selenium.By;

/**
 *
 * @author Nacho Gómez
 */
public class TratamientoEditPage extends Page {
    
    private By ordenMedicaPaginatorNextPage_locator = By.cssSelector("#OrdenTratamientoListForm\\:ordenMedicasTratamientoList_paginator_bottom a[aria-label=Next\\ Page]");
    private By ordenMedicaPaginatorFirstPage_locator = By.cssSelector("#OrdenTratamientoListForm\\:ordenMedicasTratamientoList_paginator_bottom a[aria-label=Page\\ 1]");
    private By sesionPaginatorNextPage_locator = By.cssSelector("#SesionListForm\\:datalist_paginator_bottom a[aria-label=Next\\ Page]");
    private By sesionPaginatorFirstPage_locator = By.cssSelector("#SesionListForm\\:datalist_paginator_bottom a[aria-label=Page\\ 1]");
    private By checkboxFinalizado_Locator = By.cssSelector("#TratamientoEditForm\\:finalizado div");//Index 1
    
    public TratamientoEditPage(){
        super();
        add("TextField","Diagnostico",By.id("TratamientoEditForm:diagnostico"));
        add("TextField","MedicoDerivante",By.id("TratamientoEditForm:medicoderivante"));
        add("TextField","CantidadDeSesiones",By.id("TratamientoEditForm:cantidadDeSesiones"));
        add("TextField","Observaciones",By.id("TratamientoEditForm:observaciones"));
        add("Label","TipoTratamiento",By.id("TratamientoEditForm:tipoTratamiento"));
        add("Label","Particular",By.id("TratamientoEditForm:particularl"));
        add("Button","Cancelar",By.id("TratamientoEditForm:cancelEditTratamiento"));
        add("Button","Guardar",By.id("TratamientoEditForm:confirmEditTratamiento"));
        add("Button","CrearOrdenMedica",By.id("OrdenTratamientoListForm:ordenMedicasTratamientoList:ordenMedicaCreateButton"));
        add("Button","CrearSesion",By.id("SesionListForm:datalist:createButton"));
        
    }
    
}
