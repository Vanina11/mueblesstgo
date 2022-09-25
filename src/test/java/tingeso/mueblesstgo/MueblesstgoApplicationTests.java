package tingeso.mueblesstgo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.mueblesstgo.entities.*;
import tingeso.mueblesstgo.repositories.*;
import tingeso.mueblesstgo.services.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmpleadoTest {
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    String RUT = "12345678-9";

    @Test
    void obtenerPorRutTest() {
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);
        EmpleadoEntity empleadoRut = empleadoService.obtenerPorRut(RUT);
        assertEquals(empleadoRut.getRut(), RUT);
        empleadoRepository.delete(empleado);
    }

    @Test
    void obtenerEmpleadosTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);
        List<EmpleadoEntity> empleados = empleadoService.obtenerEmpleados();
        assertThat(empleados).isNotEmpty();
        empleadoRepository.delete(empleado);
    }

    @Test
    void incrementaInasistenciasTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);
        empleadoService.incrementaInasistencias(empleado);
        EmpleadoEntity empleadoRut = empleadoService.obtenerPorRut(RUT);
        assertEquals(1,empleadoRut.getInasistencias());
        empleadoRepository.delete(empleado);
    }

    @Test
    void incrementaDescuentoAtrasoTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);
        empleadoService.incrementaDescuentoAtraso(empleado, 15);
        EmpleadoEntity empleadoRut = empleadoService.obtenerPorRut(RUT);
        assertEquals(15,empleadoRut.getDescuentoAtraso());
        empleadoRepository.delete(empleado);
    }
}

@SpringBootTest
class MarcasRelojTest{
    @Autowired
    MarcasRelojService marcasRelojService;
    @Autowired
    MarcasRelojRepository marcasRelojRepository;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    EmpleadoRepository empleadoRepository;
    String RUT = "12345678-9";
    String FECHA = "2022/08/23";
    String HORA = "08:00";
    String HORA_2 = "08:50";
    Integer MINUTOS_ATRASO = 13;
    Integer MINUTOS_ATRASO_2 = 28;
    Integer MINUTOS_ATRASO_3 = 48;
    Integer MINUTOS_ATRASO_4 = 78;
    String LINEA = "2022/08/23;08:00;12345678-9";

    @Test
    void obtenerMarcasEmpleadoTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        marcasRelojService.crearMarcaReloj(FECHA, HORA, RUT);
        List<MarcasRelojEntity> marcas = marcasRelojService.obtenerMarcasRelojPorEmpleado(empleado);
        assertThat(marcas).isNotEmpty();

        marcasRelojRepository.delete(marcas.get(0));
        empleadoRepository.delete(empleado);
    }

    @Test
    void obtenerMarcasEmpleadoYFechaTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        marcasRelojService.crearMarcaReloj(FECHA, HORA, RUT);
        MarcasRelojEntity marcas = marcasRelojService.obtenerMarcaRelojPorFechaYEmpleado(FECHA, empleado);
        assertEquals(marcas.getFecha(), FECHA);
        assertEquals(marcas.getEmpleado().getRut(), RUT);

        marcasRelojRepository.delete(marcas);
        empleadoRepository.delete(empleado);
    }
    @Test
    void descuentoAtrasosTest(){
        Integer descuento = marcasRelojService.montoDescuentoAtrasos(MINUTOS_ATRASO);
        assertEquals(1, descuento);
    }
    @Test
    void descuentoAtrasosTest2(){
        Integer descuento = marcasRelojService.montoDescuentoAtrasos(MINUTOS_ATRASO_2);
        assertEquals(3, descuento);
    }
    @Test
    void descuentoAtrasosTest3(){
        Integer descuento = marcasRelojService.montoDescuentoAtrasos(MINUTOS_ATRASO_3);
        assertEquals(6, descuento);
    }
    @Test
    void descuentoAtrasosTest4(){
        Integer descuento = marcasRelojService.montoDescuentoAtrasos(MINUTOS_ATRASO_4);
        assertEquals(15, descuento);
    }
    @Test
    void calcularDescuentoAtraso(){
        Integer descuento = marcasRelojService.calcularDescuentoAtraso(HORA);
        assertEquals(0, descuento);
    }
    @Test
    void calcularDescuentoAtraso2(){
        Integer descuento = marcasRelojService.calcularDescuentoAtraso(HORA_2);
        assertEquals(3, descuento);
    }
    @Test
    void crearMarcaRelojTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        marcasRelojService.crearMarcaReloj(FECHA, HORA, RUT);
        MarcasRelojEntity marcas = marcasRelojService.obtenerMarcaRelojPorFechaYEmpleado(FECHA, empleado);
        assertThat(marcas).isNotNull();

        marcasRelojRepository.delete(marcas);
        empleadoRepository.delete(empleado);
    }
    @Test
    void leerLineaTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        marcasRelojService.leerLinea(LINEA);
        MarcasRelojEntity marcas = marcasRelojService.obtenerMarcaRelojPorFechaYEmpleado(FECHA, empleado);
        assertThat(marcas).isNotNull();

        marcasRelojRepository.delete(marcas);
        empleadoRepository.delete(empleado);
    }
}

