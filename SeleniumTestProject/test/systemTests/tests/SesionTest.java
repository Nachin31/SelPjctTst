/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemTests.tests;

import com.codeborne.selenide.Selenide;
import java.util.Calendar;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.commonPageObjects.CommonElementsPage;
import pageObjects.commonPageObjects.LoginPage;
import pageObjects.pacientePageObjects.PacienteListPage;
import pageObjects.sesionPageObjects.SesionCreatePage;
import pageObjects.sesionPageObjects.SesionEditPage;
import pageObjects.sesionPageObjects.SesionRepeatPage;
import pageObjects.tratamientoPageObjects.TratamientoEditPage;
import pageObjects.tratamientoPageObjects.TratamientoListPage;

/**
 *
 * @author Nacho Gómez
 */
public class SesionTest extends AutomatedTest{
    
    private LoginPage loginPage;
    private CommonElementsPage commonElementsPage;
    private PacienteListPage pacienteListPage;
    private TratamientoListPage tratamientoListPage;
    private TratamientoEditPage tratamientoEditPage;
    private SesionCreatePage sesionCreatePage;
    private SesionEditPage sesionEditPage;
    private SesionRepeatPage sesionRepeatPage;
    
    @BeforeClass
    @Parameters({"username", "password"})
    public void beforeClass(String username, String password) throws Exception {

        loginPage = new LoginPage();
        commonElementsPage = new CommonElementsPage();
        pacienteListPage = new PacienteListPage();
        tratamientoListPage = new TratamientoListPage();
        tratamientoEditPage = new TratamientoEditPage();
        sesionCreatePage = new SesionCreatePage();
        sesionEditPage = new SesionEditPage();
        sesionRepeatPage = new SesionRepeatPage();

        loginPage.completeTextField("Username", username);
        loginPage.completeTextField("Password", password);
        loginPage.clickButton("IniciarSesion");

    }
    
