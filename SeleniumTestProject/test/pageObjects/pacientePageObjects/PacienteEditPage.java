/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.pacientePageObjects;

import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class PacienteEditPage extends Page {
    
    private By obrasocialAutoCompleteOpts_locator = By.cssSelector("#PacienteEditForm\\:obraSocial_panel ul > li");
    
    public PacienteEditPage(){
        super();
        add("TextField","Apellido",By.id("PacienteEditForm:apellido"));
        add("TextField","Nombre",By.id("PacienteEditForm:nombre"));
        add("TextField","DNI",By.id("PacienteEditForm:dni"));
        add("TextField","Domicilio",By.id("PacienteEditForm:domicilio"));
        add("TextField","Telefono",By.id("PacienteEditForm:telefono"));
        add("TextField","Celular",By.id("PacienteEditForm:celular"));
        add("TextField","FechaNacimiento",By.id("PacienteEditForm:fechaDeNacimiento_input"));
        add("TextField","ObraSocial",By.id("PacienteEditForm:obraSocial_input"));
        add("TextField","NumeroAfiliado",By.id("PacienteEditForm:nroAfiliadoOS"));
        add("Label","Apellido",By.id("PacienteEditForm:apellidoLbl"));
        add("Label","Nombre",By.id("PacienteEditForm:nombreLbl"));
        add("Button","Cancelar",By.id("PacienteEditForm:btnCancelar"));
        add("Button","Guardar",By.id("PacienteEditForm:btnGuardar"));
        setCloseArrowLocator(By.cssSelector("#PacienteEditDlg a"));
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
