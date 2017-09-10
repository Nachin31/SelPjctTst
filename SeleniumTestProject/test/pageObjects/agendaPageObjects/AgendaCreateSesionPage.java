/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.agendaPageObjects;

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
public class AgendaCreateSesionPage extends Page {
    
    private By pacientesList_locator = By.cssSelector("#SesionCreateForm\\:paciente_panel ul > li");
    private By tratamientosList_locator = By.cssSelector("#SesionCreateForm\\:tratamientoSeleccion_panel ul > li");
    
    public AgendaCreateSesionPage(){
        super();
        add("Label","Fecha",By.id("SesionCreateForm:fechaLabel"));
        add("Label","Paciente",By.id("SesionCreateForm:pacienteLabel"));
        add("Label","Tratamiento",By.id("SesionCreateForm:tratamientoLabel"));
        add("TextField","Fecha",By.id("SesionCreateForm:fechaHoraInicio_input"));
        add("TextField","Paciente",By.id("SesionCreateForm:paciente_input"));
        add("Button","Tratamientos",By.cssSelector("#SesionCreateForm\\:tratamientoSeleccion span"));
        add("Button","Carga rapida paciente",By.cssSelector("#SesionCreateForm\\:createPacienteButtonCargaRapida .ui-icon"));
        add("Button","Carga rapida tratamiento",By.cssSelector("#SesionCreateForm\\:createTratamientoButtonCargaRapida .ui-icon"));
        add("Button","Cancelar",By.id("SesionCreateForm:cancelarButton"));
        add("Button","Guardar",By.id("SesionCreateForm:guardarButton"));
        setCloseArrowLocator(By.cssSelector("#SesionCreateDlg a"));
    }
    
    public void selectPaciente(String pacienteName){
        
        Selenide.sleep(2000);
            
        List<SelenideElement> pListed = $$(pacientesList_locator);

        for(SelenideElement p : pListed){
            if(p.getText().equals(pacienteName)){
                p.click();
                break;
            }
        }
        
    }
    
    public void selectTratamiento(String name){
        
        Selenide.sleep(2000);
            
        List<SelenideElement> osListed = $$(tratamientosList_locator);

        for(SelenideElement os : osListed){
            if(os.getText().equals(name)){
                os.click();
                break;
            }
        }
        
    }
    
}