    @AfterMethod
    public void closePopupsIfDisplayed(){
        sesionRepeatPage.cerrarPopup();
        sesionEditPage.cerrarPopup();
        sesionCreatePage.cerrarPopup();        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS"})
    public void test_mensajeTratamientoTablaSesionesVacia(String pacienteConTratamientos,String tratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        Assert.assertEquals(tratamientoEditPage.obtenerMensajeTablaVacia("Sesiones"),"(El tratamiento aún no posee sesiones)");

    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS"})
    public void test_validacionCamposVaciosCreacionSesion(String pacienteConTratamientos,String tratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        tratamientoEditPage.clickButton("Nueva Sesion");
        
        sesionCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Error","Ingrese la fecha y hora de la sesión"));
        Assert.assertEquals(sesionCreatePage.getCssAttribute("TextField", "Fecha", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(sesionCreatePage.getCssAttribute("Label", "Fecha", "color"), "rgba(240, 51, 105, 1)");

    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS"})
    public void test_validacionFormatoDeFechaCreacionSesion(String pacienteConTratamientos,String tratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        tratamientoEditPage.clickButton("Nueva Sesion");
        
        sesionCreatePage.completeTextField("Fecha","123456898");
        sesionCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Error","Fecha:: '123456898' no se ha podido reconocer como fecha y hora."));
        Assert.assertEquals(sesionCreatePage.getCssAttribute("TextField", "Fecha", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(sesionCreatePage.getCssAttribute("Label", "Fecha", "color"), "rgba(240, 51, 105, 1)");

    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS","sesionTratamientoUsuarioSinOS"})
    public void test_cancelarCreacionSesion(String pacienteConTratamientos,String tratamientoUsuarioSinOS,String sesionTratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",this.getNextDayOfWeek(sesionTratamientoUsuarioSinOS,1)+" 08:00");
        sesionCreatePage.clickButton("Cancelar");
        
        Assert.assertEquals(tratamientoEditPage.obtenerMensajeTablaVacia("Sesiones"),"(El tratamiento aún no posee sesiones)");
        
        tratamientoEditPage.clickButton("Nueva Sesion");
        Selenide.sleep(1000);
        Assert.assertEquals(sesionCreatePage.getTextInTextField("Fecha"),"","El campo Fecha no se limpio aunque se haya cancelado la carga.");
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS","sesionTratamientoUsuarioSinOS"})
    public void test_crearNuevaSesion(String pacienteConTratamientos,String tratamientoUsuarioSinOS,String sesionTratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek(sesionTratamientoUsuarioSinOS,1)+" 08:00:00";
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",sesionDate);
        sesionCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Success", "Sesión creada con éxito."),
                    "No se mostro el mensaje de éxito de creación de la sesión o se ha mostrado con errores.");
        Assert.assertNotNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesionDate),"No se agrego la nueva sesión a la tabla.");
        
    }
 
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS"})
    public void test_failCrearNuevaSesionFinDeSemana(String pacienteConTratamientos,String tratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek("Domingo",1)+" 08:00:00";
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",sesionDate);
        sesionCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Error", "No pueden crearse sesiones los fines de semana."),
                    "No se mostro el mensaje de error de creación de la sesión o se ha mostrado con errores.");
        Assert.assertNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesionDate),"Se agrego la nueva sesión a la tabla a pesar que no debería.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS","sesionTratamientoUsuarioSinOS"})
    public void test_failCrearDosSesionesMismoHorario(String pacienteConTratamientos,String tratamientoUsuarioSinOS,String sesionTratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek(sesionTratamientoUsuarioSinOS,1)+" 10:00:00";
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",sesionDate);
        sesionCreatePage.clickButton("Guardar");
        
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",sesionDate);
        sesionCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Error", "No pueden crearse sesiones que se solapen en horario."),
                    "No se mostro el mensaje de error de creación de la sesión o se ha mostrado con errores.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS","sesionTratamientoUsuarioSinOS"})
    public void test_failCrearDosSesionesSolapadasHorario(String pacienteConTratamientos,String tratamientoUsuarioSinOS,String sesionTratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek(sesionTratamientoUsuarioSinOS,1)+" 12:00:00";
        String sesionDate2 = getNextDayOfWeek(sesionTratamientoUsuarioSinOS,1)+" 12:15:00";
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",sesionDate);
        sesionCreatePage.clickButton("Guardar");
        
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",sesionDate2);
        sesionCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Error", "No pueden crearse sesiones que se solapen en horario."),
                    "No se mostro el mensaje de error de creación de la sesión o se ha mostrado con errores.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinOS","sesionPasadaTratamientoUsuarioSinOS"})
    public void test_crearNuevaSesionFechaPasada(String pacienteConTratamientos,String tratamientoUsuarioSinOS,String sesionPasadaTratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek(sesionPasadaTratamientoUsuarioSinOS,-1)+" 08:00:00";
        tratamientoEditPage.clickButton("Nueva Sesion");
        sesionCreatePage.completeTextField("Fecha",sesionDate);
        sesionCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Success", "Sesión creada con éxito."),
                    "No se mostro el mensaje de éxito de creación de la sesión o se ha mostrado con errores.");
        Assert.assertNotNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesionDate),"No se agrego la nueva sesión a la tabla.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionConDiagnostico"})
    public void test_datosEdicionSesionConDiagnostico(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionConDiagnostico){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion = sesionConDiagnostico.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        
        //Validamos los labels del popup
        Assert.assertEquals(sesionEditPage.getTextInLabel("Paciente"),pacienteConTratamientos);
        String tratamientoLabel = tratamiento[0]+ (tratamiento[3].equals("-")?"":"-"+tratamiento[3]);
        tratamientoLabel = (tratamientoLabel.length()>30)?(tratamientoLabel.substring(0, 27) + "..."):tratamientoLabel;
        Assert.assertEquals(sesionEditPage.getTextInLabel("Tratamiento"),tratamientoLabel);
        Assert.assertEquals(sesionEditPage.getTextInLabel("Numero de Sesion"),sesion[0]);
        Assert.assertEquals(sesionEditPage.getTextInLabel("Transcurrida"),sesion[2]);
        Assert.assertEquals(sesionEditPage.getTextInTextField("Fecha"),sesion[1]);
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioSinDiagnostico","sesionSinDiagnostico"})
    public void test_datosEdicionSesionSinDiagnostico(String pacienteConTratamientos,String tratamientoUsuarioSinDiagnostico,String sesionSinDiagnostico){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioSinDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion = sesionSinDiagnostico.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        
        //Validamos los labels del popup
        Assert.assertEquals(sesionEditPage.getTextInLabel("Paciente"),pacienteConTratamientos);
        String tratamientoLabel = tratamiento[0]+ (tratamiento[3].equals("-")?"":"-"+tratamiento[3]);
        tratamientoLabel = (tratamientoLabel.length()>30)?(tratamientoLabel.substring(0, 27) + "..."):tratamientoLabel;
        Assert.assertEquals(sesionEditPage.getTextInLabel("Tratamiento"),tratamientoLabel);
        Assert.assertEquals(sesionEditPage.getTextInLabel("Numero de Sesion"),sesion[0]);
        Assert.assertEquals(sesionEditPage.getTextInLabel("Transcurrida"),sesion[2]);
        Assert.assertEquals(sesionEditPage.getTextInTextField("Fecha"),sesion[1]);
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionConDiagnostico"})
    public void test_validacionCamposVaciosEdicionSesion(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionConDiagnostico){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion = sesionConDiagnostico.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.completeTextField("Fecha","");
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionEditPage.toasterMessageDisplayed("Error","Ingrese la fecha y hora de la sesión"));
        Assert.assertEquals(sesionEditPage.getCssAttribute("TextField", "Fecha", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(sesionEditPage.getCssAttribute("Label", "Fecha", "color"), "rgba(240, 51, 105, 1)");

    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionConDiagnostico"})
    public void test_validacionFormatoDeFechaEdicionSesion(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionConDiagnostico){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion = sesionConDiagnostico.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.completeTextField("Fecha","123456898");
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionEditPage.toasterMessageDisplayed("Error","Fecha:: '123456898' no se ha podido reconocer como fecha y hora."));
        Assert.assertEquals(sesionEditPage.getCssAttribute("TextField", "Fecha", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(sesionEditPage.getCssAttribute("TextField", "Fecha", "color"), "rgba(240, 51, 105, 1)");

    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionToEdit"})
    public void test_cancelarEdicionSesion(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionToEdit){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek("Viernes",1)+" 10:00:00";
        String[] sesion = sesionToEdit.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.completeTextField("Fecha",sesionDate);
        sesionEditPage.clickButton("Cancelar");
        
        Assert.assertNotNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesion[1]),"No se agrego la nueva sesión a la tabla.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionToEdit"})
    public void test_editarSesion(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionToEdit){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek("Viernes",1)+" 10:00:00";
        String[] sesion = sesionToEdit.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.completeTextField("Fecha",sesionDate);
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionEditPage.toasterMessageDisplayed("Success", "Sesión modificada con éxito."),
                    "No se mostro el mensaje de éxito de edición de la sesión o se ha mostrado con errores.");
        Assert.assertNotNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesionDate),"No se agrego la nueva sesión a la tabla.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionTranscurrida"})
    public void test_validarNoSePuedeEditarFechaSesionTranscurrida(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionTranscurrida){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);

        String[] sesion = sesionTranscurrida.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        
        Assert.assertEquals(sesionEditPage.getHtmlAttribute("TextField","Fecha","aria-disabled"),"true");

    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionToEditToSameDate1","sesionToEditToSameDate2"})
    public void test_failEditarDosSesionesMismoHorario(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionToEditToSameDate1,String sesionToEditToSameDate2){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion1 = sesionToEditToSameDate1.split(";");
        String[] sesion2 = sesionToEditToSameDate1.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Número de Sesión="+sesion2[0]);
        sesionEditPage.completeTextField("Fecha",sesion1[1]);
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionEditPage.toasterMessageDisplayed("Error","No pueden crearse sesiones que se solapen en horario."));
        Assert.assertNotNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesion2[1]),"Se modificó la fecha de la sesión (y no debía hacerlo).");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoUsuarioConDiagnostico","sesionToEditToSameDate1","sesionToEditToSameDate2"})
    public void test_failEditarDosSesionesSolapadasHorario(String pacienteConTratamientos,String tratamientoUsuarioConDiagnostico,String sesionToEditToSameDate1,String sesionToEditToSameDate2){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoUsuarioConDiagnostico.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion1 = sesionToEditToSameDate1.split(";");
        String[] sesion2 = sesionToEditToSameDate1.split(";");
        
        String sesionDate = getNextDayOfWeek("Miercoles",1)+" 11:00:00";
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Número de Sesión="+sesion1[0]);
        sesionEditPage.completeTextField("Fecha",sesionDate);
        sesionEditPage.clickButton("Guardar");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Número de Sesión="+sesion2[0]);
        sesionEditPage.completeTextField("Fecha",sesionDate);
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionEditPage.toasterMessageDisplayed("Error","No pueden crearse sesiones que se solapen en horario."));
        Assert.assertNotNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesion2[1]),"Se modificó la fecha de la sesión (y no debía hacerlo).");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoConSesionesToEdit","sesionToEditFinDeSemana"})
    public void test_failEditarSesionFinDeSemana(String pacienteConTratamientos,String tratamientoConSesionesToEdit,String sesionToEditFinDeSemana){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoConSesionesToEdit.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek("Domingo",1)+" 08:00:00";
        String[] sesion = sesionToEditFinDeSemana.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.completeTextField("Fecha",sesionDate);
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Error", "No pueden editarse sesiones para los fines de semana."),
                    "No se mostro el mensaje de error de edición de la sesión o se ha mostrado con errores.");
        Assert.assertNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesionDate),"Se editó la fecha de la sesión EN la tabla a pesar que no debería.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoConSesionesToEdit","sesionToEditFechaPasada","sesionToEditFechaPasadaDia"})
    public void test_editarNuevaSesionFechaPasada(String pacienteConTratamientos,String tratamientoConSesionesToEdit,String sesionToEditFechaPasada,String sesionToEditFechaPasadaDia){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoConSesionesToEdit.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String sesionDate = getNextDayOfWeek(sesionToEditFechaPasadaDia,-1)+" 08:00:00";
        String[] sesion = sesionToEditFechaPasada.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.completeTextField("Fecha",sesionDate);
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Success", "Sesión editada con éxito."),
                    "No se mostro el mensaje de éxito de edición de la sesión o se ha mostrado con errores.");
        Assert.assertNotNull(tratamientoEditPage.buscarElementoEnTabla("Sesiones",null,"Fecha="+sesionDate),"No se agrego la nueva sesión a la tabla.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoConSesionesToEdit","sesionToEditCuenta"})
    public void test_editarSesionTranscurridaNoCuenta(String pacienteConTratamientos,String tratamientoConSesionesToEdit,String sesionToEditCuenta){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoConSesionesToEdit.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion = sesionToEditCuenta.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.setCheckboxSelected(false);
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Success", "Sesión modificada con éxito."),
                    "No se mostro el mensaje de éxito de edición de la sesión o se ha mostrado con errores.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoIncompletoConSesionesQueNoCuentan"})
    public void test_editarTratamientoMenosCantidadSesionesQueCuentan(String pacienteConTratamientos,String tratamientoIncompletoConSesionesQueNoCuentan){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoIncompletoConSesionesQueNoCuentan.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        tratamientoEditPage.completeTextField("CantidadDeSesiones",""+(Integer.parseInt(tratamiento[1])-1));
        tratamientoEditPage.clickButton("Guardar");
                
        Assert.assertEquals(tratamientoEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(tratamientoEditPage.toasterMessageDisplayed("Success","Tratamiento modificado con éxito."));

    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoCompletoConSesionesQueNoCuentan"})
    public void test_failEditarTratamientoMenosCantidadSesionesQueCuentan(String pacienteConTratamientos,String tratamientoCompletoConSesionesQueNoCuentan){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoCompletoConSesionesQueNoCuentan.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        tratamientoEditPage.completeTextField("CantidadDeSesiones",""+(Integer.parseInt(tratamiento[1])-1));
        tratamientoEditPage.clickButton("Guardar");
                
        Assert.assertEquals(tratamientoEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(tratamientoEditPage.toasterMessageDisplayed("Error","Ingrese un valor válido para la cantidad de sesiones. Debe ser mayor al número de sesiones que cuentan y/o a las sesiones cubiertas por ordenes."));
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("TextField", "CantidadDeSesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("TextField", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("Label", "CantidadDeSesiones", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoCompletoConSesionesQueNoCuentan","sesionFailMarcarCuenta"})
    public void test_failMarcarSesionCuentanCuandoTopeYaAlcanzado(String pacienteConTratamientos,String tratamientoCompletoConSesionesQueNoCuentan,String sesionFailMarcarCuenta){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoCompletoConSesionesQueNoCuentan.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        String[] sesion = sesionFailMarcarCuenta.split(";");
        
        tratamientoEditPage.buscarElementoEnTabla("Sesiones","Modificar","Fecha="+sesion[1]);
        sesionEditPage.setCheckboxSelected(true);
        sesionEditPage.clickButton("Guardar");
        
        Assert.assertEquals(sesionCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(sesionCreatePage.toasterMessageDisplayed("Error", "Ya se ha alcanzado el tope de sesiones del tratamiento. Incremente la cantidad de sesiones del mismo e intente de nuevo."),
                    "No se mostro el mensaje de error de edición de la sesión o se ha mostrado con errores.");
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoFailFinalizar"})
    public void test_failMarcarTratamientoFinalizado(String pacienteConTratamientos,String tratamientoFailFinalizar){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoFailFinalizar.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        tratamientoEditPage.setFinalizadoCheckboxSelected(true);
        tratamientoEditPage.clickButton("Guardar");

        Assert.assertEquals(tratamientoEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(tratamientoEditPage.toasterMessageDisplayed("Error","No se puede finalizar el tratamiento ya que posee sesiones pendientes."));
        Assert.assertEquals(tratamientoEditPage.getCssAttribute("Label", "Finalizado", "color"), "rgba(240, 51, 105, 1)");        
        
    }
    
    @Test
    @Parameters({"pacienteConTratamientos","tratamientoAFinalizar"})
    public void test_marcarTratamientoFinalizado(String pacienteConTratamientos,String tratamientoAFinalizar){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+pacienteConTratamientos);
        
        String[] tratamiento = tratamientoAFinalizar.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas="+tratamiento[4]+"/"+tratamiento[1]);
        
        tratamientoEditPage.setFinalizadoCheckboxSelected(true);
        tratamientoEditPage.clickButton("Guardar");
                
        Assert.assertEquals(tratamientoEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(tratamientoEditPage.toasterMessageDisplayed("Success","Tratamiento modificado con éxito."));
        Assert.assertEquals(tratamientoEditPage.getHtmlAttribute("Button","Guardar","aria-disabled"),"true");
        Assert.assertFalse(tratamientoEditPage.getFinzalizadoCheckboxEnabled());
        Assert.assertEquals(tratamientoEditPage.getHtmlAttribute("Button","Nueva Orden Medica","aria-disabled"),"true");
        Assert.assertEquals(tratamientoEditPage.getHtmlAttribute("Button","Nueva Sesion","aria-disabled"),"true");
    }
    
    //Dia es el dia de la semana sin tildes, y el sentido es 1, -1 o 0
    private String getNextDayOfWeek(String dia,int sentido){
        String reponse = "";
        switch(dia){
            case "Lunes":
                reponse = getNextDayOfWeek(Calendar.MONDAY,sentido);
                break;
            case "Martes":
                reponse = getNextDayOfWeek(Calendar.TUESDAY,sentido);
                break;
            case "Miercoles":
                reponse = getNextDayOfWeek(Calendar.WEDNESDAY,sentido);
                break;
            case "Jueves":
                reponse = getNextDayOfWeek(Calendar.THURSDAY,sentido);
                break;
            case "Viernes":
                reponse = getNextDayOfWeek(Calendar.FRIDAY,sentido);
                break;
            case "Sabado":
                reponse = getNextDayOfWeek(Calendar.SATURDAY,sentido);
                break;
            case "Domingo":
                reponse = getNextDayOfWeek(Calendar.SUNDAY,sentido);
                break;
            default:
                reponse = getNextDayOfWeek(Calendar.MONDAY,sentido);
        }
        return reponse;
    }
    
    private String getNextDayOfWeek(int dayOfWeek,int sentido){
        Calendar now = Calendar.getInstance();
        if(sentido!=0){
            while(!(now.get(Calendar.DAY_OF_WEEK)==dayOfWeek)){
                now.add(Calendar.DAY_OF_YEAR,1*sentido);
            }
        }
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        String sDay = String.format("%02d",day);
        String sMonth = String.format("%02d",month);
        String sYear = "" + year;
        return sDay + "/" + sMonth + "/" + sYear;
    }
    
}
