package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.*;
import tingeso.mueblesstgo.repositories.SueldoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SueldosService {
    private static final double DESCUENTO_PREVISIONAL = 0.1;
    private static final double DESCUENTO_SALUD = 0.08;
    private static final Integer HORAS_A = 25000;
    private static final Integer HORAS_B = 20000;
    private static final Integer HORAS_C = 10000;
    // Considera 20 días hábiles de trabajo por mes
    private static final Integer DIAS_MES = 20;
    private static final double DESCUENTO_INASISTENCIA = 0.15;
    private static final Integer ANIO_ACTUAL = 2022;
    private static final double BONIFICACION_1 = 0.05;
    private static final double BONIFICACION_2 = 0.08;
    private static final double BONIFICACION_3 = 0.11;
    private static final double BONIFICACION_4 = 0.14;
    private static final double BONIFICACION_5 = 0.17;
    private static final Integer SUELDO_A = 1700000;
    private static final Integer SUELDO_B = 1200000;
    private static final Integer SUELDO_C = 800000;

    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    MarcasRelojService marcasRelojService;
    @Autowired
    JustificativoService justificativoService;
    @Autowired
    HorasExtraService horasExtraService;
    @Autowired
    SueldoRepository sueldoRepository;

    // Descripción: Obtiene todos los cálculos de los sueldos
    // Entrada: void
    // Salida: List con todos los sueldos
    public List<SueldosEntity> obtenerSueldos(){ return sueldoRepository.findAll(); }

    // Descripción: Calcula el sueldo final de todos los empleados, considerando las horas extra realizadas, bonificaciones por años de servicio,
    // descuentos por atrasos e inasistencias, y descuentos de cotizaciones previsionales y de salud.
    // Entrada: void
    // Salida: booleano que indica si el cálculo fue exitoso o no
    public boolean calcularSueldos(){
        List<EmpleadoEntity> empleados = empleadoService.obtenerEmpleados();
        for (int i = 0; i<empleados.size(); i++) {
            EmpleadoEntity empleado = empleados.get(i);
            SueldosEntity sueldo = crearSueldo(empleado);
            double descuentoAtrasos = calculaDescuentos(empleado);
            sueldo.setMontoDescuentos(redondear(descuentoAtrasos));
            double horasExtra = calculaMontoHorasExtras(empleado);
            sueldo.setMontoHorasExtra(redondear(horasExtra));
            double sueldoBruto = sueldo.getSueldoFijo() + sueldo.getMontoHorasExtra() + sueldo.getMontoBonificacion() - sueldo.getMontoDescuentos();
            sueldo.setSueldoBruto(redondear(sueldoBruto));
            sueldo.setCotizacionPrevisional(redondear(sueldoBruto * DESCUENTO_PREVISIONAL));
            sueldo.setCotizacionSalud(redondear(sueldoBruto * DESCUENTO_SALUD));
            sueldo.setSueldoFinal(redondear(sueldoBruto - sueldo.getCotizacionPrevisional() - sueldo.getCotizacionSalud()));
            sueldoRepository.save(sueldo);
        }
        return true;

    }

    // Descripción: Crea un sueldo para un empleado, setea los datos del empleado, calcula el sueldo fijo, los años de servicio y la bonificación correspondiente
    // Entrada: EmpleadoEntity con el empleado
    // Salida: SueldosEntity con el sueldo creado
    public SueldosEntity crearSueldo(EmpleadoEntity empleado) {
        SueldosEntity sueldo = new SueldosEntity();
        sueldo.setEmpleado(empleado);
        sueldo.setRut(empleado.getRut());
        sueldo.setNombres(empleado.getNombres());
        sueldo.setApellidos(empleado.getApellidos());
        sueldo.setCategoria(empleado.getCategoria());
        Integer aniosS = calcularAniosServicio(empleado.getFechaIngreso());
        sueldo.setAniosServicio(aniosS);
        sueldo.setSueldoFijo(asignarSueldoFijo(empleado.getCategoria()));
        sueldo.setMontoBonificacion(redondear(calcularBonoAniosServicio(aniosS) * sueldo.getSueldoFijo()));
        return sueldo;
    }

    // Descripción: Calcula el monto final de las horas extra realizadas por un empleado
    // Entrada: EmpleadoEntity con el empleado
    // Salida: double con el monto final de las horas extra
    public double calculaMontoHorasExtras(EmpleadoEntity empleado){
        Integer horas = totalHorasExtras(empleado);
        double montoPorHora = montoHorasExtras(empleado);
        return horas * montoPorHora;
    }

    // Descripción: Calcula el total de horas extra realizadas por un empleado
    // Entrada: EmpleadoEntity con el empleado
    // Salida: Integer con el total de horas extra
    public Integer totalHorasExtras(EmpleadoEntity empleado){
        List<HorasExtraEntity> horasExtras = horasExtraService.obtenerHorasExtraPorRut(empleado);
        Integer totalHoras = 0;
        for (int i = 0; i<horasExtras.size(); i++) {
            HorasExtraEntity horasExtra = horasExtras.get(i);
            totalHoras += horasExtra.getHoras();
        }
        return totalHoras;
    }

    // Descripción: Calcula el monto por hora extra de un empleado según su categoría
    // Entrada: EmpleadoEntity con el empleado
    // Salida: double con el monto por hora extra
    public double montoHorasExtras(EmpleadoEntity empleado){
        String categoria = empleado.getCategoria();
        switch (categoria){
            case "A" -> { return HORAS_A; }
            case "B" -> { return HORAS_B; }
            case "C" -> { return HORAS_C; }
            default -> { return 0; }
        }
    }

    // Descripción: Calcula el monto de descuentos por atrasos e inasistencias de un empleado
    // Entrada: EmpleadoEntity con el empleado
    // Salida: double con el monto de descuentos
    public double calculaDescuentos(EmpleadoEntity empleado){
        Integer sueldoFijo = asignarSueldoFijo(empleado.getCategoria());
        List<MarcasRelojEntity> marcas = marcasRelojService.obtenerMarcasRelojPorEmpleado(empleado);
        List<JustificativosEntity> justificativos = justificativoService.obtenerJustificativosPorRut(empleado);
        Integer inasistencias = DIAS_MES - marcas.size() - justificativos.size();
        double porcentajeDescuento = empleado.getDescuentoAtraso()/100.0;
        return porcentajeDescuento*sueldoFijo + (inasistencias*DESCUENTO_INASISTENCIA*sueldoFijo);
    }

    // Descripción: Calcula los años de servicio de un empleado
    // Entrada: String con la fecha de ingreso del empleado en formato AAAA/MM/DD
    // Salida: Integer con los años de servicio
    public Integer calcularAniosServicio(String fecha){
        String anioI = fecha.split("/")[0];
        return ANIO_ACTUAL - Integer.parseInt(anioI);
    }

    // Descripción: Calcula la bonificación por años de servicio de un empleado
    // Entrada: Integer con los años de servicio
    // Salida: double con el porcentaje de bonificación del sueldo fijo
    public double calcularBonoAniosServicio(Integer anios){
        if(anios < 5){
            return 0;
        } else if(anios < 10){
            return BONIFICACION_1;
        } else if(anios < 15){
            return BONIFICACION_2;
        } else if(anios < 20){
            return BONIFICACION_3;
        } else if(anios < 25){
            return BONIFICACION_4;
        } else {
            return BONIFICACION_5;
        }
    }

    // Descripción: Asigna el sueldo fijo de un empleado según su categoría
    // Entrada: String con la categoría del empleado
    // Salida: Integer con el sueldo fijo
    public Integer asignarSueldoFijo(String categoria) {
        return switch (categoria) {
            case "A" -> SUELDO_A;
            case "B" -> SUELDO_B;
            case "C" -> SUELDO_C;
            default -> 0;
        };
    }

    // Descripción: Redondea un número a dos decimales
    // Entrada: double con el número a redondear
    // Salida: double con el número redondeado
    private double redondear(double numero){
        BigDecimal bd = new BigDecimal(numero).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
