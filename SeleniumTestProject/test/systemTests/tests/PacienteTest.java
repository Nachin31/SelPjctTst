/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemTests.tests;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.commonPageObjects.CommonElementsPage;
import pageObjects.commonPageObjects.LoginPage;
import pageObjects.pacientePageObjects.PacienteListPage;
import java.util.Calendar;
import org.testng.annotations.BeforeClass;
import pageObjects.pacientePageObjects.PacienteCreatePage;
import pageObjects.pacientePageObjects.PacienteEditPage;

/**
 *
 * @author Nacho Gómez
 */
public class PacienteTest extends AutomatedTest{

    private LoginPage loginPage;
    private CommonElementsPage commonElementsPage;
    private PacienteListPage pacienteListPage;
    private PacienteCreatePage pacienteCrearPage;
    private PacienteEditPage pacienteEditPage;

    @BeforeClass
    @Parameters({"username", "password"})
    public void beforeClass(String username, String password) throws Exception {

        loginPage = new LoginPage();
        commonElementsPage = new CommonElementsPage();
        pacienteListPage = new PacienteListPage();
        pacienteCrearPage = new PacienteCreatePage();
        pacienteEditPage = new PacienteEditPage();

        loginPage.completeTextField("Username", username);
        loginPage.completeTextField("Password", password);
        loginPage.clickButton("IniciarSesion");

    }

    @Test
    public void test_breadCrumb() {
        Assert.assertEquals(commonElementsPage.getPageMenuItems(), "Home>Pacientes");
    }

    @Test
    @Parameters({"nombreBusqueda", "resultadosBusquedaNombre"})
    public void test_resultadosBusquedaPorNombre(String nombreBusqueda, String resultadosBusquedaNombre) {

        String[] resultadoBusquedaNombre = resultadosBusquedaNombre.split(";");

        pacienteListPage.completeTextField("FiltroPaciente", nombreBusqueda);
        pacienteListPage.clickToOrderByName();

        List<String[]> valores = pacienteListPage.obtenerListaDeValoresTabla("Pacientes","Apellido, Nombre");

        //Validamos apellidos y nombres listados (que todos los esperados aparezcan, y en orden)
        for (int i = 0; i < resultadoBusquedaNombre.length; i++) {
            Assert.assertEquals(valores.get(i)[0], resultadoBusquedaNombre[i]);
        }

        //Validamos que las cantidades sean las mismas (que no aparezcan más que los esperados)
        Assert.assertEquals(valores.size(), resultadoBusquedaNombre.length);

    }

