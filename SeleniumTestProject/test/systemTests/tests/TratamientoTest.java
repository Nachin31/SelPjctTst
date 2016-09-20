/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemTests.tests;

import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.open;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.CommonElementsPage;
import pageObjects.LoginPage;
import pageObjects.PacienteListPage;
import static com.codeborne.selenide.Selenide.open;
import pageObjects.TratamientoCreatePage;
import pageObjects.TratamientoListPage;

/**
 *
 * @author Nacho Gómez
 */
public class TratamientoTest {

    private LoginPage loginPage;
    private CommonElementsPage commonElementsPage;
    private PacienteListPage pacienteListPage;
    private TratamientoListPage tratamientoListPage;
    private TratamientoCreatePage tratamientoCreatePage;

    @BeforeSuite
    @Parameters({"username", "password"})
    public void beforeSuite(String username, String password) throws Exception {
        //We set driver parameters
        System.setProperty("webdriver.chrome.driver", "src\\drivers\\chromedriver.exe");
        System.setProperty("selenide.browser", "Chrome");

        //General parameters
        Configuration.timeout = 20000;
        Configuration.reportsFolder = "F:\\";

        //Open the url which we want in Chrome
        open("http://localhost:40053/deltagestion/faces/protected/paciente/List.xhtml");

        loginPage = new LoginPage();
        commonElementsPage = new CommonElementsPage();
        pacienteListPage = new PacienteListPage();
        tratamientoListPage = new TratamientoListPage();
        tratamientoCreatePage = new TratamientoCreatePage();

        loginPage.completeTextField("Username", username);
        loginPage.completeTextField("Password", password);
        loginPage.clickButton("IniciarSesion");

    }

    @Test
    @Parameters({"pacienteSinTratamientos"})
    public void test_listaTratamientoVacia(String pacienteSinTratamientos) {
        
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        Assert.assertEquals(commonElementsPage.getPageMenuItems(), "Home>Pacientes>Tratamientos de "+pacienteSinTratamientos);
        
        Assert.assertEquals(tratamientoListPage.obtenerMensajeListaVacia(),"(El paciente aún no tiene tratamientos)");
        
    }

    @Test
    @Parameters({"pacienteSinTratamientos"})
    public void test_validacionTiposTratamientosParticulares(String pacienteSinTratamientos) {
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        tratamientoListPage.clickButton("AgregarTratamiento");
        
        //Seleccionamos cada tipo de tratamiento y vemos si los radiobuttons se deshabilitaron o no
        tratamientoCreatePage.selectTipoDeTratamiento("Fisiokinesioterapia");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Kinesioterapia");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Fisioterapia");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Tratamiento Neurológico");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Drenaje Linfático");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Kinesioterapia respiratoria");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Módulo Discapacidad");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Estimulación Temprana");
        Assert.assertTrue(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Gimnasia terapéutica");
        Assert.assertFalse(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.selectTipoDeTratamiento("Estética Masajes");
        Assert.assertFalse(tratamientoCreatePage.getSelectOptionsEnabled());
                        
        tratamientoCreatePage.selectTipoDeTratamiento("Estética Electrodos");
        Assert.assertFalse(tratamientoCreatePage.getSelectOptionsEnabled());
        
        tratamientoCreatePage.cerrarPopup();
    }
    
    @Test
    @Parameters({"pacienteSinTratamientos"})
    public void test_validacionesCreacionTratamiento(String pacienteSinTratamientos) {
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        tratamientoListPage.clickButton("AgregarTratamiento");
        
        //----- Se indica error de todos los campos ---
        tratamientoCreatePage.clickButton("Guardar");
        
        //Checkeamos que pida los 2 campos
        Assert.assertEquals(tratamientoCreatePage.getToasterMessageCount(1), 2);

        //Checkeamos mensaje y color de los mensajes mostrados
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Seleccione un tipo de tratamiento"));
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "La cantidad de sesiones debe ser 1 o más"));

//        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "TipoDeTratamiento", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "TipoDeTratamiento", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");

        //----- Se indica solamente el error de tipo de tratamiento sin seleccionar ---
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","10");
        tratamientoCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(tratamientoCreatePage.getToasterMessageCount(1), 1);
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Seleccione un tipo de tratamiento"));

//        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "TipoDeTratamiento", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(84, 110, 122)");

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "TipoDeTratamiento", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(84, 110, 122, 1)");

        //----- Se indica solamente el error de cantidad de sesiones menor que 1 ---
        tratamientoCreatePage.selectTipoDeTratamiento("Fisiokinesioterapia");
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","0");
        tratamientoCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(tratamientoCreatePage.getToasterMessageCount(1), 1);
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "La cantidad de sesiones debe ser 1 o más"));

//        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "TipoDeTratamiento", "border-bottom"), "1px solid rgb(84, 110, 122)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "TipoDeTratamiento", "color"), "rgba(84, 110, 122, 1)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");

        //Se indica que la cantida de sesiones esta vacía
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","");
        tratamientoCreatePage.clickButton("Guardar");
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Ingrese la cantidad de sesiones"));
        
        //Se indica que la cantidad de sesiones debe ser un número entero
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","texto");
        tratamientoCreatePage.clickButton("Guardar");
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Cantidad de Sesiones:: 'texto' debe ser un número formado por uno o varios dígitos."));
        
        tratamientoCreatePage.cerrarPopup();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

    }

}
