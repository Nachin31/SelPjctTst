/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.cajaPageObjects;

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
public class MovimientoCreatePage extends Page{
    
    private By conceptosList_locator = By.cssSelector("#MovimientoCajaCreateForm\\:concepto_panel ul > li");
    
    public MovimientoCreatePage(){
        super();
        add("Label","Tipo Movimiento",By.id("dialogHeader"));
        add("Button","Concepto",By.cssSelector("#MovimientoCajaCreateForm\\:concepto span"));
        add("TextField","Precio por Unidad",By.id("MovimientoCajaCreateForm:valorunitario_input"));
        add("TextField","Cantidad",By.id("MovimientoCajaCreateForm:cantidad_input"));
        add("TextField","Descripcion",By.id("MovimientoCajaCreateForm:descripcion"));
        add("Label","Concepto",By.id("MovimientoCajaCreateForm:conceptoLabel"));
        add("Label","Precio por Unidad",By.id("MovimientoCajaCreateForm:precioPorUnidadLabel"));
        add("Label","Monto",By.id("MovimientoCajaCreateForm:monto"));
        add("Label","Descripcion",By.id("MovimientoCajaCreateForm:descripcionLabel"));
        add("Button","Guardar",By.id("MovimientoCajaCreateForm:btnGuardar"));
        add("Button","Cancelar",By.id("MovimientoCajaCreateForm:btnCancear"));
        setCloseArrowLocator(By.cssSelector("#MovimientoCajaCreateDlg a"));
    }
 
    public void selectConcepto(String name){
        
        Selenide.sleep(2000);
            
        List<SelenideElement> osListed = $$(conceptosList_locator);

        for(SelenideElement os : osListed){
            if(os.getText().equals(name)){
                os.click();
                break;
            }
        }
        
    }
    
}
