package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import tingeso.mueblesstgo.entities.*;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.repositories.SueldoRepository;

import java.util.List;

@Service
public class OficinaRRHHService {
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
    // Salida: ArrayList con todos los sueldos
    public List<SueldosEntity> obtenerSueldos(){ return (List<SueldosEntity>) sueldoRepository.findAll(); }

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
            sueldo.setMontoDescuentos(descuentoAtrasos);
            double horasExtra = calculaMontoHorasExtras(empleado);
            sueldo.setMontoHorasExtra(horasExtra);
            double sueldoBruto = sueldo.getSueldoFijo() + sueldo.getMontoHorasExtra() + sueldo.getMontoBonificacion() - sueldo.getMontoDescuentos();
            sueldo.setSueldoBruto(sueldoBruto);
            sueldo.setCotizacionPrevisional(sueldoBruto * 0.1);
            sueldo.setCotizacionSalud(sueldoBruto * 0.08);
            sueldo.setSueldoFinal(sueldoBruto - sueldo.getCotizacionPrevisional() - sueldo.getCotizacionSalud());
            sueldoRepository.save(sueldo);
        }
        return true;

    }

    // Descripción: Crea un sueldo para un empleado, setea los datos del empleado, calcula el sueldo fijo, los años de servicio y la bonificación correspondiente
    // Entrada: EmpleadoEntity con el empleado
    // Salida: SueldosEntity con el sueldo creado
    private SueldosEntity crearSueldo(EmpleadoEntity empleado) {
        SueldosEntity sueldo = new SueldosEntity();
        sueldo.setEmpleado(empleado);
        sueldo.setRut(empleado.getRut());
        sueldo.setNombres(empleado.getNombres());
        sueldo.setApellidos(empleado.getApellidos());
        sueldo.setCategoria(empleado.getCategoria());
        Integer aniosS = calcularAniosServicio(empleado.getFechaIngreso());
        sueldo.setAniosServicio(aniosS);
        sueldo.setSueldoFijo(asignarSueldoFijo(empleado.getCategoria()));
        sueldo.setMontoBonificacion(calcularBonoAniosServicio(aniosS) * sueldo.getSueldoFijo());
        return sueldo;
    }

    private double calculaMontoHorasExtras(EmpleadoEntity empleado){
        Integer horas = totalHorasExtras(empleado);
        double montoPorHora = montoHorasExtras(empleado);
        return horas * montoPorHora;
    }

    public Integer totalHorasExtras(EmpleadoEntity empleado){
        List<HorasExtraEntity> horasExtras = horasExtraService.obtenerHorasExtraPorRut(empleado);
        Integer totalHoras = 0;
        for (int i = 0; i<horasExtras.size(); i++) {
            HorasExtraEntity horasExtra = horasExtras.get(i);
            totalHoras += horasExtra.getHoras();
        }
        return totalHoras;
    }

    public double montoHorasExtras(EmpleadoEntity empleado){
        String categoria = empleado.getCategoria();
        switch (categoria){
            case "A" -> { return 25000; }
            case "B" -> { return 20000; }
            case "C" -> { return 10000; }
            default -> { return 0; }
        }
    }

    private double calculaDescuentos(EmpleadoEntity empleado){
        Integer sueldoFijo = asignarSueldoFijo(empleado.getCategoria());
        // Asume que el mes tiene 30 días
        List<MarcasRelojEntity> marcas = marcasRelojService.obtenerMarcasRelojPorEmpleado(empleado);
        // revisar justificativos
        List<JustificativosEntity> justificativos = justificativoService.obtenerJustificativosPorRut(empleado);
        Integer inasistencias = 30 - marcas.size() - justificativos.size();
        //revisar
        double porcentajeDescuento = empleado.getDescuentoAtraso()/100.0;
        return porcentajeDescuento*sueldoFijo + (inasistencias*0.15*sueldoFijo);
    }



    private Integer calcularAniosServicio(String fecha){
        String anioI = fecha.split("/")[0];
        return 2022 - Integer.parseInt(anioI);
    }

    public double calcularBonoAniosServicio(Integer anios){
        if(anios < 5){
            return 0;
        } else if(anios < 10){
            return 0.05;
        } else if(anios < 15){
            return 0.08;
        } else if(anios < 20){
            return 0.11;
        } else if(anios < 25){
            return 0.14;
        } else {
            return 0.17;
        }
    }

    public Integer asignarSueldoFijo(String categoria) {
        return switch (categoria) {
            case "A" -> 1700000;
            case "B" -> 1200000;
            case "C" -> 800000;
            default -> 0;
        };
    }
}