    @Test
    @Parameters({"apellidoBusqueda", "resultadosBusquedaApellido"})
    public void test_resultadosBusquedaPorApellido(String apellidoBusqueda, String resultadosBusquedaApellido) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] resultadoBusquedaApellido = resultadosBusquedaApellido.split(";");

        pacienteListPage.completeTextField("FiltroPaciente", apellidoBusqueda);
        pacienteListPage.clickToOrderByName();

        List<String[]> valores = pacienteListPage.obtenerListaDeValoresTabla("Pacientes","Apellido, Nombre");

        //Validamos apellidos y nombres listados (que todos los esperados aparezcan, y en orden)
        for (int i = 0; i < resultadoBusquedaApellido.length; i++) {
            Assert.assertEquals(valores.get(i)[0], resultadoBusquedaApellido[i]);
        }

        //Validamos que las cantidades sean las mismas (que no aparezcan más que los esperados)
        Assert.assertEquals(valores.size(), resultadoBusquedaApellido.length);

    }

    @Test
    @Parameters({"apellidoynombreBusqueda", "resultadosBusquedaApellidoYNombre"})
    public void test_resultadosBusquedaPorNombreYApellido(String apellidoynombreBusqueda, String resultadosBusquedaApellidoYNombre) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] resultadoBusquedaApellidoyNombre = resultadosBusquedaApellidoYNombre.split(";");

        pacienteListPage.completeTextField("FiltroPaciente", apellidoynombreBusqueda);
        pacienteListPage.clickToOrderByName();

        List<String[]> valores = pacienteListPage.obtenerListaDeValoresTabla("Pacientes","Apellido, Nombre");

        //Validamos apellidos y nombres listados (que todos los esperados aparezcan, y en orden)
        for (int i = 0; i < resultadoBusquedaApellidoyNombre.length; i++) {
            Assert.assertEquals(valores.get(i)[0], resultadoBusquedaApellidoyNombre[i]);
        }

        //Validamos que las cantidades sean las mismas (que no aparezcan más que los esperados)
        Assert.assertEquals(valores.size(), resultadoBusquedaApellidoyNombre.length);

    }

    @Test
    @Parameters({"apellidoynombreBusquedaSinResultado"})
    public void test_busquedaSinResultado(String apellidoynombreBusquedaSinResultado) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.completeTextField("FiltroPaciente", apellidoynombreBusquedaSinResultado);
        pacienteListPage.clickToOrderByName();
        String mensaje = pacienteListPage.obtenerMensajeTablaVacia("Pacientes");

        Assert.assertEquals(mensaje, "(No se encontró ningún paciente)");

    }

    @Test
    @Parameters({"pacienteCarenteDeDatos"})
    public void test_busquedaPacienteCarenteDeAlgunosDatos(String pacienteCarenteDeDatos) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.completeTextField("FiltroPaciente", pacienteCarenteDeDatos);
        pacienteListPage.clickToOrderByName();
        
        List<String[]> valores = pacienteListPage.obtenerListaDeValoresTabla("Pacientes","Domicilio", "Teléfono", "Celular", "Edad", "Obra Social");

        for (String[] vals : valores) {
            for (String s : vals) {
                Assert.assertEquals(s, "-");
            }
        }

    }

    @Test
    public void test_camposObligatoriosAlCrearPaciente() {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.completeTextField("FiltroPaciente", "");
        pacienteListPage.clickButton("AgregarPaciente");

        //----- Se indica error de todos los campos vacíos---
        pacienteCrearPage.clickButton("Guardar");

        //Checkeamos que pida los 2 campos
        Assert.assertEquals(pacienteCrearPage.getToasterMessageCount(1), 2);

        //Checkeamos mensaje y color de los mensajes mostrados
        Assert.assertTrue(pacienteCrearPage.toasterMessageDisplayed("Error", "Ingrese el nombre del paciente"));
        Assert.assertTrue(pacienteCrearPage.toasterMessageDisplayed("Error", "Ingrese el apellido del paciente"));

        Assert.assertEquals(pacienteCrearPage.getCssAttribute("TextField", "Apellido", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(pacienteCrearPage.getCssAttribute("TextField", "Nombre", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(pacienteCrearPage.getCssAttribute("Label", "Apellido", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(pacienteCrearPage.getCssAttribute("Label", "Apellido", "color"), "rgba(240, 51, 105, 1)");

        //----- Se indica error del Nombre vacío---        
        pacienteCrearPage.completeTextField("Apellido", "xxxxx");
        pacienteCrearPage.clickButton("Guardar");

        Assert.assertEquals(pacienteListPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Error", "Ingrese el nombre del paciente"));

        Assert.assertEquals(pacienteCrearPage.getCssAttribute("TextField", "Apellido", "border-bottom"), "1px solid rgb(176, 190, 197)");
        Assert.assertEquals(pacienteCrearPage.getCssAttribute("TextField", "Nombre", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(pacienteCrearPage.getCssAttribute("Label", "Apellido", "color"), "rgba(84, 110, 122, 1)");
        Assert.assertEquals(pacienteCrearPage.getCssAttribute("Label", "Nombre", "color"), "rgba(240, 51, 105, 1)");

        //----- Se indica error del Apellido vacío---
        pacienteCrearPage.completeTextField("Apellido", "");
        pacienteCrearPage.completeTextField("Nombre", "xxxxx");
        pacienteCrearPage.clickButton("Guardar");

        Assert.assertEquals(pacienteListPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Error", "Ingrese el apellido del paciente"));

        Assert.assertEquals(pacienteCrearPage.getCssAttribute("TextField", "Apellido", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(pacienteCrearPage.getCssAttribute("TextField", "Nombre", "border-bottom"), "1px solid rgb(176, 190, 197)");

        Assert.assertEquals(pacienteCrearPage.getCssAttribute("Label", "Apellido", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(pacienteCrearPage.getCssAttribute("Label", "Nombre", "color"), "rgba(84, 110, 122, 1)");

        //Cerramos el popup
        pacienteCrearPage.cerrarPopup();
    }

    @Test
    @Parameters({"pacienteACrear"})
    public void test_creacionCanceladaDePaciente(String pacienteACrear) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] pacienteData = pacienteACrear.split(";");

        pacienteListPage.completeTextField("FiltroPaciente", "");
        pacienteListPage.clickButton("AgregarPaciente");

        pacienteCrearPage.completeTextField("Apellido", pacienteData[0]);
        pacienteCrearPage.completeTextField("Nombre", pacienteData[1]);
        pacienteCrearPage.completeTextField("DNI", pacienteData[2]);
        pacienteCrearPage.completeTextField("Domicilio", pacienteData[3]);
        pacienteCrearPage.completeTextField("Telefono", pacienteData[4]);
        pacienteCrearPage.completeTextField("Celular", pacienteData[5]);
        pacienteCrearPage.completeTextField("FechaNacimiento", pacienteData[6]);
        pacienteCrearPage.completeTextField("ObraSocial", pacienteData[7]);
        pacienteCrearPage.selectObraSocial(pacienteData[7]);
        pacienteCrearPage.completeTextField("NumeroAfiliado", pacienteData[8]);

        pacienteCrearPage.clickButton("Cancelar");

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] paciente = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ pacienteData[0] + ", " + pacienteData[1]);

        //Checkeamos que el paciente no se haya creado
        Assert.assertNull(paciente);

        //Validamos que todos los campos esten vacíos al abrir denuevo el popup
        pacienteListPage.clickButton("AgregarPaciente");

        Assert.assertEquals(pacienteCrearPage.getTextInTextField("Apellido"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("Nombre"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("DNI"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("Domicilio"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("Telefono"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("Celular"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("FechaNacimiento"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("ObraSocial"), "");
        Assert.assertEquals(pacienteCrearPage.getTextInTextField("NumeroAfiliado"), "");

        pacienteCrearPage.cerrarPopup();

    }

    @Test
    @Parameters({"pacienteACrear"})
    public void test_creacionExitosaPaciente(String pacienteACrear) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] pacienteData = pacienteACrear.split(";");

        pacienteListPage.completeTextField("FiltroPaciente", "");
        pacienteListPage.clickButton("AgregarPaciente");

        pacienteCrearPage.completeTextField("Apellido", pacienteData[0]);
        pacienteCrearPage.completeTextField("Nombre", pacienteData[1]);
        pacienteCrearPage.completeTextField("DNI", pacienteData[2]);
        pacienteCrearPage.completeTextField("Domicilio", pacienteData[3]);
        pacienteCrearPage.completeTextField("Telefono", pacienteData[4]);
        pacienteCrearPage.completeTextField("Celular", pacienteData[5]);
        pacienteCrearPage.completeTextField("FechaNacimiento", pacienteData[6]);
        pacienteCrearPage.completeTextField("ObraSocial", pacienteData[7]);
        pacienteCrearPage.selectObraSocial(pacienteData[7]);
        pacienteCrearPage.completeTextField("NumeroAfiliado", pacienteData[8]);

        pacienteCrearPage.clickButton("Guardar");

        //Checkeamos que el mensaje de éxito es mostrado
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Success", "Paciente creado con éxito.", 2));

        //Validamos que fue redirigido a la lista de tratamientos
//      Assert.assertEquals(commonElementsPage.getPageMenuItems(),"Home>Pacientes>Tratamientos de "+pacienteData[0]+", "+pacienteData[1]);
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] paciente = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ pacienteData[0] + ", " + pacienteData[1]);
        
        //Checkeamos que se haya creado
        Assert.assertNotNull(paciente);

        //Checkeamos que la data que muestra la lista sea correcta
        Assert.assertEquals(paciente[0], pacienteData[0] + ", " + pacienteData[1]);
        Assert.assertEquals(paciente[1], pacienteData[3]);
        Assert.assertEquals(paciente[2], pacienteData[4]);
        Assert.assertEquals(paciente[3], pacienteData[5]);
        Assert.assertEquals(paciente[4], calcularEdad(pacienteData[6]));
        Assert.assertEquals(paciente[5], pacienteData[7]);

        pacienteListPage.buscarElementoEnTabla("Pacientes","Modificar","Apellido, Nombre="+ pacienteData[0] + ", " + pacienteData[1]);
        
        //Checkeamos que la data que muestra al querer editar el paciente sea correcta
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Apellido"), pacienteData[0]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Nombre"), pacienteData[1]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("DNI"), pacienteData[2]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Domicilio"), pacienteData[3]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Telefono"), pacienteData[4]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Celular"), pacienteData[5]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("FechaNacimiento"), pacienteData[6]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("ObraSocial"), pacienteData[7]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("NumeroAfiliado"), pacienteData[8]);

        pacienteEditPage.cerrarPopup();
    }

    @Test
    @Parameters({"pacienteACrear"})
    public void test_imposibleCrearPacienteExistente(String pacienteACrear) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] pacienteData = pacienteACrear.split(";");

        pacienteListPage.completeTextField("FiltroPaciente", "");
        pacienteListPage.clickButton("AgregarPaciente");

        pacienteCrearPage.completeTextField("Apellido", pacienteData[0]);
        pacienteCrearPage.completeTextField("Nombre", pacienteData[1]);
        pacienteCrearPage.completeTextField("DNI", pacienteData[2]);
        pacienteCrearPage.completeTextField("Domicilio", pacienteData[3]);
        pacienteCrearPage.completeTextField("Telefono", pacienteData[4]);
        pacienteCrearPage.completeTextField("Celular", pacienteData[5]);
        pacienteCrearPage.completeTextField("FechaNacimiento", pacienteData[6]);
        pacienteCrearPage.completeTextField("ObraSocial", pacienteData[7]);
        pacienteCrearPage.selectObraSocial(pacienteData[7]);
        pacienteCrearPage.completeTextField("NumeroAfiliado", pacienteData[8]);

        pacienteCrearPage.clickButton("Guardar");

        //Checkeamos que el mensaje de éxito es mostrado
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Error", "El paciente cargado ya existe.", 2));

        pacienteCrearPage.cerrarPopup();

    }

    @Test
    @Parameters({"pacienteACrear","pacienteAEditar"})
    public void test_edicionPacienteCancelada(String pacienteACrear,String pacienteAEditar) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] newPacienteData = pacienteAEditar.split(";");
        String[] oldPacienteFullData = pacienteACrear.split(";");
        String[] oldPacienteData = pacienteListPage.buscarElementoEnTabla("Pacientes","Modificar","Apellido, Nombre="+ newPacienteData[0] + ", " + newPacienteData[1]);
        
        pacienteEditPage.completeTextField("Apellido", newPacienteData[0]);
        pacienteEditPage.completeTextField("Nombre", newPacienteData[1]);
        pacienteEditPage.completeTextField("DNI", newPacienteData[2]);
        pacienteEditPage.completeTextField("Domicilio", newPacienteData[3]);
        pacienteEditPage.completeTextField("Telefono", newPacienteData[4]);
        pacienteEditPage.completeTextField("Celular", newPacienteData[5]);
        pacienteEditPage.completeTextField("FechaNacimiento", newPacienteData[6]);
        pacienteEditPage.completeTextField("ObraSocial", newPacienteData[7]);
        pacienteEditPage.selectObraSocial(newPacienteData[7]);
        pacienteEditPage.completeTextField("NumeroAfiliado", newPacienteData[8]);

        pacienteEditPage.clickButton("Cancelar");

        //Buscamos el paciente y validamos que tenga los datos viejos
        String[] paciente = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ newPacienteData[0] + ", " + newPacienteData[1]);
        
        //Checkeamos que la data que muestra la lista sea correcta
        Assert.assertEquals(paciente[0], oldPacienteData[0]);
        Assert.assertEquals(paciente[1], oldPacienteData[1]);
        Assert.assertEquals(paciente[2], oldPacienteData[2]);
        Assert.assertEquals(paciente[3], oldPacienteData[3]);
        Assert.assertEquals(paciente[4], oldPacienteData[4]);
        Assert.assertEquals(paciente[5], oldPacienteData[5]);

        
        //Buscamos el paciente y abrimos el popup de edición para ver que los datos sean los datos viejos
        pacienteListPage.buscarElementoEnTabla("Pacientes","Modificar","Apellido, Nombre="+ newPacienteData[0] + ", " + newPacienteData[1]);
        
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Apellido"), oldPacienteFullData[0]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Nombre"), oldPacienteFullData[1]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("DNI"), oldPacienteFullData[2]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Domicilio"), oldPacienteFullData[3]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Telefono"), oldPacienteFullData[4]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Celular"), oldPacienteFullData[5]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("FechaNacimiento"), oldPacienteFullData[6]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("ObraSocial"), oldPacienteFullData[7]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("NumeroAfiliado"), oldPacienteFullData[8]);
        
        pacienteEditPage.cerrarPopup();
        
    }
    
    @Test
    @Parameters({"pacienteACrear"})
    public void test_campsObligatoriosAlEditarPaciente(String pacienteACrear) {

        String[] pacienteData = pacienteACrear.split(";");
        
        commonElementsPage.clickMenuOption("Listado de Pacientes");

        pacienteListPage.buscarElementoEnTabla("Pacientes","Modificar","Apellido, Nombre="+ pacienteData[0] + ", " + pacienteData[1]);
        
        pacienteEditPage.completeTextField("Apellido","");
        pacienteEditPage.completeTextField("Nombre","");
        
        //----- Se indica error de todos los campos vacíos---
        pacienteEditPage.clickButton("Guardar");

        //Checkeamos que pida los 2 campos
        Assert.assertEquals(pacienteEditPage.getToasterMessageCount(1), 2);

        //Checkeamos mensaje y color de los mensajes mostrados
        Assert.assertTrue(pacienteEditPage.toasterMessageDisplayed("Error", "Ingrese el nombre del paciente"));
        Assert.assertTrue(pacienteEditPage.toasterMessageDisplayed("Error", "Ingrese el apellido del paciente"));

        Assert.assertEquals(pacienteEditPage.getCssAttribute("TextField", "Apellido", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(pacienteEditPage.getCssAttribute("TextField", "Nombre", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(pacienteEditPage.getCssAttribute("Label", "Apellido", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(pacienteEditPage.getCssAttribute("Label", "Apellido", "color"), "rgba(240, 51, 105, 1)");

        //----- Se indica error del Nombre vacío---        
        pacienteEditPage.completeTextField("Apellido", "xxxxx");
        pacienteEditPage.clickButton("Guardar");

        Assert.assertEquals(pacienteEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(pacienteEditPage.toasterMessageDisplayed("Error", "Ingrese el nombre del paciente"));

        Assert.assertEquals(pacienteEditPage.getCssAttribute("TextField", "Apellido", "border-bottom"), "1px solid rgb(176, 190, 197)");
        Assert.assertEquals(pacienteEditPage.getCssAttribute("TextField", "Nombre", "border-bottom"), "1px solid rgb(240, 51, 105)");

        Assert.assertEquals(pacienteEditPage.getCssAttribute("Label", "Apellido", "color"), "rgba(84, 110, 122, 1)");
        Assert.assertEquals(pacienteEditPage.getCssAttribute("Label", "Nombre", "color"), "rgba(240, 51, 105, 1)");

        //----- Se indica error del Apellido vacío---
        pacienteEditPage.completeTextField("Apellido", "");
        pacienteEditPage.completeTextField("Nombre", "xxxxx");
        pacienteEditPage.clickButton("Guardar");

        Assert.assertEquals(pacienteEditPage.getToasterMessageCount(1), 1);
        Assert.assertTrue(pacienteEditPage.toasterMessageDisplayed("Error", "Ingrese el apellido del paciente"));

        Assert.assertEquals(pacienteEditPage.getCssAttribute("TextField", "Apellido", "border-bottom"), "1px solid rgb(240, 51, 105)");
        Assert.assertEquals(pacienteEditPage.getCssAttribute("TextField", "Nombre", "border-bottom"), "1px solid rgb(176, 190, 197)");

        Assert.assertEquals(pacienteEditPage.getCssAttribute("Label", "Apellido", "color"), "rgba(240, 51, 105, 1)");
        Assert.assertEquals(pacienteEditPage.getCssAttribute("Label", "Nombre", "color"), "rgba(84, 110, 122, 1)");

        //Cerramos el popup
        pacienteEditPage.cerrarPopup();
    }
            
    @Test
    @Parameters({"pacienteAEditar"})
    public void test_edicionPacienteExitosa(String pacienteAEditar) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] newFullPacienteData = pacienteAEditar.split(";");
        String[] oldPacienteData = pacienteListPage.buscarElementoEnTabla("Pacientes","Modificar","Apellido, Nombre="+ newFullPacienteData[0] + ", " + newFullPacienteData[1]);

        pacienteEditPage.completeTextField("Apellido", newFullPacienteData[0]);
        pacienteEditPage.completeTextField("Nombre", newFullPacienteData[1]);
        pacienteEditPage.completeTextField("DNI", newFullPacienteData[2]);
        pacienteEditPage.completeTextField("Domicilio", newFullPacienteData[3]);
        pacienteEditPage.completeTextField("Telefono", newFullPacienteData[4]);
        pacienteEditPage.completeTextField("Celular", newFullPacienteData[5]);
        pacienteEditPage.completeTextField("FechaNacimiento", newFullPacienteData[6]);
        pacienteEditPage.completeTextField("ObraSocial", newFullPacienteData[7]);
        pacienteEditPage.selectObraSocial(newFullPacienteData[7]);
        pacienteEditPage.completeTextField("NumeroAfiliado", newFullPacienteData[8]);

        pacienteEditPage.clickButton("Guardar");

        //Checkeamos que el mensaje de éxito es mostrado
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Success", "Paciente modificado con éxito.", 2));

        //Buscamos el paciente y validamos que tenga los datos viejos
        String[] pacienteTableData = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ newFullPacienteData[0] + ", " + newFullPacienteData[1]);

        //Checkeamos que la data que muestra la lista sea correcta
        Assert.assertEquals(pacienteTableData[0], newFullPacienteData[0]+", "+newFullPacienteData[1]);
        Assert.assertEquals(pacienteTableData[1], newFullPacienteData[3]);
        Assert.assertEquals(pacienteTableData[2], newFullPacienteData[4].equals("")?"-":"");
        Assert.assertEquals(pacienteTableData[3], newFullPacienteData[5]);
        Assert.assertEquals(pacienteTableData[4], calcularEdad(newFullPacienteData[6]));
        Assert.assertEquals(pacienteTableData[5], newFullPacienteData[7]);

        pacienteListPage.buscarElementoEnTabla("Pacientes","Modificar","Apellido, Nombre="+ newFullPacienteData[0] + ", " + newFullPacienteData[1]);

        //Checkeamos que en el popup de paciente la data este actualizada también
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Apellido"), newFullPacienteData[0]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Nombre"), newFullPacienteData[1]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("DNI"), newFullPacienteData[2]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Domicilio"), newFullPacienteData[3]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Telefono"), newFullPacienteData[4]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("Celular"), newFullPacienteData[5]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("FechaNacimiento"), newFullPacienteData[6]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("ObraSocial"), newFullPacienteData[7]);
        Assert.assertEquals(pacienteEditPage.getTextInTextField("NumeroAfiliado"), newFullPacienteData[8]);
        
        pacienteEditPage.cerrarPopup();
    }
    
    @Test
    @Parameters({"pacienteAEditar"})
    public void test_cancelarEliminacionPaciente(String pacienteAEditar) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] pacienteFullData = pacienteAEditar.split(";");
        
        //Seleccionamos el usuario para eliminarlo, pero cancelamos eliminación
        String[] pacienteTableData = pacienteListPage.buscarElementoEnTabla("Pacientes","Eliminar","Apellido, Nombre="+ pacienteFullData[0] + ", " + pacienteFullData[1]);
        
        pacienteListPage.confirmDelete(false);
        pacienteTableData = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ pacienteFullData[0] + ", " + pacienteFullData[1]);
        
        //Validamos que el usuario aún existe
        Assert.assertNotNull(pacienteTableData);
        
    }
    
    @Test
    @Parameters({"pacienteAEditar"})
    public void test_eliminacionExitosaPaciente(String pacienteAEditar) {

        commonElementsPage.clickMenuOption("Listado de Pacientes");

        String[] pacienteFullData = pacienteAEditar.split(";");
        
        //Seleccionamos el usuario para eliminarlo y confirmamos eliminación
        String[] pacienteTableData = pacienteListPage.buscarElementoEnTabla("Pacientes","Eliminar","Apellido, Nombre="+ pacienteFullData[0] + ", " + pacienteFullData[1]);
        
        pacienteListPage.confirmDelete(true);
        
        //Validamos el mensaje de confirmación y que el usuario no se liste más
        Assert.assertTrue(pacienteListPage.toasterMessageDisplayed("Success", "Paciente eliminado con éxito.", 2));
        
        pacienteTableData = pacienteListPage.buscarElementoEnTabla("Pacientes",null,"Apellido, Nombre="+ pacienteFullData[0] + ", " + pacienteFullData[1]);
        
        Assert.assertNull(pacienteTableData);
        
    }
                            
    private String calcularEdad(String fechaNacimiento) {

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        String diaNac = fechaNacimiento.split("/")[0];
        String mesNac = fechaNacimiento.split("/")[1];
        String anioNac = fechaNacimiento.split("/")[2];

        date2.set(Integer.parseInt(anioNac), Integer.parseInt(mesNac)-1, Integer.parseInt(diaNac));

        Calendar aux = Calendar.getInstance();
        aux.setTimeInMillis(date1.getTimeInMillis() - date2.getTimeInMillis());

        //A tomar en cuenta que el año minimo es 1970 por eso le resto esa cantidad 
        int edad = aux.get(Calendar.YEAR) - 1970;

        return "" + edad;
    }

}
