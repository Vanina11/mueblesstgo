package tingeso.mueblesstgo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;
import tingeso.mueblesstgo.services.EmpleadoService;
import tingeso.mueblesstgo.services.MarcasRelojService;
import tingeso.mueblesstgo.services.OficinaRRHHService;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MueblesstgoApplicationTests {

	@Test
	void contextLoads() {
	}

}

@SpringBootTest
class EmpleadoTest {

	private TestEntityManager entityManager;

	@Autowired
	private EmpleadoService empleadoService;



	// Crea un empleado
	@Test
	void testEmpleado() {
		EmpleadoEntity empleado = new EmpleadoEntity();
		empleado.setRut("12.345.678-9");
		empleado.setNombres("Antonia");
		empleado.setApellidos("Chávez");
		empleado.setCategoria("A");
		empleadoService.guardarEmpleado(empleado);

		EmpleadoEntity empleado1 = new EmpleadoEntity();
		empleado1.setRut("11.234.123-6");
		empleado1.setNombres("Christopher");
		empleado1.setApellidos("Chávez");
		empleado1.setCategoria("A");
		empleadoService.guardarEmpleado(empleado1);

		EmpleadoEntity empleado2 = new EmpleadoEntity();
		empleado2.setRut("12.457.562-3");
		empleado2.setNombres("John");
		empleado2.setApellidos("Chávez");
		empleado2.setCategoria("A");
		empleadoService.guardarEmpleado(empleado2);

		EmpleadoEntity empleado3 = new EmpleadoEntity();
		empleado3.setRut("21.142.354-k");
		empleado3.setNombres("Xavier");
		empleado3.setApellidos("Chávez");
		empleado3.setCategoria("A");
		empleadoService.guardarEmpleado(empleado3);

		EmpleadoEntity empleado4 = new EmpleadoEntity();
		empleado4.setRut("17.765.876-2");
		empleado4.setNombres("Nícolas");
		empleado4.setApellidos("Chávez");
		empleado4.setCategoria("A");
		empleadoService.guardarEmpleado(empleado4);
	}

	@Test
	public void encontrarEmpleadoPorRut() {
		// given
		EmpleadoEntity empleado1 = new EmpleadoEntity();
		empleado1.setRut("27.134.678-6");
		empleado1.setNombres("Peter");
		empleado1.setApellidos("Chávez");
		empleado1.setCategoria("A");

		entityManager.persistAndFlush(empleado1);
	/*
		// when .get
		EmpleadoEntity empleado2 = empleadoRepository.findByRut(empleado1.getRut());

		// then
		assertThat(empleado2.getRut())
				.isEqualTo(empleado1.getRut());

	 */
	}
}

@SpringBootTest
class OficinaRRHHTest{

	@Autowired
	private OficinaRRHHService oficinaRRHHService;
	EmpleadoEntity empleado = new EmpleadoEntity();
	OficinaRRHHService oficinaRRHH = new OficinaRRHHService();

	// Calcula el sueldo fijo según categoría del empleado
	@Test
	void testAsignarSueldoFijo() {
		empleado.setCategoria("A");
		oficinaRRHH.asignarSueldoFijo(empleado);
		assertEquals(1700000, empleado.getSueldoFijo());
	}

	// Calcula los años de servicio del empleado
	@Test
	void testCalcularAniosServicio() {
		// Asigna fecha de ingreso
		Calendar fechaIngreso = Calendar.getInstance();
		fechaIngreso.set(2014,05,05);
		empleado.setFechaIngreso(fechaIngreso);
		// Calcula años de servicio, considerando 2022 como el año actual, deberían ser 8 años
		oficinaRRHH.calcularAniosServicio(empleado);
		assertEquals(8, empleado.getAniosServicio());
	}

	@Test
	void testCalcularBono() {
		empleado.setCategoria("A");
		oficinaRRHH.asignarSueldoFijo(empleado);
		Calendar fechaIngreso = Calendar.getInstance();
		fechaIngreso.set(2014,05,05);
		empleado.setFechaIngreso(fechaIngreso);
		oficinaRRHH.calcularAniosServicio(empleado);
		double bono = oficinaRRHH.calcularBonoAniosServicio(empleado);
		assertEquals(85000, bono);
	}

	@Test
	void testMontoHorasExtras() {
		empleado.setCategoria("A");
		double montoHorasExtras = oficinaRRHH.montoHorasExtras(empleado);
		assertEquals(25000, montoHorasExtras);
	}

	@Test
	void testDescuentoMinutos() {
		empleado.setCategoria("A");
		oficinaRRHH.asignarSueldoFijo(empleado);
		double descuentoMinutos = oficinaRRHH.montoDescuentoAtrasos(empleado, 20);
		assertEquals(17000, descuentoMinutos);
	}
}
/*
@SpringBootTest
class MarcasRelojTest{
	@Autowired
	private MarcasRelojService marcasRelojService;
	@Test
	void marcasRelojTest(){
		MarcasRelojEntity marcasReloj = new MarcasRelojEntity();
		marcasReloj.setFecha(Calendar.getInstance().set(2022,08,23););
		marcasReloj.setHora("08:00");
		//marcasRelojService.save(marcasReloj);
	}
}
*/
