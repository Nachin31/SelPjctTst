/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemTests.tests;

import com.codeborne.selenide.Selenide;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.agendaPageObjects.AgendaCargaRapidaPacientePage;
import pageObjects.agendaPageObjects.AgendaCargaRapidaTratamientoPage;
import pageObjects.agendaPageObjects.AgendaCreateSesionPage;
import pageObjects.agendaPageObjects.AgendaEditSesion;
import pageObjects.agendaPageObjects.AgendaPage;
import pageObjects.commonPageObjects.CommonElementsPage;
import pageObjects.commonPageObjects.LoginPage;
import pageObjects.pacientePageObjects.PacienteListPage;
/**
 *
 * @author Nacho Gómez
 */
public class AgendaTest extends AutomatedTest{
    
    private LoginPage loginPage;
    private CommonElementsPage commonElementsPage;
    private AgendaPage agendaPage;
    private AgendaCargaRapidaPacientePage cargaRapidaPacientePopup;
    private AgendaCargaRapidaTratamientoPage cargaRapidaTratamientoPopup;
    private AgendaCreateSesionPage createSesionPage;
    private AgendaEditSesion editSesionPage;
    private PacienteListPage pacienteListPage;
    
    @BeforeClass
    @Parameters({"username", "password"})
    public void beforeClass(String username, String password) throws Exception {

        loginPage = new LoginPage();
        commonElementsPage = new CommonElementsPage();
        agendaPage = new AgendaPage();
        cargaRapidaPacientePopup = new AgendaCargaRapidaPacientePage();
        cargaRapidaTratamientoPopup = new AgendaCargaRapidaTratamientoPage();
        createSesionPage = new AgendaCreateSesionPage();
        editSesionPage = new AgendaEditSesion();
        pacienteListPage = new PacienteListPage();

        loginPage.completeTextField("Username", username);
        loginPage.completeTextField("Password", password);
        loginPage.clickButton("IniciarSesion");

    }
    
    @AfterMethod
    public void closePopupsIfDisplayed(){
        Selenide.sleep(1000);
        cargaRapidaPacientePopup.cerrarPopup();
        cargaRapidaTratamientoPopup.cerrarPopup();
        createSesionPage.cerrarPopup();
        editSesionPage.cerrarPopup();
    }
    
