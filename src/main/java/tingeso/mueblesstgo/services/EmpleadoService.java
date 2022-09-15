package tingeso.mueblesstgo.services;

import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    EmpleadoRepository empleadoRepository;

    public ArrayList<EmpleadoEntity> obtenerEmpleados(){ return (ArrayList<EmpleadoEntity>) empleadoRepository.findAll(); }

    public EmpleadoEntity guardarEmpleado(EmpleadoEntity empleado){
        return empleadoRepository.save(empleado);
    }

    public Optional<EmpleadoEntity> obtenerPorId(Long id){
        return empleadoRepository.findById(id);
    }

    public EmpleadoEntity obtenerPorRut(String rut){
        return empleadoRepository.findByRut(rut);
    }


}