@SpringBootTest
class HorasExtraTest{
    @Autowired
    HorasExtraRepository horasExtraRepository;
    @Autowired
    HorasExtraService horasExtraService;
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    MarcasRelojService marcasRelojService;
    @Autowired
    MarcasRelojRepository marcasRelojRepository;
    String RUT = "12345678-9";
    String MES = "08";
    Integer CANTIDAD = 3;
    Integer CANTIDAD_2 = 2;
    String FECHA = "2022/08/23";
    String HORA = "08:00";
    String LINEA = "2022/08/23;21:00;12345678-9";

    @Test
    void obtenerHorasExtraRutTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        horasExtraService.crearHora(CANTIDAD, empleado, MES);
        List<HorasExtraEntity> horasExtra = horasExtraService.obtenerHorasExtraPorRut(empleado);
        assertEquals(RUT, horasExtra.get(0).getEmpleado().getRut());

        horasExtraRepository.delete(horasExtra.get(0));
        empleadoRepository.delete(empleado);
    }

    @Test
    void verificarHorasExtraTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        marcasRelojService.crearMarcaReloj(FECHA, HORA, RUT);
        marcasRelojService.leerLinea(LINEA);

        boolean resultado = horasExtraService.verificaHorasExtra(CANTIDAD, empleado, FECHA);
        assertEquals(true, resultado);

        marcasRelojRepository.delete(marcasRelojService.obtenerMarcaRelojPorFechaYEmpleado(FECHA, empleado));
        empleadoRepository.delete(empleado);
    }
    @Test
    void crearHoraExtraTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        marcasRelojService.crearMarcaReloj(FECHA, HORA, RUT);
        marcasRelojService.leerLinea(LINEA);

        boolean resultado = horasExtraService.guardarHorasExtra(CANTIDAD_2, RUT, MES);
        // Horas extra realizadas no coinciden con ingresadas
        assertEquals(false, resultado);

        marcasRelojRepository.delete(marcasRelojService.obtenerMarcaRelojPorFechaYEmpleado(FECHA, empleado));
        empleadoRepository.delete(empleado);
    }
    @Test
    void crearHoraExtra2Test(){
        boolean resultado = horasExtraService.guardarHorasExtra(CANTIDAD, "11111111-1", MES);
        assertEquals(false, resultado);
    }
    /*
    @Test
    void crearHoraExtraTest2(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        marcasRelojService.crearMarcaReloj(FECHA, HORA, RUT);
        marcasRelojService.leerLinea(LINEA);

        boolean resultado = horasExtraService.guardarHorasExtra(CANTIDAD, RUT, MES);
        assertEquals(true, resultado);

        horasExtraRepository.delete(horasExtraService.obtenerHorasExtraPorRut(empleado).get(0));
        marcasRelojRepository.delete(marcasRelojService.obtenerMarcaRelojPorFechaYEmpleado(FECHA, empleado));
        empleadoRepository.delete(empleado);
    }
     */
}

@SpringBootTest
class JustificativoTest {
	@Autowired
	JustificativoService justificativoService;
    @Autowired
    JustificativoRepository justificativoRepository;
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    EmpleadoService empleadoService;
    String RUT = "12345678-9";
    String FECHA = "2022/08/23";
    String FECHA_2 = "23-08-2022";

	@Test
	void obtenerJustificativoTest() {
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        JustificativosEntity justificativo = new JustificativosEntity();
        justificativo.setEmpleado(empleado);
        justificativoRepository.save(justificativo);

        List<JustificativosEntity> justificativos = justificativoService.obtenerJustificativosPorRut(empleado);
        assertEquals(RUT, justificativos.get(0).getEmpleado().getRut());

        justificativoRepository.delete(justificativos.get(0));
        empleadoRepository.delete(empleado);
    }

