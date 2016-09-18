/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects;

import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;

/**
 *
 * @author Nacho Gómez
 */
public class PacienteListPage extends Page {
    
    private By headers_locator = By.tagName("th");
    private By filasTabla_locator = By.cssSelector("#PacienteListForm\\:datalist tr");
    private By paginatorNextPage_locator = By.cssSelector("#PacienteListForm\\:datalist_paginator_bottom a[aria-label=Next\\ Page]");
    private By paginatorFirstPage_locator = By.cssSelector("#PacienteListForm\\:datalist_paginator_bottom a[aria-label=Page\\ 1]");
    private By mensajeListaVacia_locator = By.className("ui-datatable-empty-message");
    private By toasterPopupDivs_locator = By.cssSelector("#growl_container > div");
    private By confirmDeleteButtons_locator = By.cssSelector(".ui-confirm-dialog button");
    
    public PacienteListPage(){
        super();
        add("TextField","FiltroPaciente",By.id("PacienteListForm:datalist:globalFilter"));
        add("Button","AgregarPaciente",By.id("PacienteListForm:datalist:createButton"));
    }
    
    public List<String[]> getValuesList(String ... columnasDeseadas){
        
        List<SelenideElement> columnasTabla = $$(headers_locator);
        String [] stringsColumnasTabla = new String[columnasTabla.size()];
        Integer [] indiceColumnaDeseada = new Integer[columnasDeseadas.length];
        
        for(int i=0;i<columnasTabla.size();i++) stringsColumnasTabla[i] = columnasTabla.get(i).getText();
        
        for(int i=0;i<stringsColumnasTabla.length;i++)
            for(int j=0;j<columnasDeseadas.length;j++)
                if(stringsColumnasTabla[i].equals(columnasDeseadas[j]))
                    indiceColumnaDeseada[j]=i;
        
        return obtenerColumnasDeseadas(indiceColumnaDeseada);
        
    }
    
    private List<String[]> obtenerColumnasDeseadas(Integer [] indicesColumnaDeseada){
        
        $(paginatorFirstPage_locator).click();
        
        int pagina = 0;
        List<String[]> listaPacientes = new ArrayList<String[]>();
        
        do{
            if(pagina!=0)
                $(paginatorNextPage_locator).click();
                
            List<SelenideElement> filas = $$(filasTabla_locator);
            filas = filas.subList(1,filas.size()); //Removemos el header de la talba}
        
            for(int j=0;j<filas.size();j++){
                SelenideElement f = filas.get(j);
                String[] valoresDeseadosFila = new String[indicesColumnaDeseada.length];
                for(int i=0;i<valoresDeseadosFila.length;i++)
                    valoresDeseadosFila[i] = f.$$(By.tagName("td")).get(indicesColumnaDeseada[i]).getText();
                listaPacientes.add(valoresDeseadosFila);
            }
  
            pagina++;
            
        }while(!$(paginatorNextPage_locator).getAttribute("class").contains("ui-state-disabled"));
        
        return listaPacientes;
        
    }
    
    public String obtenerMensajeListaVacia(){
        String msj = "";
        
        msj = $(mensajeListaVacia_locator).getText();
        
        return msj;
    }
    
    public String[] buscarPaciente(String nombreyap){
        return buscarPaciente(nombreyap,null);
    }
    
    //Este método asume que se busca un paciente de nombre único
    public String[] buscarPaciente(String nombreyap,String action){
        
        int pagina = 0;
        String[] paciente = null;
        
        $(paginatorFirstPage_locator).click();
        
        do{
            
            if(pagina!=0)
                $(paginatorNextPage_locator).click();
            
            Selenide.sleep(1000);
            
            List<SelenideElement> filas = $$(filasTabla_locator);
            filas = filas.subList(1,filas.size()); //Removemos el header de la talba}
                    
            for(int j=0;j<filas.size() && paciente== null;j++){
                SelenideElement f = filas.get(j);
                if(f.$$(By.tagName("td")).get(0).getText().equals(nombreyap)){
                    paciente = new String[6];
                    paciente[0] = f.$$(By.tagName("td")).get(0).getText();
                    paciente[1] = f.$$(By.tagName("td")).get(1).getText();
                    paciente[2] = f.$$(By.tagName("td")).get(2).getText();
                    paciente[3] = f.$$(By.tagName("td")).get(3).getText();
                    paciente[4] = f.$$(By.tagName("td")).get(4).getText();
                    paciente[5] = f.$$(By.tagName("td")).get(5).getText();
                    if(action==null);
                    else if(action.equals("Editar"))
                        f.$$(By.tagName("td")).get(6).$$(By.tagName("button")).get(0).click();
                    else if(action.equals("Tratamientos"))
                        f.$$(By.tagName("td")).get(6).$$(By.tagName("button")).get(1).click();
                    else if(action.equals("Eliminar"))
                        f.$$(By.tagName("td")).get(6).$$(By.tagName("button")).get(2).click();
                }
            }
            
            pagina++;
            
        }while(!$(paginatorNextPage_locator).getAttribute("class").contains("ui-state-disabled") && paciente==null);
        
        return paciente;
        
    }
    
    public void confirmPacienteDelete(boolean confirm){
        if(confirm)
            $$(confirmDeleteButtons_locator).get(0).click();
        else
            $$(confirmDeleteButtons_locator).get(1).click();
    }
    
}
