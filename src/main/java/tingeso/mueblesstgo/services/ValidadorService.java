package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;

@Service
public class ValidadorService {
    @Autowired
    EmpleadoService empleadoService;

    // Descripción: Valida la cantidad de horas ingresada sea un número válido
    // Entradas: Integer para la cantidad de horas
    // Salidas: booleano que indica si la cantidad de horas es válida o no
    public boolean validarHoras(Integer hora){
        return hora > 0;
    }

    // Descripción: Valida que el rut ingresado sea un rut válido si existe en la base de datos
    // Entradas: String para el rut
    // Salidas: booleano que indica si el rut es válido o no
    public boolean validarRut(String rut) {
        EmpleadoEntity empleado = empleadoService.obtenerPorRut(rut);
        return empleado != null;
    }

    // Descripción: Valida que la fecha ingresada sea una fecha válida
    // Entradas: String para la fecha en el formato AAAA/MM/DD
    // Salidas: booleano que indica si la fecha es válida o no
    public boolean validarFecha(String fecha) {
        String[] fechaS = fecha.split("/");
        if(fechaS.length == 3){
            int dia = Integer.parseInt(fechaS[2]);
            int mes = Integer.parseInt(fechaS[1]);
            int anio = Integer.parseInt(fechaS[0]);
            return dia > 0 && dia < 32 && mes > 0 && mes < 13 && anio > 0;
        } else {
            return false;
        }
    }

}
