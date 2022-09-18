package tingeso.mueblesstgo;


import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.HorasExtraEntity;
import tingeso.mueblesstgo.entities.JustificativosEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import tingeso.mueblesstgo.repositories.HorasExtraRepository;
import tingeso.mueblesstgo.repositories.JustificativoRepository;
import tingeso.mueblesstgo.repositories.MarcasRelojRepository;
import tingeso.mueblesstgo.services.EmpleadoService;
import tingeso.mueblesstgo.services.HorasExtraService;
import tingeso.mueblesstgo.services.MarcasRelojService;
import tingeso.mueblesstgo.services.JustificativoService;
import tingeso.mueblesstgo.services.OficinaRRHHService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MueblesstgoApplicationTests {

	@Test
	void contextLoads() {
	}

}
@SpringBootTest
class SueldosTest{
	@Autowired
	OficinaRRHHService oficinaRRHHService;
	@Test
	void calcularSueldosTest(){
		oficinaRRHHService.calcularSueldos();
	}
}
@SpringBootTest
class EmpleadoTest {
	@Autowired
	private EmpleadoService empleadoService;
	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Test
	void testEmpleado() {
		EmpleadoEntity empleado = new EmpleadoEntity();
		empleado.setRut("12.345.678-9");
		empleado.setNombres("Antonia");
		empleado.setApellidos("Chávez");
		empleado.setFechaNacimiento("2002/05/15");
		empleado.setCategoria("A");
		empleado.setFechaIngreso("2020/05/15");
		empleadoService.guardarEmpleado(empleado);

		EmpleadoEntity empleado1 = new EmpleadoEntity();
		empleado1.setRut("11.234.123-6");
		empleado1.setNombres("Christopher");
		empleado1.setApellidos("Torres");
		empleado1.setFechaNacimiento("2002/05/25");
		empleado1.setCategoria("A");
		empleado1.setFechaIngreso("2018/05/25");
		empleadoService.guardarEmpleado(empleado1);

		EmpleadoEntity empleado2 = new EmpleadoEntity();
		empleado2.setRut("12.457.562-3");
		empleado2.setNombres("John");
		empleado2.setApellidos("Serrano");
		empleado2.setFechaNacimiento("2000/10/13");
		empleado2.setCategoria("B");
		empleado2.setFechaIngreso("2019/10/13");
		empleadoService.guardarEmpleado(empleado2);

		EmpleadoEntity empleado3 = new EmpleadoEntity();
		empleado3.setRut("21.142.354-k");
		empleado3.setNombres("Xavier");
		empleado3.setApellidos("Muñoz");
		empleado3.setFechaNacimiento("1997/11/15");
		empleado3.setCategoria("C");
		empleado3.setFechaIngreso("2017/11/15");
		empleadoService.guardarEmpleado(empleado3);

		EmpleadoEntity empleado4 = new EmpleadoEntity();
		empleado4.setRut("17.765.876-2");
		empleado4.setNombres("Nícolas");
		empleado4.setApellidos("Farfán");
		empleado4.setFechaNacimiento("2001/06/20");
		empleado4.setCategoria("A");
		empleado4.setFechaIngreso("2004/06/20");
		empleadoService.guardarEmpleado(empleado4);
	}

	@Test
	public void encontrarEmpleadoPorRut() {
		EmpleadoEntity empleado = new EmpleadoEntity();
		empleado.setRut("27.134.678-6");
		empleado.setNombres("Alex");
		empleado.setApellidos("Pacheco");
		empleadoService.guardarEmpleado(empleado);
		EmpleadoEntity empleadoRut = empleadoRepository.findByRut(empleado.getRut());
		assertEquals(empleadoRut.getRut(), empleado.getRut());
	}
}

@SpringBootTest
class MarcasRelojTest{
	@Autowired
	private MarcasRelojRepository marcasRelojRepository;
	@Autowired
	private EmpleadoRepository empleadoRepository;
	@Autowired
	private MarcasRelojService marcasRelojService;
	@Test
	void marcasRelojTest(){
		MarcasRelojEntity marcasReloj = new MarcasRelojEntity();
		marcasReloj.setFecha("2022/08/10");
		marcasReloj.setHora("08:00");
		marcasReloj.setHoraSalida("18:00");
		marcasReloj.setEmpleado(empleadoRepository.findByRut("17.765.876-2"));
		marcasRelojRepository.save(marcasReloj);
	}

	@Test
	void marcasRelojTest2(){
		marcasRelojService.crearMarcaReloj("2022/08/11", "08:30", "17.765.876-2");
	}

	@Test
	void encontrarFechaYEmpleado(){
		marcasRelojService.crearMarcaReloj("2022/08/07", "08:10", "21.142.354-k");
		MarcasRelojEntity marcasReloj = marcasRelojRepository.findByFechaAndEmpleado("2022/08/07", empleadoRepository.findByRut("21.142.354-k"));
		assertEquals(marcasReloj.getFecha(), "2022/08/07");
		assertEquals(marcasReloj.getEmpleado().getRut(), "21.142.354-k");
	}
}

@SpringBootTest
class HorasExtraTest {
	@Autowired
	private HorasExtraRepository horasExtraRepository;
	@Autowired
	private EmpleadoRepository empleadoRepository;
	@Autowired
	private HorasExtraService horasExtraService;
	@Autowired
	private MarcasRelojRepository marcasRelojRepository;

	@Test
	void horasExtraTest() {
		MarcasRelojEntity marcasReloj = new MarcasRelojEntity();
		marcasReloj.setFecha("2022/08/05");
		marcasReloj.setHora("08:00");
		marcasReloj.setHoraSalida("20:31");
		marcasReloj.setEmpleado(empleadoRepository.findByRut("21.142.354-k"));
		marcasRelojRepository.save(marcasReloj);
  		//////////////////////////

		assertThat(horasExtraService.guardarHorasExtra(2, "21.142.354-k", "2022/08/05")).isTrue();
	}

	@Test
	void horasExtraTest2() {
		MarcasRelojEntity marcasReloj = new MarcasRelojEntity();
		marcasReloj.setFecha("2022/08/06");
		marcasReloj.setHora("08:00");
		marcasReloj.setHoraSalida("20:31");
		marcasReloj.setEmpleado(empleadoRepository.findByRut("21.142.354-k"));
		marcasRelojRepository.save(marcasReloj);

		// Horas incorrectas
		assertThat(horasExtraService.guardarHorasExtra(3, "21.142.354-k", "2022/08/05")).isFalse();
	}
}

@SpringBootTest
class JustificativoTest {
	@Autowired
	private JustificativoService justificativoService;

	@Test
	void justificativoTest(){
		assertThat(justificativoService.guardarJustificativo("2022/08/01", "21.142.354-k")).isTrue();
	}
}





