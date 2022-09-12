package tingeso.mueblesstgo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.services.EmpleadoService;
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

	EmpleadoEntity empleado = new EmpleadoEntity();

	// Crea un empleado
	@Test
	void testEmpleado() {
		empleado.setRut("12345678-9");
		empleado.setNombres("Antonia");
		empleado.setApellidos("Chávez");
		empleado.setCategoria("A");
		empleadoService.guardarEmpleado(empleado);
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

