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
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;

/**
 *
 * @author Nacho Gómez
 */
public class TratamientoListPage extends Page {
    
    private By headers_locator = By.tagName("th");
    private By filasTabla_locator = By.cssSelector("#TratamientoListForm\\:datalist tr");
    private By paginatorNextPage_locator = By.cssSelector("#TratamientoListForm\\:datalist_paginator_bottom a[aria-label=Next\\ Page]");
    private By paginatorFirstPage_locator = By.cssSelector("#TratamientoListForm\\:datalist_paginator_bottom a[aria-label=Page\\ 1]");
    private By mensajeListaVacia_locator = By.className("ui-datatable-empty-message");
    
    public TratamientoListPage(){
        super();
        add("Table","Tratamientos",null,"TratamientoListForm");
        add("Button","AgregarTratamiento",By.id("TratamientoListForm:datalist:createButton"));
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
    
    public String[] buscarTratamiento(String tipoTratamiento,String sesiones){
        return buscarTratamiento(tipoTratamiento,sesiones,null);
    }
    
    //Este método asume que se busca un paciente de nombre único
    public String[] buscarTratamiento(String tipoTratamiento,String sesiones,String action){
        
        int pagina = 0;
        String[] tratamiento = null;
        
        $(paginatorFirstPage_locator).click();
        
        do{
            
            if(pagina!=0)
                $(paginatorNextPage_locator).click();
            
            Selenide.sleep(1000);
            
            List<SelenideElement> filas = $$(filasTabla_locator);
            filas = filas.subList(1,filas.size()); //Removemos el header de la talba}
            
            int indexOfOptions = $$(headers_locator).size()-1;
                    
            for(int j=0;j<filas.size() && tratamiento== null;j++){
                SelenideElement f = filas.get(j);
                if(f.$$(By.tagName("td")).get(1).getText().equals(tipoTratamiento)
                    && f.$$(By.tagName("td")).get(3).getText().split("/")[1].equals(sesiones)){
                    tratamiento = new String[7];
                    tratamiento[0] = f.$$(By.tagName("td")).get(0).getText();
                    tratamiento[1] = f.$$(By.tagName("td")).get(1).getText();
                    tratamiento[2] = f.$$(By.tagName("td")).get(2).getText();
                    tratamiento[3] = f.$$(By.tagName("td")).get(3).getText();
                    tratamiento[4] = f.$$(By.tagName("td")).get(4).getText();
                    tratamiento[5] = f.$$(By.tagName("td")).get(5).getText();
                    if(action==null);
                    else if(action.equals("Editar"))
                        f.$$(By.tagName("td")).get(indexOfOptions).$$(By.tagName("button")).get(0).click();
                    else if(action.equals("GenerarConsentimiento"))
                        f.$$(By.tagName("td")).get(indexOfOptions).$$(By.tagName("button")).get(1).click();
                    else if(action.equals("Eliminar"))
                        f.$$(By.tagName("td")).get(indexOfOptions).$$(By.tagName("button")).get(2).click();
                    else if(action.equals("Estudios"))
                        f.$$(By.tagName("td")).get(indexOfOptions).$$(By.tagName("button")).get(3).click();
                }
            }
            
            pagina++;
            
        }while(tratamiento==null && !$(paginatorNextPage_locator).getAttribute("class").contains("ui-state-disabled"));
        
        return tratamiento;
        
    }
    
}
