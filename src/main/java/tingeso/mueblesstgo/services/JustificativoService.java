package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.JustificativosEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import tingeso.mueblesstgo.repositories.JustificativoRepository;

import java.util.ArrayList;

@Service
public class JustificativoService {
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    JustificativoRepository justificativoRepository;
    @Autowired
    ValidadorService validadorService;
    public boolean guardarJustificativo(String fecha, String rut) {
        // Verifica que el rut exista en la base de datos y que la fecha sea válida
        if (validadorService.validarRut(rut) && validadorService.validarFecha(fecha)) {
            JustificativosEntity justificativo = new JustificativosEntity();
            justificativo.setFecha(fecha);
            EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
            justificativo.setEmpleado(empleado);
            justificativoRepository.save(justificativo);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<JustificativosEntity> obtenerJustificativosPorRut(EmpleadoEntity empleado){
        return justificativoRepository.findByRut(empleado.getRut());
    }


}
