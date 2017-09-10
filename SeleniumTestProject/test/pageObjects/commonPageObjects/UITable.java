/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.commonPageObjects;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.SelenideElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nacho Gómez
 */
public class UITable {
    
    private By headers_locator;
    private By filasTabla_locator;
    private By paginatorNextPage_locator;
    private By paginatorFirstPage_locator;
    private By mensajeListaVacia_locator;
    private int dontIncludeLastColumn;
    
    public UITable(String containerFormId,String dataListId){
        filasTabla_locator = By.cssSelector("#"+containerFormId+"\\:"+dataListId+" tr");
        paginatorNextPage_locator = By.cssSelector("#"+containerFormId+"\\:"+dataListId+"_paginator_bottom a[aria-label=Next\\ Page]");
        paginatorFirstPage_locator = By.cssSelector("#"+containerFormId+"\\:"+dataListId+"_paginator_bottom a[aria-label=Page\\ 1]");
        mensajeListaVacia_locator = By.cssSelector("#"+containerFormId+"\\:"+dataListId+"_data .ui-datatable-empty-message");
        headers_locator = By.cssSelector("#"+containerFormId+"\\:"+dataListId+"_head th");
    }
    
    private Integer[] getIndicesColumnas(String[] columnasDeseadas){
        List<SelenideElement> columnasTabla = $$(headers_locator);
        String [] stringsColumnasTabla = new String[columnasTabla.size()];
        Integer [] indiceColumnaDeseada = new Integer[columnasDeseadas.length];
        
        for(int i=0;i<columnasTabla.size();i++) stringsColumnasTabla[i] = columnasTabla.get(i).getText();
        
        for(int i=0;i<stringsColumnasTabla.length;i++)
            for(int j=0;j<columnasDeseadas.length;j++)
                if(stringsColumnasTabla[i].equals(columnasDeseadas[j]))
                    indiceColumnaDeseada[j]=i;
        
        return indiceColumnaDeseada;
    }
    
    private List<String[]> obtenerValoresColumnasDeseadas(Integer [] indicesColumnaDeseada){
        
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
    
    public List<String[]> getValuesList(String ... columnasDeseadas){
        Integer[] indiceColumnaDeseada = getIndicesColumnas(columnasDeseadas);
        return obtenerValoresColumnasDeseadas(indiceColumnaDeseada);  
    }
    
    public String[] buscarElemento(String action,String... columnasKeysAndValues){
        
        //Obtenemos los nombres de las columnas y los valores para cada una
        String[] columnas = new String[columnasKeysAndValues.length];
        String[] valores = new String[columnasKeysAndValues.length];
        for(int i=0;i<columnasKeysAndValues.length;i++){
            columnas[i] = columnasKeysAndValues[i].split("=")[0];
            valores[i] = columnasKeysAndValues[i].split("=")[1];
        }
        Integer[] columnasIndexes = this.getIndicesColumnas(columnas);
        
        int pagina = 0;
        String[] elemento = null;
        
        $(paginatorFirstPage_locator).click();
        
        do{
            
            if(pagina!=0)
                $(paginatorNextPage_locator).click();
            
            Selenide.sleep(1000);
            
            List<SelenideElement> filas = $$(filasTabla_locator);
            filas = filas.subList(1,filas.size()); //Removemos el header de la talba}
                    
            for(int j=0;j<filas.size() && elemento== null;j++){
                SelenideElement f = filas.get(j);
                //Validamos que coincida cada columna con el valor esperado
                boolean elementMatches = true;
                for(int k=0;k<columnasIndexes.length;k++){
                    String actualValue = f.$$(By.tagName("td")).get(columnasIndexes[k]).getText();
                    String filterValue = valores[k];
                    elementMatches = elementMatches && filterValue.equals(actualValue);
                }
                
                if(elementMatches){
                    int totalAmountOfColumns = f.$$(By.tagName("td")).size();
                    elemento = new String[totalAmountOfColumns];//Ultima columna siempre de opciones
                    
                    //Guardo los datos del elemento
                    for(int i=0;i<totalAmountOfColumns;i++){
                        elemento[i] = f.$$(By.tagName("td")).get(i).getText();
                    }
                    
                    //Llevar a cabo la acción, de existir tal
                    if(action==null);
                    else f.$(By.cssSelector("td button[title="+action+"]")).click();
                
                }
            }
            
            pagina++;
            
        }while(elemento==null && !$(paginatorNextPage_locator).getAttribute("class").contains("ui-state-disabled"));
        
        return elemento;
        
    }
    
    public String obtenerMensajeListaVacia(){
        String msj = $(mensajeListaVacia_locator).getText();
        return msj;
    }
    
    public boolean obtenerOperationEnabled(String action,String... columnasKeysAndValues){
        
        //Obtenemos los nombres de las columnas y los valores para cada una
        String[] columnas = new String[columnasKeysAndValues.length];
        String[] valores = new String[columnasKeysAndValues.length];
        for(int i=0;i<columnasKeysAndValues.length;i++){
            columnas[i] = columnasKeysAndValues[i].split("=")[0];
            valores[i] = columnasKeysAndValues[i].split("=")[1];
        }
        Integer[] columnasIndexes = this.getIndicesColumnas(columnas);
        
        int pagina = 0;
        String[] elemento = null;
        
        $(paginatorFirstPage_locator).click();
        
        do{
            
            if(pagina!=0)
                $(paginatorNextPage_locator).click();
            
            Selenide.sleep(1000);
            
            List<SelenideElement> filas = $$(filasTabla_locator);
            filas = filas.subList(1,filas.size()); //Removemos el header de la talba}
                    
            for(int j=0;j<filas.size() && elemento== null;j++){
                SelenideElement f = filas.get(j);
                //Validamos que coincida cada columna con el valor esperado
                boolean elementMatches = true;
                for(int k=0;k<columnasIndexes.length;k++){
                    String actualValue = f.$$(By.tagName("td")).get(columnasIndexes[k]).getText();
                    String filterValue = valores[k];
                    elementMatches = elementMatches && filterValue.equals(actualValue);
                }
                
                if(elementMatches){
                    int totalAmountOfColumns = f.$$(By.tagName("td")).size();
                    elemento = new String[totalAmountOfColumns-1];//Ultima columna siempre de opciones
                    
                    //Guardo los datos del elemento
                    for(int i=0;i<totalAmountOfColumns-1;i++){
                        elemento[i] = f.$$(By.tagName("td")).get(i).getText();
                    }
                    
                    //Llevar a cabo la acción, de existir tal
                    if(action==null);
                    else{
                        SelenideElement actionBtn = f.$(By.cssSelector("td button[title="+action+"]"));
                        if(actionBtn.exists() && actionBtn.getAttribute("aria-disabled").equals("false"))
                            return true;
                        else
                            return false;
                    }
                }
            }
            
            pagina++;
            
        }while(elemento==null && !$(paginatorNextPage_locator).getAttribute("class").contains("ui-state-disabled"));
        
        return false;
        
    }
    
}
