/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemTests.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.cajaPageObjects.MovimientoCreatePage;
import pageObjects.cajaPageObjects.MovimientoListPage;
import pageObjects.commonPageObjects.CommonElementsPage;
import pageObjects.commonPageObjects.LoginPage;

/**
 *
 * @author Nacho Gómez
 */
public class CajaTest extends AutomatedTest{
    
    private LoginPage loginPage;
    private CommonElementsPage commonElementsPage;
    private MovimientoListPage movimientosListPage;
    private MovimientoCreatePage movimientoCreatePage;
    private double total = 0;
    
    @BeforeClass
    @Parameters({"username", "password"})
    public void beforeClass(String username, String password) throws Exception {

        loginPage = new LoginPage();
        commonElementsPage = new CommonElementsPage();
        movimientosListPage = new MovimientoListPage();
        movimientoCreatePage = new MovimientoCreatePage();

        loginPage.completeTextField("Username", username);
        loginPage.completeTextField("Password", password);
        loginPage.clickButton("IniciarSesion");

    }
    
    @AfterMethod
    public void closePopupsIfDisplayed(){
        movimientoCreatePage.cerrarPopup();
    }
    
    @Test
    public void test_mensajeTablaMovimientosVacia(){
        
        commonElementsPage.clickMenuOption("Caja");
        Assert.assertEquals(movimientosListPage.obtenerMensajeTablaVacia("Movimientos"),"No hay movimientos de caja");

    }
    
