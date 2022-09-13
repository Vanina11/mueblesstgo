package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.JustificativosEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import tingeso.mueblesstgo.repositories.JustificativoRepository;

@Service
public class JustificativoService {
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    JustificativoRepository justificativoRepository;
    public void guardarJustificativo(String fecha, String rut) {
        JustificativosEntity justificativo = new JustificativosEntity();
        justificativo.setFecha(fecha);
        EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
        justificativo.setEmpleado(empleado);
        justificativoRepository.save(justificativo);
    }
}