    @Test
    void guardarJustificativoTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        boolean resultado = justificativoService.guardarJustificativo(FECHA, RUT);
        assertEquals(true, resultado);

        justificativoRepository.delete(justificativoService.obtenerJustificativosPorRut(empleado).get(0));
        empleadoRepository.delete(empleado);
    }

    @Test
    void guardarJustificativoTest2(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleadoService.guardarEmpleado(empleado);

        boolean resultado = justificativoService.guardarJustificativo(FECHA_2, RUT);
        assertEquals(false, resultado);

        empleadoRepository.delete(empleado);
    }
}

@SpringBootTest
class SueldosTest{
    @Autowired
    SueldosService sueldosService;
    @Autowired
    SueldoRepository sueldosRepository;
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    JustificativoService justificativoService;
    @Autowired
    JustificativoRepository justificativoRepository;
    @Autowired
    HorasExtraService horasExtraService;
    @Autowired
    HorasExtraRepository horasExtraRepository;
    @Autowired
    MarcasRelojService marcasRelojService;
    @Autowired
    MarcasRelojRepository marcasRelojRepository;
    String FECHA = "2020/08/23";
    String RUT = "12345678-9";
    String HORA = "08:00";
    @Test
    void asignarSueldoFijoTest(){
        Integer sueldoFijo = sueldosService.asignarSueldoFijo("A");
        assertEquals(1700000, sueldoFijo);
    }
    @Test
    void asignarSueldoFijoTest2(){
        Integer sueldoFijo = sueldosService.asignarSueldoFijo("B");
        assertEquals(1200000, sueldoFijo);
    }
    @Test
    void asignarSueldoFijoTest3(){
        Integer sueldoFijo = sueldosService.asignarSueldoFijo("C");
        assertEquals(800000, sueldoFijo);
    }
    @Test
    void asignarSueldoFijoTest4(){
        Integer sueldoFijo = sueldosService.asignarSueldoFijo("D");
        assertEquals(0, sueldoFijo);
    }
    @Test
    void asignarBonoAniosServicioTest(){
        double bonoAniosServicio = sueldosService.calcularBonoAniosServicio(4);
        assertEquals(0, bonoAniosServicio);
    }
    @Test
    void asignarBonoAniosServicioTest2(){
        double bonoAniosServicio = sueldosService.calcularBonoAniosServicio(8);
        assertEquals(0.05, bonoAniosServicio);
    }
    @Test
    void asignarBonoAniosServicioTest3(){
        double bonoAniosServicio = sueldosService.calcularBonoAniosServicio(12);
        assertEquals(0.08, bonoAniosServicio);
    }
    @Test
    void asignarBonoAniosServicioTest4(){
        double bonoAniosServicio = sueldosService.calcularBonoAniosServicio(16);
        assertEquals(0.11, bonoAniosServicio);
    }
    @Test
    void asignarBonoAniosServicioTest5(){
        double bonoAniosServicio = sueldosService.calcularBonoAniosServicio(22);
        assertEquals(0.14, bonoAniosServicio);
    }
    @Test
    void asignarBonoAniosServicioTest6(){
        double bonoAniosServicio = sueldosService.calcularBonoAniosServicio(27);
        assertEquals(0.17, bonoAniosServicio);
    }
    @Test
    void redondearTest(){
        double bonoAniosServicio = sueldosService.redondear(0.123456789);
        assertEquals(0.12, bonoAniosServicio);
    }
    @Test
    void calcularAniosServicioTest(){
        Integer aniosServicio = sueldosService.calcularAniosServicio(FECHA);
        assertEquals(2, aniosServicio);
    }
    @Test
    void calculaDescuentosTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        for(int i=0; i<20; i++){
            if(i < 10){
                marcasRelojService.crearMarcaReloj("2022/08/0" + i , HORA, RUT);
            }else{
                marcasRelojService.crearMarcaReloj("2022/08/" + i , HORA, RUT);
            }
        }
        double descuentos = sueldosService.calculaDescuentos(empleado);
        assertEquals(0.0, descuentos);

