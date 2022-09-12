package tingeso.mueblesstgo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.services.EmpleadoService;

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
