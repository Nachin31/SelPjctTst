/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.pacientePageObjects;

import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class PacienteCreatePage extends Page {
    
    private By obrasocialAutoCompleteOpts_locator = By.cssSelector("#PacienteCreateForm\\:obraSocial_panel ul > li");
    
    public PacienteCreatePage(){
        super();
        add("TextField","Apellido",By.id("PacienteCreateForm:apellido"));
        add("TextField","Nombre",By.id("PacienteCreateForm:nombre"));
        add("TextField","DNI",By.id("PacienteCreateForm:dni"));
        add("TextField","Domicilio",By.id("PacienteCreateForm:domicilio"));
        add("TextField","Telefono",By.id("PacienteCreateForm:telefono"));
        add("TextField","Celular",By.id("PacienteCreateForm:celular"));
        add("TextField","FechaNacimiento",By.id("PacienteCreateForm:fechaDeNacimiento_input"));
        add("TextField","ObraSocial",By.id("PacienteCreateForm:obraSocial_input"));
        add("TextField","NumeroAfiliado",By.id("PacienteCreateForm:nroAfiliadoOS"));
        add("Label","Apellido",By.id("PacienteCreateForm:apellidoLbl"));
        add("Label","Nombre",By.id("PacienteCreateForm:nombreLbl"));
        add("Button","Cancelar",By.id("PacienteCreateForm:btnCancear"));
        add("Button","Guardar",By.id("PacienteCreateForm:btnGuardar"));
        setCloseArrowLocator(By.cssSelector("#PacienteCreateDlg a"));
    }
    
    public void selectObraSocial(String osName){
        
        Selenide.sleep(2000);
            
        List<SelenideElement> osListed = $$(obrasocialAutoCompleteOpts_locator);

        for(SelenideElement os : osListed){
            if(os.getText().equals(osName)){
                os.click();
                break;
            }
        }
        
    }
    
}