        List<MarcasRelojEntity> marcas = marcasRelojService.obtenerMarcasRelojPorEmpleado(empleado);
        for(int i=0; i<marcas.size(); i++){
            marcasRelojRepository.delete(marcas.get(i));
        }
        empleadoRepository.delete(empleado);
    }
    @Test
    void calculaMontoHorasExtraTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        double montoHorasExtra = sueldosService.montoHorasExtras(empleado);
        assertEquals(25000, montoHorasExtra);
        empleadoRepository.delete(empleado);
    }
    @Test
    void calculaMontoHorasExtra2Test(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setCategoria("B");
        empleadoService.guardarEmpleado(empleado);
        double montoHorasExtra = sueldosService.montoHorasExtras(empleado);
        assertEquals(20000, montoHorasExtra);
        empleadoRepository.delete(empleado);
    }
    @Test
    void calculaMontoHorasExtra3Test(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setCategoria("C");
        empleadoService.guardarEmpleado(empleado);
        double montoHorasExtra = sueldosService.montoHorasExtras(empleado);
        assertEquals(10000, montoHorasExtra);
        empleadoRepository.delete(empleado);
    }
    @Test
    void calculaMontoHorasExtra4Test(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setCategoria("D");
        empleadoService.guardarEmpleado(empleado);
        double montoHorasExtra = sueldosService.montoHorasExtras(empleado);
        assertEquals(0.0, montoHorasExtra);
        empleadoRepository.delete(empleado);
    }
    @Test
    void totalHorasExtraTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        HorasExtraEntity horas = new HorasExtraEntity();
        horas.setEmpleado(empleado);
        horas.setHoras(10);
        horasExtraRepository.save(horas);
        Integer totalHorasExtra = sueldosService.totalHorasExtras(empleado);
        assertEquals(10, totalHorasExtra);
        horasExtraRepository.delete(horas);
        empleadoRepository.delete(empleado);
    }
    @Test
    void montoHorasExtraTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        HorasExtraEntity horas = new HorasExtraEntity();
        horas.setEmpleado(empleado);
        horas.setHoras(10);
        horasExtraRepository.save(horas);
        double montoHorasExtra = sueldosService.calculaMontoHorasExtras(empleado);
        assertEquals(250000, montoHorasExtra);
        horasExtraRepository.delete(horas);
        empleadoRepository.delete(empleado);
    }
    @Test
    void crearSueldoTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setNombres("Sebastián Ignacio");
        empleado.setApellidos("Correa Chávez");
        empleado.setFechaIngreso("2020/08/01");
        empleado.getFechaNacimiento();
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        SueldosEntity sueldo = sueldosService.crearSueldo(empleado);
        assertEquals(RUT,sueldo.getRut());
        sueldosRepository.delete(sueldo);
        empleadoRepository.delete(empleado);
    }
    @Test
    void obtenerSueldoTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setNombres("Sebastián Ignacio");
        empleado.setApellidos("Correa Chávez");
        empleado.setFechaIngreso("2020/08/01");
        empleado.getFechaNacimiento();
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        SueldosEntity sueldo = sueldosService.crearSueldo(empleado);
        List<SueldosEntity> sueldos = sueldosService.obtenerSueldos();
        assertThat(sueldos).isNotNull();
        sueldosRepository.delete(sueldo);
        empleadoRepository.delete(empleado);
    }
    @Test
    void calcularSueldosTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setNombres("Sebastián Ignacio");
        empleado.setApellidos("Correa Chávez");
        empleado.setFechaIngreso("2020/08/01");
        empleado.getFechaNacimiento();
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        boolean resultado = sueldosService.calcularSueldos();
        assertEquals(true, resultado);

        List<SueldosEntity> sueldos = sueldosRepository.findByRut(RUT);
        for(int i = 0; i < sueldos.size(); i++){
            sueldosRepository.delete(sueldos.get(i));
        }
        empleadoRepository.delete(empleado);

    }

    @Test
    void obtenerSueldosTest(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut(RUT);
        empleado.setNombres("Sebastián Ignacio");
        empleado.setApellidos("Correa Chávez");
        empleado.setFechaIngreso("2020/08/01");
        empleado.getFechaNacimiento();
        empleado.setCategoria("A");
        empleadoService.guardarEmpleado(empleado);
        List<SueldosEntity> sueldos = sueldosService.obtenerSueldos();
        assertThat(sueldos).isNotNull();

        List<SueldosEntity> sueldosR = sueldosRepository.findByRut(RUT);
        for(int i = 0; i < sueldosR.size(); i++){
            sueldosRepository.delete(sueldosR.get(i));
        }
        empleadoRepository.delete(empleado);

    }
}




