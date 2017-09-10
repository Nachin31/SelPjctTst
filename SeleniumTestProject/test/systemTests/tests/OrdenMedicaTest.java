package systemTests.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.codeborne.selenide.Selenide;
import java.util.Calendar;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import pageObjects.commonPageObjects.CommonElementsPage;
import pageObjects.commonPageObjects.LoginPage;
import pageObjects.ordenMedicaPageObjects.OrdenMedicaAutorizarPage;
import pageObjects.ordenMedicaPageObjects.OrdenMedicaCreatePage;
import pageObjects.pacientePageObjects.PacienteListPage;
import pageObjects.tratamientoPageObjects.TratamientoEditPage;
import pageObjects.tratamientoPageObjects.TratamientoListPage;

/**
 *
 * @author Nacho Gómez
 */
public class OrdenMedicaTest extends AutomatedTest {
    
    private LoginPage loginPage;
    private CommonElementsPage commonElementsPage;
    private PacienteListPage pacienteListPage;
    private TratamientoListPage tratamientoListPage;
    private TratamientoEditPage tratamientoEditPage;
    private OrdenMedicaCreatePage ordenCreatePage;
    private OrdenMedicaAutorizarPage ordenAutorizarPage;
    private int cantSesionesCubiertasPorOrdenAgregadas = 0;
    
    @BeforeClass
    @Parameters({"username", "password"})
    public void beforeClass(String username, String password) throws Exception {

        loginPage = new LoginPage();
        commonElementsPage = new CommonElementsPage();
        pacienteListPage = new PacienteListPage();
        tratamientoListPage = new TratamientoListPage();
        tratamientoEditPage = new TratamientoEditPage();
        ordenCreatePage = new OrdenMedicaCreatePage();
        ordenAutorizarPage = new OrdenMedicaAutorizarPage();

        loginPage.completeTextField("Username", username);
        loginPage.completeTextField("Password", password);
        loginPage.clickButton("IniciarSesion");

    }
    
    @AfterMethod
    public void closePopupsIfDisplayed(){
        ordenCreatePage.cerrarPopup();
        ordenAutorizarPage.cerrarPopup();
    }
    
    @Test
    @Parameters({"usuarioSinOS","tratamientoUsuarioSinOS"})
    public void test_mensajeNoOsDentroTratamientoPacienteSinOS(String usuarioSinOS,String tratamientoUsuarioSinOS){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioSinOS);
        