    @Test
    @Parameters({"ingresoACrear1"})
    public void test_cancelarCreacionIngreso(String ingresoACrear){
        
        String[] ingreso = ingresoACrear.split(";");
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto(ingreso[0]);
        movimientoCreatePage.completeTextField("Precio por Unidad",ingreso[1]);
        movimientoCreatePage.completeTextField("Cantidad",ingreso[2]);
        movimientoCreatePage.completeTextField("Descripcion",ingreso[3]);
        movimientoCreatePage.clickButton("Cancelar");

        Assert.assertEquals(movimientosListPage.obtenerMensajeTablaVacia("Movimientos"),"No hay movimientos de caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        Assert.assertEquals(movimientoCreatePage.getTextInLabel("Tipo Movimiento"),"Nuevo movimiento de caja: INGRESO");
        Assert.assertEquals(movimientoCreatePage.getTextInTextField("Precio por Unidad"),"0.00");
        Assert.assertEquals(movimientoCreatePage.getTextInTextField("Cantidad"),"1");
        Assert.assertEquals(movimientoCreatePage.getTextInLabel("Monto"),"0");
    }
    
    @Test
    @Parameters({"egresoACrear1"})
    public void test_cancelarCreacionEgreso(String egresoACrear1){
        
        String[] egreso = egresoACrear1.split(";");
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto(egreso[0]);
        movimientoCreatePage.completeTextField("Precio por Unidad",egreso[1]);
        movimientoCreatePage.completeTextField("Cantidad",egreso[2]);
        movimientoCreatePage.completeTextField("Descripcion",egreso[3]);
        movimientoCreatePage.clickButton("Cancelar");

        Assert.assertEquals(movimientosListPage.obtenerMensajeTablaVacia("Movimientos"),"No hay movimientos de caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        Assert.assertEquals(movimientoCreatePage.getTextInLabel("Tipo Movimiento"),"Nuevo movimiento de caja: EGRESO");
        Assert.assertEquals(movimientoCreatePage.getTextInTextField("Precio por Unidad"),"0.00");
        Assert.assertEquals(movimientoCreatePage.getTextInTextField("Cantidad"),"1");
        Assert.assertEquals(movimientoCreatePage.getTextInLabel("Monto"),"0");
    }
    
    @Test
    @Parameters({"ingresoACrear1"})
    public void test_crearIngresoConTodosLosDatos(String ingresoACrear1){
        
        String[] ingreso = ingresoACrear1.split(";");
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto(ingreso[0]);
        movimientoCreatePage.completeTextField("Precio por Unidad",ingreso[1]);
        movimientoCreatePage.completeTextField("Cantidad",ingreso[2]);
        movimientoCreatePage.completeTextField("Descripcion",ingreso[3]);
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(2), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Success","Movimiento creado con éxito"));
        double monto = new Integer(ingreso[2]) * new Double(ingreso[1]);
        total+=monto;
        String[] movimiento = movimientosListPage.buscarElementoEnTabla("Movimientos",null,"Concepto="+ingreso[0]);
        Assert.assertNotNull(movimiento,"No se encontro el movimiento creado en la tabla");
        Assert.assertEquals(movimiento[2],ingreso[3]);
        Assert.assertEquals(new Double(movimiento[3].replace(",",".")),monto);
        Assert.assertEquals(new Double(movimiento[5].replace(",",".")),total);
        
    }
    
    @Test
    @Parameters({"ingresoACrear2"})
    public void test_crearIngresoSinDescripcion(String ingresoACrear2){
        
        String[] ingreso = ingresoACrear2.split(";");
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto(ingreso[0]);
        movimientoCreatePage.completeTextField("Precio por Unidad",ingreso[1]);
        movimientoCreatePage.completeTextField("Cantidad",ingreso[2]);
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(2), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Success","Movimiento creado con éxito"));
        double monto = new Integer(ingreso[2]) * new Double(ingreso[1]);
        total+=monto;
        String[] movimiento = movimientosListPage.buscarElementoEnTabla("Movimientos",null,"Concepto="+ingreso[0]);
        Assert.assertNotNull(movimiento,"No se encontro el movimiento creado en la tabla");
        Assert.assertEquals(movimiento[2],"");
        Assert.assertEquals(new Double(movimiento[3].replace(",",".")),monto);
        Assert.assertEquals(new Double(movimiento[5].replace(",",".")),total);
        
    }
    
    @Test
    @Parameters({"egresoACrear1"})
    public void test_crearEgresoConTodosLosDatos(String egresoACrear1){
        
        String[] egreso = egresoACrear1.split(";");
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto(egreso[0]);
        movimientoCreatePage.completeTextField("Precio por Unidad",egreso[1]);
        movimientoCreatePage.completeTextField("Cantidad",egreso[2]);
        movimientoCreatePage.completeTextField("Descripcion",egreso[3]);
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(2), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Success","Movimiento creado con éxito"));
        double monto = new Integer(egreso[2]) * new Double(egreso[1]);
        total-=monto;
        String[] movimiento = movimientosListPage.buscarElementoEnTabla("Movimientos",null,"Concepto="+egreso[0]);
        Assert.assertNotNull(movimiento,"No se encontro el movimiento creado en la tabla");
        Assert.assertEquals(movimiento[2],egreso[3]);
        Assert.assertEquals(new Double(movimiento[4].replace(",",".")),monto);
        Assert.assertEquals(new Double(movimiento[5].replace(",",".")),total);
        
    }
    
    @Test
    @Parameters({"egresoACrear2"})
    public void test_crearEgresoSinDescripcion(String egresoACrear2){
        
        String[] egreso = egresoACrear2.split(";");
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto(egreso[0]);
        movimientoCreatePage.completeTextField("Precio por Unidad",egreso[1]);
        movimientoCreatePage.completeTextField("Cantidad",egreso[2]);
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(2), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Success","Movimiento creado con éxito"));
        double monto = new Integer(egreso[2]) * new Double(egreso[1]);
        total-=monto;
        String[] movimiento = movimientosListPage.buscarElementoEnTabla("Movimientos",null,"Concepto="+egreso[0]);
        Assert.assertNotNull(movimiento,"No se encontro el movimiento creado en la tabla");
        Assert.assertEquals(movimiento[2],"");
        Assert.assertEquals(new Double(movimiento[4].replace(",",".")),monto);
        Assert.assertEquals(new Double(movimiento[5].replace(",",".")),total);
        
    }
    
    @Test
    public void test_validacionCamposObligatoriosCreacionIngreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        movimientoCreatePage.completeTextField("Precio por Unidad","");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 2);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","Seleccione un concepto"));
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","El precio por unidad no puede ser nulo"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Precio por Unidad", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Concepto", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Precio por Unidad", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    public void test_validacionPrecioPorUnidadPositivoCreacionIngreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto("Estampilla");
        movimientoCreatePage.completeTextField("Precio por Unidad","-2");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","El precio por unidad debe ser un número positivo"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Precio por Unidad", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Precio por Unidad", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    public void test_validacionCantidadPositivaCreacionIngreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto("Estampilla");
        movimientoCreatePage.completeTextField("Cantidad","-2");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","La cantidad debe ser un número positivo"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Cantidad", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Cantidad", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    public void test_validacionLargoDescripcionCreacionIngreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Ingreso");
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto("Estampilla");
        movimientoCreatePage.completeTextField("Precio por Unidad","12.5");
        movimientoCreatePage.completeTextField("Cantidad","1");
        movimientoCreatePage.completeTextField("Descripcion","Una descripcion con más de 29 caracteres.");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","el tamaño tiene que estar entre 0 y 30"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Descripcion", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Descripcion", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    public void test_validacionCamposObligatoriosCreacionEgreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        movimientoCreatePage.completeTextField("Precio por Unidad","");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 2);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","Seleccione un concepto"));
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","El precio por unidad no puede ser nulo"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Precio por Unidad", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Concepto", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Precio por Unidad", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    public void test_validacionPrecioPorUnidadPositivoCreacionEgreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto("Estampilla");
        movimientoCreatePage.completeTextField("Precio por Unidad","-2");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","El precio por unidad debe ser un número positivo"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Precio por Unidad", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Precio por Unidad", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    public void test_validacionCantidadPositivaCreacionEgreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto("Estampilla");
        movimientoCreatePage.completeTextField("Cantidad","-2");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","La cantidad debe ser un número positivo"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Cantidad", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Cantidad", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    public void test_validacionLargoDescripcionCreacionEgreso(){
        
        commonElementsPage.clickMenuOption("Caja");
        movimientosListPage.clickButton("Nuevo Egreso");
        movimientoCreatePage.clickButton("Concepto");
        movimientoCreatePage.selectConcepto("Estampilla");
        movimientoCreatePage.completeTextField("Precio por Unidad","12.5");
        movimientoCreatePage.completeTextField("Cantidad","1");
        movimientoCreatePage.completeTextField("Descripcion","Una descripcion con más de 29 caracteres.");
        movimientoCreatePage.clickButton("Guardar");

        Assert.assertEquals(movimientoCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(movimientoCreatePage.toasterMessageDisplayed("Error","el tamaño tiene que estar entre 0 y 30"));
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("TextField", "Descripcion", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(movimientoCreatePage.getCssAttribute("Label", "Descripcion", "color"), "rgba(240, 51, 105, 1)");
    }
    
}