    @Test
    @Parameters({"diaValidoCrearSesion"})
    public void test_validacionCamposObligatoriosCargaRapidaPaciente(String diaValidoCrearSesion){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        createSesionPage.clickButton("Carga rapida paciente");
        cargaRapidaPacientePopup.clickButton("Guardar");
        
        Assert.assertEquals(cargaRapidaPacientePopup.getToasterMessageCount(2), 2);
        Assert.assertTrue(cargaRapidaPacientePopup.toasterMessageDisplayed("Error","Ingrese el apellido del paciente"));
        Assert.assertTrue(cargaRapidaPacientePopup.toasterMessageDisplayed("Error","Ingrese el nombre del paciente"));
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("TextField", "Apellido", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("TextField", "Nombre", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("Label", "Apellido", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("Label", "Nombre", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    @Parameters({"diaValidoCrearSesion"})
    public void test_validacionTelefonosNumericosCargaRapidaPaciente(String diaValidoCrearSesion){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        createSesionPage.clickButton("Carga rapida paciente");
        cargaRapidaPacientePopup.completeTextField("Apellido","TestApe");
        cargaRapidaPacientePopup.completeTextField("Nombre","TestNom");
        cargaRapidaPacientePopup.completeTextField("Telefono","StringNotANumber");
        cargaRapidaPacientePopup.completeTextField("Celular","StringNotANumber");
        cargaRapidaPacientePopup.clickButton("Guardar");
        
        Assert.assertEquals(cargaRapidaPacientePopup.getToasterMessageCount(2), 2);
        Assert.assertTrue(cargaRapidaPacientePopup.toasterMessageDisplayed("Error","Ingrese un teléfono válido"));
        Assert.assertTrue(cargaRapidaPacientePopup.toasterMessageDisplayed("Error","Ingrese un celular válido"));
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("TextField", "Telefono", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("TextField", "Celular", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("Label", "Telefono", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(cargaRapidaPacientePopup.getCssAttribute("Label", "Celular", "color"), "rgba(240, 51, 105, 1)");
    }

    @Test
    @Parameters({"diaValidoCrearSesion","pacienteConTratamientos"})
    public void test_validacionCamposObligatoriosCargaRapidaTratamiento(String diaValidoCrearSesion, String pacienteConTratamientos){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        createSesionPage.completeTextField("Paciente",pacienteConTratamientos.split(",")[0]);
        createSesionPage.selectPaciente(pacienteConTratamientos);
        Selenide.sleep(1000);
        createSesionPage.clickButton("Carga rapida tratamiento");
        cargaRapidaTratamientoPopup.clickButton("Guardar");
        
        Assert.assertEquals(cargaRapidaTratamientoPopup.getToasterMessageCount(2), 2);
        Assert.assertTrue(cargaRapidaTratamientoPopup.toasterMessageDisplayed("Error","Seleccione un tipo de tratamiento"));
        Assert.assertTrue(cargaRapidaTratamientoPopup.toasterMessageDisplayed("Error","La cantidad de sesiones debe ser 1 o más"));
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("TextField", "Cantidad de sesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("Label", "Tipo de tratamiento", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("Label", "Cantidad de sesiones", "color"), "rgba(240, 51, 105, 1)");
    }

    @Test
    @Parameters({"diaValidoCrearSesion","pacienteConTratamientos","tratamientoCargarRapido"})
    public void test_validacionSesionSeaNumeroCargaRapidaTratamiento(String diaValidoCrearSesion, String pacienteConTratamientos, String tratamientoCargarRapido){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String[] tratamiento = tratamientoCargarRapido.split(";");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        createSesionPage.completeTextField("Paciente",pacienteConTratamientos.split(",")[0]);
        createSesionPage.selectPaciente(pacienteConTratamientos);
        Selenide.sleep(1000);
        createSesionPage.clickButton("Carga rapida tratamiento");
                
        cargaRapidaTratamientoPopup.clickButton("Tipo de tratamiento");
        cargaRapidaTratamientoPopup.selectTipoTratamiento(tratamiento[0]);
        cargaRapidaTratamientoPopup.completeTextField("Cantidad de sesiones","NotANumber");
        cargaRapidaTratamientoPopup.clickButton("Guardar");
        
        Assert.assertEquals(cargaRapidaTratamientoPopup.getToasterMessageCount(2), 1);
        Assert.assertTrue(cargaRapidaTratamientoPopup.toasterMessageDisplayed("Error","Cantidad de Sesiones:: 'NotANumber' debe ser un número formado por uno o varios dígitos."));
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("TextField", "Cantidad de sesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("Label", "Cantidad de sesiones", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    @Parameters({"diaValidoCrearSesion","pacienteConTratamientos","tratamientoCargarRapido"})
    public void test_validacionSesionSeaPositivaCargaRapidaTratamiento(String diaValidoCrearSesion, String pacienteConTratamientos, String tratamientoCargarRapido){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String[] tratamiento = tratamientoCargarRapido.split(";");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        createSesionPage.completeTextField("Paciente",pacienteConTratamientos.split(",")[0]);
        createSesionPage.selectPaciente(pacienteConTratamientos);
        Selenide.sleep(1000);
        createSesionPage.clickButton("Carga rapida tratamiento");

        cargaRapidaTratamientoPopup.clickButton("Tipo de tratamiento");        
        cargaRapidaTratamientoPopup.selectTipoTratamiento(tratamiento[0]);
        cargaRapidaTratamientoPopup.completeTextField("Cantidad de sesiones","-4");
        cargaRapidaTratamientoPopup.clickButton("Guardar");
        
        Assert.assertEquals(cargaRapidaTratamientoPopup.getToasterMessageCount(2), 1);
        Assert.assertTrue(cargaRapidaTratamientoPopup.toasterMessageDisplayed("Error","La cantidad de sesiones debe ser 1 o más"));
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("TextField", "Cantidad de sesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("Label", "Cantidad de sesiones", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    @Parameters({"diaValidoCrearSesion","pacienteConTratamientos","tratamientoCargarRapido"})
    public void test_validacionSesionSeaEnteraCargaRapidaTratamiento(String diaValidoCrearSesion, String pacienteConTratamientos, String tratamientoCargarRapido){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String[] tratamiento = tratamientoCargarRapido.split(";");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        createSesionPage.completeTextField("Paciente",pacienteConTratamientos.split(",")[0]);
        createSesionPage.selectPaciente(pacienteConTratamientos);
        Selenide.sleep(1000);
        createSesionPage.clickButton("Carga rapida tratamiento");

        cargaRapidaTratamientoPopup.clickButton("Tipo de tratamiento");        
        cargaRapidaTratamientoPopup.selectTipoTratamiento(tratamiento[0]);
        cargaRapidaTratamientoPopup.completeTextField("Cantidad de sesiones","2.4");
        cargaRapidaTratamientoPopup.clickButton("Guardar");
        
        Assert.assertEquals(cargaRapidaTratamientoPopup.getToasterMessageCount(2), 1);
        Assert.assertTrue(cargaRapidaTratamientoPopup.toasterMessageDisplayed("Error","Cantidad de Sesiones:: '2.4' debe ser un número formado por uno o varios dígitos."));
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("TextField", "Cantidad de sesiones", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(cargaRapidaTratamientoPopup.getCssAttribute("Label", "Cantidad de sesiones", "color"), "rgba(240, 51, 105, 1)");
    }
    
    @Test
    @Parameters({"diaValidoCrearSesion","pacienteACrearCamposObligatorios"})
    public void test_cancelarCargaRapidaPaciente(String diaValidoCrearSesion, String pacienteACrearCamposObligatorios){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        String [] paciente = pacienteACrearCamposObligatorios.split(";");
        Selenide.sleep(1000);
        createSesionPage.clickButton("Carga rapida paciente");
        
        cargaRapidaPacientePopup.completeTextField("Apellido",paciente[0]);
        cargaRapidaPacientePopup.completeTextField("Nombre",paciente[1]);
        cargaRapidaPacientePopup.clickButton("Cancelar");
        
        createSesionPage.clickButton("Cancelar");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        String[] pacienteEncontrado = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ paciente[0] + ", " + paciente[1]);

        //Checkeamos que el paciente no se haya creado
        Assert.assertNull(pacienteEncontrado);
    }
    
    @Test
    @Parameters({"diaValidoCrearSesion","pacienteACrearCamposObligatorios"})
    public void test_cargaRapidaPacienteCamposObligatoriosSuccessful(String diaValidoCrearSesion, String pacienteACrearCamposObligatorios){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        String [] paciente = pacienteACrearCamposObligatorios.split(";");
        Selenide.sleep(1000);
        createSesionPage.clickButton("Carga rapida paciente");
        
        cargaRapidaPacientePopup.completeTextField("Apellido",paciente[0]);
        cargaRapidaPacientePopup.completeTextField("Nombre",paciente[1]);
        cargaRapidaPacientePopup.clickButton("Guardar");

        Assert.assertEquals(createSesionPage.getToasterMessageCount(2), 1);        
        Assert.assertTrue(createSesionPage.toasterMessageDisplayed("Success","Paciente creado con éxito."));
        Assert.assertEquals(createSesionPage.getTextInTextField("Paciente"),paciente[0] + ", " + paciente[1]);
        
        createSesionPage.clickButton("Cancelar");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        String[] pacienteEncontrado = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ paciente[0] + ", " + paciente[1]);

        //Checkeamos que el paciente no se haya creado
        Assert.assertNotNull(pacienteEncontrado);
        
        Assert.assertEquals(pacienteEncontrado[0], paciente[0] + ", " + paciente[1]);
        Assert.assertEquals(pacienteEncontrado[1], "-");
        Assert.assertEquals(pacienteEncontrado[2], "-");
        Assert.assertEquals(pacienteEncontrado[3], "-");
        Assert.assertEquals(pacienteEncontrado[4], "-");
        Assert.assertEquals(pacienteEncontrado[5], "-");
        
    }
    
    @Test
    @Parameters({"diaValidoCrearSesion","pacienteACrearTodosCampos"})
    public void test_cargaRapidaPacienteTodosCamposSuccessful(String diaValidoCrearSesion, String pacienteACrearTodosCampos){
        
        commonElementsPage.clickMenuOption("Agenda");
        
        String fecha = getNextDayOfWeek(diaValidoCrearSesion,1)+" 15:00:00";
        agendaPage.completeTextField("Fecha",fecha);
        agendaPage.clickButton("Nueva Sesion");
        
        String [] paciente = pacienteACrearTodosCampos.split(";");
        Selenide.sleep(1000);
        createSesionPage.clickButton("Carga rapida paciente");
        
        cargaRapidaPacientePopup.completeTextField("Apellido",paciente[0]);
        cargaRapidaPacientePopup.completeTextField("Nombre",paciente[1]);
        cargaRapidaPacientePopup.completeTextField("Telefono",paciente[2]);
        cargaRapidaPacientePopup.completeTextField("Celular",paciente[3]);
        cargaRapidaPacientePopup.clickButton("Guardar");

        Assert.assertEquals(createSesionPage.getToasterMessageCount(2), 1);        
        Assert.assertTrue(createSesionPage.toasterMessageDisplayed("Success","Paciente creado con éxito."));
        Assert.assertEquals(createSesionPage.getTextInTextField("Paciente"),paciente[0] + ", " + paciente[1]);
        
        createSesionPage.clickButton("Cancelar");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");
        String[] pacienteEncontrado = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ paciente[0] + ", " + paciente[1]);

        //Checkeamos que el paciente no se haya creado
        Assert.assertNotNull(pacienteEncontrado);
        
        Assert.assertEquals(pacienteEncontrado[0], paciente[0] + ", " + paciente[1]);
        Assert.assertEquals(pacienteEncontrado[1], "-");
        Assert.assertEquals(pacienteEncontrado[2], paciente[2]);
        Assert.assertEquals(pacienteEncontrado[3], paciente[3]);
        Assert.assertEquals(pacienteEncontrado[4], "-");
        Assert.assertEquals(pacienteEncontrado[5], "-");
        
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
