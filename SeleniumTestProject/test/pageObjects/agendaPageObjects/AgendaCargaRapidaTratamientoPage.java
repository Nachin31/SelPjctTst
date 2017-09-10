/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.agendaPageObjects;

import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class AgendaCargaRapidaTratamientoPage extends Page{
    
    private By tipoTratamientosList_locator = By.cssSelector("#TratamientoCreateCargaRapidaForm\\:tipoDeTratamiento_items > li");
    private By selectedTipoTratamientoLabel_locator = By.id("TratamientoCreateCargaRapidaForm:tipoDeTratamiento_label");
    private By particularCheckboxes_locator = By.cssSelector("#TratamientoCreateCargaRapidaForm\\:particular div");//Index 2 para Particular, Index 5 para Obra social
    
    public AgendaCargaRapidaTratamientoPage(){
        super();
        add("Label","Tipo de tratamiento",By.id("TratamientoCreateCargaRapidaForm:tipoTratamientoLabel"));
        add("Label","Cantidad de sesiones",By.id("TratamientoCreateCargaRapidaForm:cantidadDeSesionesLabel"));
        add("Label","Diagnostico",By.id("TratamientoCreateCargaRapidaForm:diagnosticoLabel"));
        add("Button","Tipo de tratamiento",By.cssSelector("#TratamientoCreateCargaRapidaForm\\:tipoDeTratamiento span"));
        add("TextField","Cantidad de sesiones",By.id("TratamientoCreateCargaRapidaForm:cantidadDeSesiones"));        
        add("TextField","Diagnostico",By.id("TratamientoCreateCargaRapidaForm:diagnostico"));        
        add("Button","Cancelar",By.id("TratamientoCreateCargaRapidaForm:cancelarTratamientoButton"));
        add("Button","Guardar",By.id("TratamientoCreateCargaRapidaForm:guardarTratamientoButton"));
        setCloseArrowLocator(By.cssSelector("#TratamientoCreateDlg a"));
    }
    
    public void selectTipoTratamiento(String name){
        
        Selenide.sleep(2000);
            
        List<SelenideElement> osListed = $$(tipoTratamientosList_locator);

        for(SelenideElement os : osListed){
            if(os.getText().equals(name)){
                os.click();
                break;
            }
        }
        
    }
    
    public boolean getSelectOptionsEnabled(){
        
        boolean particularEnabled = false;
        boolean osEnabled = false;
        
        SelenideElement particularRadioBtn = $$(particularCheckboxes_locator).get(2);
        SelenideElement obraSocialRadioBtn = $$(particularCheckboxes_locator).get(5);
        
        particularEnabled = !particularRadioBtn.getAttribute("class").contains("ui-state-disabled");
        osEnabled = !obraSocialRadioBtn.getAttribute("class").contains("ui-state-disabled");
        
        
        return particularEnabled && osEnabled;
        
    }
    
    public void setCheckboxSelected(String nombre,boolean enabled){
        
        SelenideElement radioBtn = null;
        String selectedColor = "rgba(38, 198, 218, 1)";
        
        switch(nombre){
            case "Particular":
                radioBtn = $$(particularCheckboxes_locator).get(2);
                break;
            case "Obra Social":
                radioBtn = $$(particularCheckboxes_locator).get(5);
                break;
        }
        
        //Si no esta seleccionado y me dicen que lo seleccione, lo clickeo
        if( !radioBtn.$("span").getCssValue("background-color").equals(selectedColor) && enabled )
            radioBtn.click();        
        
    }
    
    public String getSelectedTratamiento(){
        return $(selectedTipoTratamientoLabel_locator).getText();
    }
    
}
