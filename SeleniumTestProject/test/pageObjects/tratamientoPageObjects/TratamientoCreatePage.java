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

/**
 *
 * @author Nacho Gómez
 */
public class TratamientoCreatePage extends Page {
    
    private By tipoTratamientoSelect_locator = By.cssSelector("#TratamientoCreateForm\\:tipoDeTratamiento > div");//Index 2
    private By tipoTratamientoList_locator = By.cssSelector("#TratamientoCreateForm\\:tipoDeTratamiento_panel > div > ul > li");
    private By particularCheckboxes_locator = By.cssSelector("#TratamientoCreateForm\\:particular div");//Index 2 para Particular, Index 5 para Obra social
    private By selectedTipoTratamientoLabel_locator = By.id("TratamientoCreateForm:tipoDeTratamiento_label");
    
    public TratamientoCreatePage(){
        super();
        add("TextField","CantidadDeSesiones",By.id("TratamientoCreateForm:cantidadDeSesiones"));
        add("TextField","TipoDeTratamiento",By.id("TratamientoCreateForm:tipoDeTratamiento"));
        add("TextField","Diagnostico",By.id("TratamientoCreateForm:diagnostico"));
        add("TextField","Observaciones",By.id("TratamientoCreateForm:observaciones"));
        add("Label","TipoDeTratamiento",By.id("TratamientoCreateForm:tipoTratamientoLbl"));
        add("Label","CantidadDeSesiones",By.id("TratamientoCreateForm:cantidadDeSesionesLbl"));
        add("Label","Particular",By.id("TratamientoCreateForm:particularLbl"));
        add("Label","Diagnostico",By.id("TratamientoCreateForm:diagnosticoLbl"));
        add("Label","Observaciones",By.id("TratamientoCreateForm:observacionesLbl"));
        add("Button","Cancelar",By.id("TratamientoCreateForm:cancelarTratamientoCreateBtn"));
        add("Button","Guardar",By.id("TratamientoCreateForm:guardarTratamientoCreateBtn"));
        setCloseArrowLocator(By.cssSelector("#TratamientoCreateDlg a"));
    }
    
    public void selectTipoDeTratamiento(String tipoTratamiento){
        
        $$(tipoTratamientoSelect_locator).get(2).click();
        
        Selenide.sleep(1000);
            
        List<SelenideElement> tiposListados = $$(tipoTratamientoList_locator);

        for(SelenideElement tt : tiposListados){
            if(tt.getText().equals(tipoTratamiento)){
                tt.click();
                break;
            }
        }
        
    }
    
    public boolean getSelectOptionsEnabled(){
        
        Selenide.sleep(500);
        
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
