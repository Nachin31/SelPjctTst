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
import java.text.SimpleDateFormat;
import java.util.Date;
import pageObjects.TratamientoCreatePage;
import pageObjects.TratamientoEditPage;
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
    private TratamientoEditPage tratamientoEditPage;

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
        tratamientoEditPage = new TratamientoEditPage();

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
        
        //Hago foco en otro campo
        tratamientoCreatePage.completeTextField("Observaciones","");
        
        //Checkeamos que pida los 2 campos
        Assert.assertEquals(tratamientoCreatePage.getToasterMessageCount(1), 2);

        //Checkeamos mensaje y color de los mensajes mostrados
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Seleccione un tipo de tratamiento"));
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "La cantidad de sesiones debe ser 1 o más"));

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "TipoDeTratamiento", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "TipoDeTratamiento", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");

        //----- Se indica solamente el error de tipo de tratamiento sin seleccionar ---
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","10");
        tratamientoCreatePage.clickButton("Guardar");
        
        //Hago foco en otro campo
        tratamientoCreatePage.completeTextField("Observaciones","");
        
        Assert.assertEquals(tratamientoCreatePage.getToasterMessageCount(1), 1);
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Seleccione un tipo de tratamiento"));

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "TipoDeTratamiento", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(176, 190, 197)");

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "TipoDeTratamiento", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(84, 110, 122, 1)");

        //----- Se indica solamente el error de cantidad de sesiones menor que 1 ---
        tratamientoCreatePage.selectTipoDeTratamiento("Fisiokinesioterapia");
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","0");
        tratamientoCreatePage.clickButton("Guardar");
        
        //Hago foco en otro campo
        tratamientoCreatePage.completeTextField("Observaciones","");
        
        Assert.assertEquals(tratamientoCreatePage.getToasterMessageCount(1), 1);
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "La cantidad de sesiones debe ser 1 o más"));

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "TipoDeTratamiento", "border-bottom"), "1px solid rgb(176, 190, 197)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "TipoDeTratamiento", "color"), "rgba(84, 110, 122, 1)");
        Assert.assertEquals(tratamientoCreatePage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");

        //Se indica que la cantida de sesiones esta vacía
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","");
        tratamientoCreatePage.clickButton("Guardar");
        
        //Hago foco en otro campo
        tratamientoCreatePage.completeTextField("Observaciones","");
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Ingrese la cantidad de sesiones"));
        
        //Se indica que la cantidad de sesiones debe ser un número entero
        tratamientoCreatePage.completeTextField("CantidadDeSesiones","texto");
        tratamientoCreatePage.clickButton("Guardar");
        
        Assert.assertTrue(tratamientoCreatePage.toasterMessageDisplayed("Error", "Cantidad de Sesiones:: 'texto' debe ser un número formado por uno o varios dígitos."));
        
        tratamientoCreatePage.cerrarPopup();
    }

    
    
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearParticular"})
    public void test_cancelarCreacionTratamiento(String pacienteSinTratamientos,String tratmientoACrearParticular) {
        
        String[] tratamientoACrear = tratmientoACrearParticular.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        tratamientoListPage.clickButton("AgregarTratamiento");
        
        //Completamos todos los campos
        tratamientoCreatePage.selectTipoDeTratamiento(tratamientoACrear[0]);
        tratamientoCreatePage.completeTextField("CantidadDeSesiones",tratamientoACrear[1]);
        if(tratamientoACrear[2].equals("Particular")){
            tratamientoCreatePage.setCheckboxSelected("Particular", true);
            tratamientoCreatePage.setCheckboxSelected("Obra Social", false);
        }
        else{
            tratamientoCreatePage.setCheckboxSelected("Particular", false);
            tratamientoCreatePage.setCheckboxSelected("Obra Social", true);
        }
        tratamientoCreatePage.completeTextField("Diagnostico",tratamientoACrear[3]);
        tratamientoCreatePage.completeTextField("Observaciones",tratamientoACrear[4]);
        
        //----- Se indica error de todos los campos ---
        tratamientoCreatePage.clickButton("Cancelar");
        
        //Validamos que la lista de tratamientos siga estando vacía
        Assert.assertEquals(tratamientoListPage.obtenerMensajeListaVacia(),"(El paciente aún no tiene tratamientos)");
        
        //Abrimos nuevamente el popup y validamos que los campos esten vacíos
        tratamientoListPage.clickButton("AgregarTratamiento");
        
        Assert.assertEquals(tratamientoCreatePage.getSelectedTratamiento(),"Seleccione uno...");
        Assert.assertEquals(tratamientoCreatePage.getTextInTextField("CantidadDeSesiones"),"0");
        Assert.assertEquals(tratamientoCreatePage.getTextInTextField("Diagnostico"),"");
        Assert.assertEquals(tratamientoCreatePage.getTextInTextField("Observaciones"),"");
        
        tratamientoCreatePage.cerrarPopup();
        
    }
    
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearParticular"})
    public void test_creacionTratamientoParticularExitosa(String pacienteSinTratamientos,String tratmientoACrearParticular) {
        
        String[] tratamientoACrear = tratmientoACrearParticular.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        tratamientoListPage.clickButton("AgregarTratamiento");
        
        //Completamos todos los campos
        tratamientoCreatePage.selectTipoDeTratamiento(tratamientoACrear[0]);
        tratamientoCreatePage.completeTextField("CantidadDeSesiones",tratamientoACrear[1]);
        if(tratamientoACrear[2].equals("Particular")){
            tratamientoCreatePage.setCheckboxSelected("Particular", true);
            tratamientoCreatePage.setCheckboxSelected("Obra Social", false);
        }
        else{
            tratamientoCreatePage.setCheckboxSelected("Particular", false);
            tratamientoCreatePage.setCheckboxSelected("Obra Social", true);
        }
        tratamientoCreatePage.completeTextField("Diagnostico",tratamientoACrear[3]);
        tratamientoCreatePage.completeTextField("Observaciones",tratamientoACrear[4]);
        
        //----- Se indica error de todos los campos ---
        tratamientoCreatePage.clickButton("Guardar");
        
        //Validamos el toaster confirmando la creación del tratamiento
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Success", "Tratamiento creado con éxito.", 2));
        
        //Buscamos el tratamiento en la lista y validamos lo que muestra
        String[] tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1]);
        
        Assert.assertNotNull(tratamientoTableData);
        Assert.assertEquals(tratamientoTableData[0],getDateString(new Date()));
        Assert.assertEquals(tratamientoTableData[1],tratamientoACrear[0]);
        Assert.assertEquals(tratamientoTableData[2],tratamientoACrear[3]);
        Assert.assertEquals(tratamientoTableData[3],"0/"+tratamientoACrear[1]);
        Assert.assertEquals(tratamientoTableData[4],tratamientoACrear[2].equals("Particular")?"Si":"No");
        Assert.assertEquals(tratamientoTableData[5],"En curso");
        Assert.assertEquals(tratamientoTableData[6],"-");
        
        //Seleccionamos el tratamiento y validamos los datos ingresados previamente
        tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1],"Editar");
        
        Assert.assertEquals(tratamientoEditPage.getTextInLabel("TipoTratamiento"),tratamientoACrear[0]);
        Assert.assertEquals(tratamientoEditPage.getTextInLabel("Particular"),tratamientoACrear[2].equals("Particular")?"Si":"No");
        Assert.assertEquals(tratamientoEditPage.getTextInTextField("Diagnostico"),tratamientoACrear[3]);
        Assert.assertEquals(tratamientoEditPage.getTextInTextField("Observaciones"),tratamientoACrear[4]);
        Assert.assertEquals(tratamientoEditPage.getTextInTextField("CantidadDeSesiones"),tratamientoACrear[1]);
                
    }
    
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearObraSocial"})
    public void test_creacionTratamientoObraSocialExitosa(String pacienteSinTratamientos,String tratmientoACrearObraSocial) {
        
        String[] tratamientoACrear = tratmientoACrearObraSocial.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        tratamientoListPage.clickButton("AgregarTratamiento");
        
        //Completamos todos los campos
        tratamientoCreatePage.selectTipoDeTratamiento(tratamientoACrear[0]);
        tratamientoCreatePage.completeTextField("CantidadDeSesiones",tratamientoACrear[1]);
        if(tratamientoACrear[2].equals("Particular")){
            tratamientoCreatePage.setCheckboxSelected("Particular", true);
            tratamientoCreatePage.setCheckboxSelected("Obra Social", false);
        }
        else{
            tratamientoCreatePage.setCheckboxSelected("Particular", false);
            tratamientoCreatePage.setCheckboxSelected("Obra Social", true);
        }
        tratamientoCreatePage.completeTextField("Diagnostico",tratamientoACrear[3]);
        tratamientoCreatePage.completeTextField("Observaciones",tratamientoACrear[4]);
        
        //----- Se indica error de todos los campos ---
        tratamientoCreatePage.clickButton("Guardar");
        
        //Validamos el toaster confirmando la creación del tratamiento
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Success", "Tratamiento creado con éxito.", 2));
        
        //Buscamos el tratamiento en la lista y validamos lo que muestra
        String[] tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1],"Editar");
        
        Assert.assertNotNull(tratamientoTableData);
        Assert.assertEquals(tratamientoTableData[0],getDateString(new Date()));
        Assert.assertEquals(tratamientoTableData[1],tratamientoACrear[0]);
        Assert.assertEquals(tratamientoTableData[2],tratamientoACrear[3]);
        Assert.assertEquals(tratamientoTableData[3],"0/"+tratamientoACrear[1]);
        Assert.assertEquals(tratamientoTableData[4],tratamientoACrear[2].equals("Particular")?"Si":"No");
        Assert.assertEquals(tratamientoTableData[5],"En curso");
        Assert.assertEquals(tratamientoTableData[6],"-");
        
        //Seleccionado el tratamiento, validamos los datos ingresados previamente
        Assert.assertEquals(tratamientoEditPage.getTextInLabel("TipoTratamiento"),tratamientoACrear[0]);
        Assert.assertEquals(tratamientoEditPage.getTextInLabel("Particular"),tratamientoACrear[2].equals("Particular")?"Si":"No");
        Assert.assertEquals(tratamientoEditPage.getTextInTextField("Diagnostico"),tratamientoACrear[3]);
        Assert.assertEquals(tratamientoEditPage.getTextInTextField("Observaciones"),tratamientoACrear[4]);
        Assert.assertEquals(tratamientoEditPage.getTextInTextField("CantidadDeSesiones"),tratamientoACrear[1]);
                
    }

    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearObraSocial","tratmientoACrearParticular"})
    public void test_validarListaTratamientosPaciente(String pacienteSinTratamientos,String tratmientoACrearObraSocial, String tratmientoACrearParticular) {
        
        String[] tratamientoParticular = tratmientoACrearParticular.split(";");
        String[] tratamientoObraSocial = tratmientoACrearObraSocial.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        String[] tratamientoPartTableData = tratamientoListPage.buscarTratamiento(tratamientoParticular[0],tratamientoParticular[1]);
        String[] tratamientoOSTableData = tratamientoListPage.buscarTratamiento(tratamientoObraSocial[0],tratamientoObraSocial[1]);
        
        //Validamos que ambos existan y sus datos sean correctos
        Assert.assertNotNull(tratamientoPartTableData);
        Assert.assertNotNull(tratamientoOSTableData);
        
        Assert.assertEquals(tratamientoPartTableData[0],getDateString(new Date()));
        Assert.assertEquals(tratamientoPartTableData[1],tratamientoParticular[0]);
        Assert.assertEquals(tratamientoPartTableData[2],tratamientoParticular[3]);
        Assert.assertEquals(tratamientoPartTableData[3],"0/"+tratamientoParticular[1]);
        Assert.assertEquals(tratamientoPartTableData[4],tratamientoParticular[2].equals("Particular")?"Si":"No");
        Assert.assertEquals(tratamientoPartTableData[5],"En curso");
        Assert.assertEquals(tratamientoPartTableData[6],"-");
        
        Assert.assertEquals(tratamientoOSTableData[0],getDateString(new Date()));
        Assert.assertEquals(tratamientoOSTableData[1],tratamientoObraSocial[0]);
        Assert.assertEquals(tratamientoOSTableData[2],tratamientoObraSocial[3]);
        Assert.assertEquals(tratamientoOSTableData[3],"0/"+tratamientoObraSocial[1]);
        Assert.assertEquals(tratamientoOSTableData[4],tratamientoObraSocial[2].equals("Particular")?"Si":"No");
        Assert.assertEquals(tratamientoOSTableData[5],"En curso");
        Assert.assertEquals(tratamientoOSTableData[6],"-");
        
    }
    
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearObraSocial"})
    public void test_validacionSesionesDuranteEdicionDeUnTratamiento(String pacienteSinTratamientos,String tratmientoACrearObraSocial) {
     
        String[] tratamientoObraSocial = tratmientoACrearObraSocial.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        tratamientoListPage.buscarTratamiento(tratamientoObraSocial[0],tratamientoObraSocial[1],"Editar");
        
        tratamientoEditPage.completeTextField("CantidadDeSesiones","0");
        tratamientoEditPage.clickButton("Guardar");
        
        //Checkeamos toaster notification y colores de error en los campos
        //Checkeamos que pida los 2 campos
        Assert.assertEquals(tratamientoEditPage.getToasterMessageCount(1), 1);

        //Checkeamos mensaje y color de los mensajes mostrados
        Assert.assertTrue(tratamientoEditPage.toasterMessageDisplayed("Error", "La cantidad de sesiones debe ser 1 o más"));
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");

        //Dejamos el campo vacío y checkeamos el mensaje de error
        tratamientoEditPage.completeTextField("CantidadDeSesiones","");
        tratamientoEditPage.clickButton("Guardar");
        
        Assert.assertTrue(tratamientoEditPage.toasterMessageDisplayed("Error", "Ingrese la cantidad de sesiones"));
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");

        //Completamos el campo con un string
        tratamientoEditPage.completeTextField("CantidadDeSesiones","texto");
        tratamientoEditPage.clickButton("Guardar");
        
        Assert.assertTrue(tratamientoEditPage.toasterMessageDisplayed("Error", "Cantidad de Sesiones:: 'texto' debe ser un número formado por uno o varios dígitos."));
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");

    }
    
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearObraSocial"})
    public void test_edicionExitosaDatosGeneralesTratamiento(String pacienteSinTratamientos,String tratmientoACrearObraSocial) {
    
        String[] tratamientoAEditar = tratmientoACrearObraSocial.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        tratamientoListPage.buscarTratamiento(tratamientoAEditar[0],tratamientoAEditar[1],"Editar");
        
        tratamientoEditPage.completeTextField("CantidadDeSesiones", "2");
        tratamientoEditPage.clickButton("Guardar");
        
        //Checkeamos el toaster popup con el mensaje de éxito
        Assert.assertTrue(tratamientoListPage.toasterMessageDisplayed("Success", "Tratamiento modificado con éxito.", 2));
        
        //Buscamos ese tratamiento nuevamente con el nuevo valor de sesiones, de encontrarlo checkeamos la data mostrada en la tabla        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        String[] tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoAEditar[0],"2");
        Assert.assertNotNull(tratamientoTableData);
        
        Assert.assertEquals(tratamientoTableData[0],getDateString(new Date()));
        Assert.assertEquals(tratamientoTableData[1],tratamientoAEditar[0]);
        Assert.assertEquals(tratamientoTableData[2],tratamientoAEditar[3]);
        Assert.assertEquals(tratamientoTableData[3],"0/2");
        Assert.assertEquals(tratamientoTableData[4],tratamientoAEditar[2].equals("Particular")?"Si":"No");
        Assert.assertEquals(tratamientoTableData[5],"En curso");
        Assert.assertEquals(tratamientoTableData[6],"-");
        
        //Restituimos la cantidad de sesiones
        tratamientoListPage.buscarTratamiento(tratamientoAEditar[0],"2","Editar");
        
        tratamientoEditPage.completeTextField("CantidadDeSesiones", tratamientoAEditar[1]);
        tratamientoEditPage.clickButton("Guardar");
        
    }
    
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearObraSocial"})
    public void test_cancelarEliminacionDeTratamiento(String pacienteSinTratamientos,String tratmientoACrearObraSocial) {
        
        String[] tratamientoACrear = tratmientoACrearObraSocial.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        //Buscamos el tratamiento en la lista y validamos lo que muestra
        String[] tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1],"Eliminar");
        
        tratamientoListPage.confirmTratamientoDelete(false);
        tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1]);
        
        //Validamos que el usuario aún existe
        Assert.assertNotNull(tratamientoTableData);
                
    }
     
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearObraSocial"})
    public void test_eliminacionExitosaTratamientoConVariosListados(String pacienteSinTratamientos,String tratmientoACrearObraSocial) {
        
        String[] tratamientoACrear = tratmientoACrearObraSocial.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        //Buscamos el tratamiento en la lista y validamos lo que muestra
        String[] tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1],"Eliminar");
        
        tratamientoListPage.confirmTratamientoDelete(true);
        
        //Validamos que el mensaje de éxito se muestre
        Assert.assertTrue(tratamientoListPage.toasterMessageDisplayed("Success", "Tratamiento eliminado con éxito.", 2));
        
        tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1]);
        
        //Validamos que el usuario aún existe
        Assert.assertNull(tratamientoTableData);
                
    }
    
    @Test
    @Parameters({"pacienteSinTratamientos","tratmientoACrearParticular"})
    public void test_eliminacionExitosaTratamientoUnicoListado(String pacienteSinTratamientos,String tratmientoACrearParticular) {
        
        String[] tratamientoACrear = tratmientoACrearParticular.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        pacienteListPage.buscarPaciente(pacienteSinTratamientos,"Tratamientos");
        
        //Buscamos el tratamiento en la lista y validamos lo que muestra
        String[] tratamientoTableData = tratamientoListPage.buscarTratamiento(tratamientoACrear[0],tratamientoACrear[1],"Eliminar");
        
        tratamientoListPage.confirmTratamientoDelete(true);
        
        //Validamos que el mensaje de éxito se muestre
        Assert.assertTrue(tratamientoListPage.toasterMessageDisplayed("Success", "Tratamiento eliminado con éxito.", 2));
        
        //Validamos que la lista este ahora vacía
        Assert.assertEquals(tratamientoListPage.obtenerMensajeListaVacia(),"(El paciente aún no tiene tratamientos)");
                
    }
    
    private String getDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
        return sdf.format(date);
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

    }

}
