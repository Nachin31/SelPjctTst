/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pageObjects.cajaPageObjects;

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
public class MovimientoListPage extends Page {
    
    public MovimientoListPage(){
        super();
        add("Button","Nuevo Ingreso",By.id("MovimientosCajaListForm:datalist:createMovimientoIngreso"));
        add("Button","Nuevo Egreso",By.id("MovimientosCajaListForm:datalist:createMovimientoEgreso"));
        add("Table","Movimientos",null,"MovimientosCajaListForm");
    }
    
    
    
}
