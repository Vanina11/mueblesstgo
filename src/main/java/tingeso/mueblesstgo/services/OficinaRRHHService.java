package tingeso.mueblesstgo.services;

import tingeso.mueblesstgo.entities.EmpleadoEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class OficinaRRHHService {
    public void asignarSueldoFijo(EmpleadoEntity empleado){
        switch (empleado.getCategoria()) {
            case "A" -> empleado.setSueldoFijo(1700000);
            case "B" -> empleado.setSueldoFijo(1200000);
            case "C" -> empleado.setSueldoFijo(800000);
        }
    }

    public void calcularAniosServicio(EmpleadoEntity empleado){
        int anioIngreso = empleado.getFechaIngreso().get(Calendar.YEAR);
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        int aniosServicio = anioActual - anioIngreso;
        empleado.setAniosServicio(aniosServicio);
    }

    public double calcularBonoAniosServicio(EmpleadoEntity empleado){
        int aniosServicio = empleado.getAniosServicio();
        if(aniosServicio < 5){
            return 0;
        } else if(aniosServicio < 10){
            return 0.05 * empleado.getSueldoFijo();
        } else if(aniosServicio < 15){
            return 0.08 * empleado.getSueldoFijo();
        } else if(aniosServicio < 20){
            return 0.11 * empleado.getSueldoFijo();
        } else if(aniosServicio < 25){
            return 0.14 * empleado.getSueldoFijo();
        } else {
            return 0.17 * empleado.getSueldoFijo();
        }
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

    public double montoDescuentoAtrasos(EmpleadoEntity empleado, Integer minutos){
        if(10 < minutos && minutos <= 25){
            return 0.01 * empleado.getSueldoFijo();
        } else if(25 < minutos && minutos <= 45){
            return 0.03 * empleado.getSueldoFijo();
        } else if(45 < minutos && minutos <= 70){
            return 0.06 * empleado.getSueldoFijo();
        } else {
            return 0.15 * empleado.getSueldoFijo();
        }
    }

}
