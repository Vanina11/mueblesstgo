package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.HorasExtraEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import tingeso.mueblesstgo.repositories.HorasExtraRepository;
import tingeso.mueblesstgo.repositories.JustificativoRepository;

@Service
public class HorasExtraService {
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    HorasExtraRepository horasExtraRepository;
    public void guardarHorasExtra(Integer horas, String rut) {
        HorasExtraEntity horasExtra = new HorasExtraEntity();
        horasExtra.setHoras(horas);
        EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
        horasExtra.setEmpleado(empleado);
        horasExtraRepository.save(horasExtra);
    }
}
