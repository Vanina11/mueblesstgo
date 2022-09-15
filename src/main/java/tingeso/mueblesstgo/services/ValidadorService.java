package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;

@Service
public class ValidadorService {
    @Autowired
    EmpleadoRepository empleadoRepository;
    public boolean validarHoras(Integer hora){
        return hora > 0;
    }

    public boolean validarRut(String rut) {
        EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
        return empleado != null;
    }

    public boolean validarFecha(String fecha) {
        String fechaS[] = fecha.split("/");
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
