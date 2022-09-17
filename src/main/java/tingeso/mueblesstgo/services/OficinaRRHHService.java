package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import tingeso.mueblesstgo.entities.*;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.repositories.SueldoRepository;

import java.util.ArrayList;

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

    public void calcularSueldos(){
        ArrayList<EmpleadoEntity> empleados = empleadoService.obtenerEmpleados();

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

    }


    private double calculaMontoHorasExtras(EmpleadoEntity empleado){
        Integer horas = totalHorasExtras(empleado);
        double montoPorHora = montoHorasExtras(empleado);
        return horas * montoPorHora;
    }

    public Integer totalHorasExtras(EmpleadoEntity empleado){
        ArrayList<HorasExtraEntity> horasExtras = horasExtraService.obtenerHorasExtraPorRut(empleado);
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
        }
        return 0;
    }

    private double calculaDescuentos(EmpleadoEntity empleado){
        Integer sueldoFijo = asignarSueldoFijo(empleado.getCategoria());
        System.out.println("EMPLEADO: " + empleado.getNombres());
        // Asume que el mes tiene 30 días
        ArrayList<MarcasRelojEntity> marcas = marcasRelojService.obtenerMarcasRelojPorEmpleado(empleado);
        System.out.println("TAMAÑO DE MARCAS: " + marcas.size());
        // revisar justificativos
        ArrayList<JustificativosEntity> justificativos = justificativoService.obtenerJustificativosPorRut(empleado);
        System.out.println("TAMAÑO DE JUSTIFICATIVOS: " + justificativos.size());
        Integer inasistencias = 30 - marcas.size() - justificativos.size();
        System.out.println("INASISTENCIAS: " + inasistencias);
        //revisar
        double porcentajeDescuento = empleado.getDescuentoAtraso()/100.0;
        double descuentoAtrasos = porcentajeDescuento*sueldoFijo + (inasistencias*0.15*sueldoFijo);
        System.out.println("SUELDO FIJO:" + sueldoFijo);
        System.out.println(inasistencias*0.15*sueldoFijo);
        System.out.println("DESCUENTO ATRASOS: " + descuentoAtrasos);
        return descuentoAtrasos;
    }

    private SueldosEntity crearSueldo(EmpleadoEntity empleados) {
        SueldosEntity sueldo = new SueldosEntity();
        sueldo.setEmpleado(empleados);
        sueldo.setRut(empleados.getRut());
        sueldo.setNombres(empleados.getNombres());
        sueldo.setApellidos(empleados.getApellidos());
        sueldo.setCategoria(empleados.getCategoria());
        sueldo.setAniosServicio(calcularAniosServicio(empleados.getFechaIngreso()));
        sueldo.setSueldoFijo(asignarSueldoFijo(empleados.getCategoria()));
        sueldo.setMontoBonificacion(calcularBonoAniosServicio(sueldo.getAniosServicio()) * sueldo.getSueldoFijo());
        return sueldo;
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