        String[] tratamiento = tratamientoUsuarioSinOS.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        Assert.assertTrue(tratamientoEditPage.checkMensajeNoObraSocial("Para añadir ordenes médicas primero debes configurar la Obra Social del paciente."),"El mensaje de advetencia de que el usuario no posee Obra Social no se mmuestra o se muestra incorrectamente");
        
    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamParticUsuarioConOSyNroAfld"})
    public void test_noHayTablaOrdenMedicaEnTratamientoParticular(String usuarioConOSyNroAfiliado,String tratamParticUsuarioConOSyNroAfld){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamParticUsuarioConOSyNroAfld.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        Assert.assertFalse(tratamientoEditPage.isOrdenMedicaListDisplayed(),"Se encontro la tabla de ordenes médicas en un tratamiento que no debía poseerla.");

    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldSinOrd"})
    public void test_mensajeTratamientoTablaOrdenesMedicasVacia(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldSinOrd){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldSinOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        Assert.assertEquals(tratamientoEditPage.obtenerMensajeTablaVacia("Ordenes"),"(El tratamiento aún no posee ordenes médicas)");

    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldSinOrd"})
    public void test_validacionesDeFormatoCreacionDeOrden(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldSinOrd){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldSinOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        tratamientoEditPage.clickButton("Nueva Orden Medica");
        //Escribimos y guardamos para validar cada campo
        
        //El sistema debe valdiar que el numero de ordenes sea no-negativo
        ordenCreatePage.completeTextField("Cantidad de Sesiones","-8");
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Error", "Ingrese un valor mayor que cero"));
        
        //El sistema debe validar que el número no posea comas
        ordenCreatePage.completeTextField("Cantidad de Sesiones","2.15");
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Error", "Cantidad de sesiones: '2.15' debe ser un número formado por uno o varios dígitos."));
        
        //El sistem debe validar que el número de sesiones sea numércio
        ordenCreatePage.completeTextField("Cantidad de Sesiones","abch!ou?");
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Error", "Cantidad de sesiones: 'abch!ou?' debe ser un número formado por uno o varios dígitos."));
        
        //El sistema debe validar que el numero sea no nulo
        ordenCreatePage.completeTextField("Cantidad de Sesiones","");
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Error", "Ingrese un valor mayor que cero"));
        
    }
    
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldSinOrd"})
    public void test_validacionOrdenUnicaSobrepasaCantSesionesDelTratamiento(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldSinOrd){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldSinOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        tratamientoEditPage.clickButton("Nueva Orden Medica");
        
        //El sistema debe validar que el numero sea no nulo
        ordenCreatePage.completeTextField("Cantidad de Sesiones",""+(1 + new Integer(tratamiento[1])));
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Error", "El número de sesiones cubiertas por la orden debe ser menor o igual a la cantidad de sesiones del tratamiento."));
        
    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd"})
    public void test_validacionMultiplesOrdenesSobrepasaCantSesionesFaltanTratamiento(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        tratamientoEditPage.clickButton("Nueva Orden Medica");
        
        //El sistema debe validar que el numero sea no nulo
        ordenCreatePage.completeTextField("Cantidad de Sesiones",""+(1 + new Integer(tratamiento[1])));
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Error", "El número de sesiones cubiertas por la orden debe ser menor o igual a la cantidad de sesiones del tratamiento."));
        
    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd"})
    public void test_creacionOrdenMenosSesionesDeFaltantesDelTratamiento(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        tratamientoEditPage.clickButton("Nueva Orden Medica");
        
        //El sistema debe validar que el numero sea no nulo
        ordenCreatePage.completeTextField("Cantidad de Sesiones","1");
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Success", "Orden creada con éxito."),
                    "No se mostro el mensaje de éxito de creación de la orden medica o se ha mostrado con errores.");
        
        cantSesionesCubiertasPorOrdenAgregadas += 1;
        
    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd","ordenTratamConOrdConOs","orden2TratamConOrdConOs"})
    public void test_creacionOrdenSesionesIgualAFaltantesDelTratamiento(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd,String ordenTratamConOrdConOs,String orden2TratamConOrdConOs){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        tratamientoEditPage.clickButton("Nueva Orden Medica");
        
        String[] ordenMedica = ordenTratamConOrdConOs.split(";");
        String[] ordenMedica2 = orden2TratamConOrdConOs.split(";");
                
        //El sistema debe validar que el numero sea no nulo
        ordenCreatePage.completeTextField("Cantidad de Sesiones",
                ""+(new Integer(tratamiento[1]) - new Integer(ordenMedica[0]) - new Integer(ordenMedica2[0]) - cantSesionesCubiertasPorOrdenAgregadas));
        ordenCreatePage.clickButton("Guardar");
        
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Success", "Orden creada con éxito."),
                    "No se mostro el mensaje de éxito de creación de la orden medica o se ha mostrado con errores.");
    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd","ordenTratamConOrdConOs"})
    public void test_infoOrdenMedicaPacienteConTodaLaInfo(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd,String ordenTratamConOrdConOs){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOs.split(";");        
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        
        //Validamos la información que muestra el popup
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Paciente"),usuarioConOSyNroAfiliado);
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Tratamiento"),tratamiento[0]);
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Obra social"),ordenMedica[1]);
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Numero de afiliado"),ordenMedica[2]);
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Cantidad de sesiones"),ordenMedica[0]);
        
    }
    
    @Test
    @Parameters({"usuarioConOSSinNroAfiliado","tratamPorOsUsuarioConOSSinNroAfldConOrd","ordenTratamConOrdConOsSinNroAfiliafo"})
    public void test_infoOrdenMedicaPacienteSinNroAfiliado(String usuarioConOSSinNroAfiliado,String tratamPorOsUsuarioConOSSinNroAfldConOrd,String ordenTratamConOrdConOsSinNroAfiliafo){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSSinNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSSinNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOsSinNroAfiliafo.split(";");        
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        
        //Validamos la información que muestra el popup
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Paciente"),usuarioConOSSinNroAfiliado);
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Tratamiento"),tratamiento[0]);
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Obra social"),ordenMedica[1]);
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Numero de afiliado"),"No cargado");
        Assert.assertEquals(ordenAutorizarPage.getTextInLabel("Cantidad de sesiones"),ordenMedica[0]);
        
    }
 
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd","ordenTratamConOrdConOs"})
    public void test_validacionAutorizarOrdenTratamiento(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd,String ordenTratamConOrdConOs){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOs.split(";");        
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        
        //Validamos la información que muestra el popup
        ordenAutorizarPage.completeTextField("Codigo de autorizacion","");
        ordenAutorizarPage.clickButton("Guardar");
        
        Assert.assertEquals(ordenAutorizarPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenAutorizarPage.toasterMessageDisplayed("Error", "Ingrese el código de autorización"),
                    "No se mostro el mensaje de error al intentar usar un código de autorización vacío.");
        
    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd","ordenTratamConOrdConOs"})
    public void test_cancelarAutorizarOrdenTratamiento(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd,String ordenTratamConOrdConOs){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOs.split(";");        
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        
        //Validamos la información que muestra el popup
        ordenAutorizarPage.completeTextField("Codigo de autorizacion","A124E2C3");
        ordenAutorizarPage.clickButton("Cancelar");
        
        List<String[]> autorizaciones = tratamientoEditPage.obtenerListaDeValoresTabla("Ordenes","Fecha de Autorización","Cód. de Autorización");
        //Validamos que no se haya guardado la autorización
        for(String[] ordAuth : autorizaciones){
            Assert.assertEquals(ordAuth[0],"","Se guardó la autorización de la orden aunque se haya cancelado.");
            Assert.assertEquals(ordAuth[1],"","Se guardó la autorización de la orden aunque se haya cancelado.");
        }
        //Validamos que la orden aun sea eliminable
        Assert.assertTrue(tratamientoEditPage.validarAccionHabilitada("Ordenes","Eliminar","Cantidad de Sesiones="+ordenMedica[0]),
                "Se deshabilitó la opción de eliminar aun cuando se cancelo la autorización de la orden.");
        
        //Validamos que el popup de autorizacion se haya limpiado
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        Selenide.sleep(1000);
        Assert.assertEquals(ordenAutorizarPage.getTextInTextField("Codigo de autorizacion"),"","No se limpio el campo de cod. de autorización al cancelar el popup.");

    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd","ordenTratamConOrdConOs"})
    public void test_autorizarOrdenTratamientoExitosamente(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd,String ordenTratamConOrdConOs){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOs.split(";");        
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        
        //Validamos la información que muestra el popup
        ordenAutorizarPage.completeTextField("Codigo de autorizacion","A124E2C3");
        ordenAutorizarPage.clickButton("Guardar");
        
        //Validamos mensaje de éxito
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Success", "Orden modificada con éxito."),
                    "No se mostro el mensaje de éxito de autorización de la orden medica o se ha mostrado con errores.");
        
        //Buscamos en la tabla la orden, y validamos que esté autorizada
        String[] ordenTabla = tratamientoEditPage.buscarElementoEnTabla("Ordenes",null,"Cantidad de Sesiones="+ordenMedica[0]);
        String date = getNowDate();
        Assert.assertEquals(ordenTabla[2],"A124E2C3");
        Assert.assertEquals(ordenTabla[0],date);

        //Validamos que la orden ya no sea eliminable
        Assert.assertFalse(tratamientoEditPage.validarAccionHabilitada("Ordenes","Eliminar","Cantidad de Sesiones="+ordenMedica[0]),
                "Está habilitada la opción de eliminar orden medica, aunque la orden ya esté autorizzada.");
        
        //Validamos que el popup de autorizacion tenga el código cargado al abrirlo denuevo
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        Selenide.sleep(1000);
        Assert.assertEquals(ordenAutorizarPage.getTextInTextField("Codigo de autorizacion"),"A124E2C3",
                "No se muestra en el popup el codigo de autorización que la orden tiene actualmente.");

    }
    
    @Test
    @Parameters({"usuarioConOSyNroAfiliado","tratamPorOsUsuarioConOSyNroAfldConOrd","ordenTratamConOrdConOs","orden2TratamConOrdConOs"})
    public void test_repetirCodAutorizacionEnDistintasOrdenesTratamiento(String usuarioConOSyNroAfiliado,String tratamPorOsUsuarioConOSyNroAfldConOrd,String ordenTratamConOrdConOs,String orden2TratamConOrdConOs){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSyNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSyNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOs.split(";");
        String[] ordenMedica2 = orden2TratamConOrdConOs.split(";");
        String codAutorizacion = "EE11224A8BB";
        //Autorizamos la primera de las ordenes
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica[0]);
        ordenAutorizarPage.completeTextField("Codigo de autorizacion",codAutorizacion);
        ordenAutorizarPage.clickButton("Guardar");
        //Autorizamos la segunda de las ordenes
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Autorizar","Cantidad de Sesiones="+ordenMedica2[0]);
        ordenAutorizarPage.completeTextField("Codigo de autorizacion",codAutorizacion);
        ordenAutorizarPage.clickButton("Guardar");
                
        //Buscamos en la tabla la orden, y validamos que estén autorizadas
        String[] orden1Tabla = tratamientoEditPage.buscarElementoEnTabla("Ordenes",null,"Cantidad de Sesiones="+ordenMedica[0]);
        String[] orden2Tabla = tratamientoEditPage.buscarElementoEnTabla("Ordenes",null,"Cantidad de Sesiones="+ordenMedica2[0]);
        String date = getNowDate();
        Assert.assertEquals(orden1Tabla[2],codAutorizacion);
        Assert.assertEquals(orden1Tabla[0],date);
        Assert.assertEquals(orden2Tabla[2],codAutorizacion);
        Assert.assertEquals(orden2Tabla[0],date);

    }

    @Test
    @Parameters({"usuarioConOSSinNroAfiliado","tratamPorOsUsuarioConOSSinNroAfldConOrd","ordenTratamConOrdConOsSinNroAfiliafo"})
    public void test_cancelarEliminacionOrdenTratamiento(String usuarioConOSSinNroAfiliado,String tratamPorOsUsuarioConOSSinNroAfldConOrd,String ordenTratamConOrdConOsSinNroAfiliafo){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSSinNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSSinNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOsSinNroAfiliafo.split(";");
        //Clickeamos para eliminar la orden
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Eliminar","Cantidad de Sesiones="+ordenMedica[0]);
        tratamientoEditPage.confirmDelete(false);
        
        //Buscamos en la tabla la orden, y validamos que estén autorizadas
        String[] ordenTabla = tratamientoEditPage.buscarElementoEnTabla("Ordenes",null,"Cantidad de Sesiones="+ordenMedica[0]);
        Assert.assertNotNull(ordenTabla,"Se elimino la orden a pesar que se canceó la eliminación.");
    }
    
    @Test
    @Parameters({"usuarioConOSSinNroAfiliado","tratamPorOsUsuarioConOSSinNroAfldConOrd","ordenTratamConOrdConOsSinNroAfiliafo"})
    public void test_eliminacionExitosaOrdenTratamiento(String usuarioConOSSinNroAfiliado,String tratamPorOsUsuarioConOSSinNroAfldConOrd,String ordenTratamConOrdConOsSinNroAfiliafo){
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Tratamientos","Apellido, Nombre="+usuarioConOSSinNroAfiliado);
        
        String[] tratamiento = tratamPorOsUsuarioConOSSinNroAfldConOrd.split(";");
        
        tratamientoListPage.buscarElementoEnTabla("Tratamientos","Modificar","Tipo de Tratamiento="+tratamiento[0],"Sesiones Realizadas=0/"+tratamiento[1]);
        
        String[] ordenMedica = ordenTratamConOrdConOsSinNroAfiliafo.split(";");
        //Clickeamos para eliminar la orden
        tratamientoEditPage.buscarElementoEnTabla("Ordenes","Eliminar","Cantidad de Sesiones="+ordenMedica[0]);
        tratamientoEditPage.confirmDelete(true);
        
        //Validamos que se muestra el mensaje de éxito
        Assert.assertEquals(ordenCreatePage.getToasterMessageCount(1), 1);
        Assert.assertTrue(ordenCreatePage.toasterMessageDisplayed("Success", "Orden eliminada con éxito."),
                    "No se mostro el mensaje de éxito de eliminación de la orden medica o se ha mostrado con errores.");
        
        //Validamos que la tabla de ordenes esté vacía
        Assert.assertEquals(tratamientoEditPage.obtenerMensajeTablaVacia("Ordenes"),"(El tratamiento aún no posee ordenes médicas)");
        
    }
    
    
    private String getNowDate(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        String sDay = String.format("%02d",day);
        String sMonth = String.format("%02d",month);
        String sYear = "" + year;
        return sDay + "/" + sMonth + "/" + sYear;
    }
}
