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
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import pageObjects.commonPageObjects.Page;

/**
 *
 * @author Nacho Gómez
 */
public class PacienteListPage extends Page {
    
    
    
    public PacienteListPage(){
        super();
        add("TextField","FiltroPaciente",By.id("PacienteListForm:datalist:globalFilter"));
        add("Button","AgregarPaciente",By.id("PacienteListForm:datalist:createButton"));
        add("Table","Pacientes",null,"PacienteListForm");
    }
    
    public void clickToOrderByName(){
        $(By.cssSelector("table th")).click();
    }
    
//    public String[] buscarPaciente(String nombreyap){
//        return buscarPaciente(nombreyap,null);
//    }
//    
//    //Este método asume que se busca un paciente de nombre único
//    public String[] buscarPaciente(String nombreyap,String action){
//        
//        int pagina = 0;
//        String[] paciente = null;
//        
//        $(paginatorFirstPage_locator).click();
//        
//        do{
//            
//            if(pagina!=0)
//                $(paginatorNextPage_locator).click();
//            
//            Selenide.sleep(1000);
//            
//            List<SelenideElement> filas = $$(filasTabla_locator);
//            filas = filas.subList(1,filas.size()); //Removemos el header de la talba}
//                    
//            for(int j=0;j<filas.size() && paciente== null;j++){
//                SelenideElement f = filas.get(j);
//                if(f.$$(By.tagName("td")).get(0).getText().equals(nombreyap)){
//                    paciente = new String[6];
//                    paciente[0] = f.$$(By.tagName("td")).get(0).getText();
//                    paciente[1] = f.$$(By.tagName("td")).get(1).getText();
//                    paciente[2] = f.$$(By.tagName("td")).get(2).getText();
//                    paciente[3] = f.$$(By.tagName("td")).get(3).getText();
//                    paciente[4] = f.$$(By.tagName("td")).get(4).getText();
//                    paciente[5] = f.$$(By.tagName("td")).get(5).getText();
//                    if(action==null);
//                    else if(action.equals("Editar"))
//                        f.$$(By.tagName("td")).get(6).$$(By.tagName("button")).get(0).click();
//                    else if(action.equals("Tratamientos"))
//                        f.$$(By.tagName("td")).get(6).$$(By.tagName("button")).get(1).click();
//                    else if(action.equals("Eliminar"))
//                        f.$$(By.tagName("td")).get(6).$$(By.tagName("button")).get(2).click();
//                }
//            }
//            
//            pagina++;
//            
//        }while(paciente==null && !$(paginatorNextPage_locator).getAttribute("class").contains("ui-state-disabled"));
//        
//        return paciente;
//        
//    }
    
}
