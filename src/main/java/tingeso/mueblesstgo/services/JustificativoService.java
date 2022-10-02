package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.JustificativosEntity;
import tingeso.mueblesstgo.repositories.JustificativoRepository;

import java.util.List;

@Service
public class JustificativoService {
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    JustificativoRepository justificativoRepository;
    @Autowired
    ValidadorService validadorService;

    // Descripción: Guarda un justificativo en la base de datos, pero antes verifica que el rut del empleado exista y que la fecha sea válida
    // Entradas: String para la fecha en el formato AAAA/MM/DD y String para el rut del empleado
    // Salidas: booleano que indica si el justificativo fue guardado o no
    public boolean guardarJustificativo(String fecha, String rut) {
        if (validadorService.validarRut(rut) && validadorService.validarFecha(fecha)) {
            JustificativosEntity justificativo = new JustificativosEntity();
            justificativo.setFecha(fecha);
            EmpleadoEntity empleado = empleadoService.obtenerPorRut(rut);
            justificativo.setEmpleado(empleado);
            justificativoRepository.save(justificativo);
            return true;
        } else {
            return false;
        }
    }

    // Descripción: Obtiene todos los justificativos de un empleado por su rut
    // Entradas: EmpleadoEntity para el empleado
    // Salidas: Lista de JustificativosEntity
    public List<JustificativosEntity> obtenerJustificativosPorRut(EmpleadoEntity empleado){
        return justificativoRepository.findByRut(empleado.getRut());
    }

    public void eliminarJustificativos(){
        justificativoRepository.deleteAll();
    }

}
