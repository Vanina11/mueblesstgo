package tingeso.mueblesstgo.services;

import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {
    @Autowired
    EmpleadoRepository empleadoRepository;

    public List<EmpleadoEntity> obtenerEmpleados(){ return empleadoRepository.findAll(); }

    public void guardarEmpleado(EmpleadoEntity empleado){
        empleadoRepository.save(empleado);
    }

    public EmpleadoEntity obtenerPorRut(String rut){
        return empleadoRepository.findByRut(rut);
    }

    public void incrementaDescuentoAtraso(EmpleadoEntity empleado, Integer descuento){
        empleado.setDescuentoAtraso(empleado.getDescuentoAtraso() + descuento);
        empleadoRepository.save(empleado);
    }

    public void incrementaInasistencias(EmpleadoEntity empleado){
        empleado.setInasistencias(empleado.getInasistencias() + 1);
        empleadoRepository.save(empleado);
    }

}
