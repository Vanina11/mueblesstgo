package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.HorasExtraEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import tingeso.mueblesstgo.repositories.HorasExtraRepository;

@Service
public class HorasExtraService {
    @Autowired
    HorasExtraRepository horasExtraRepository;
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    ValidadorService validadorService;
    public boolean guardarHorasExtra(Integer horas, String rut, String fecha) {
        // Verifica que el rut ingresado sea correcto, cantidad de horas sea mayor a 0 y la fecha válida
        if(validadorService.validarRut(rut) && validadorService.validarHoras(horas) && validadorService.validarFecha(fecha)) {
            EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
            HorasExtraEntity horaExistente = horasExtraRepository.findByEmpleado(empleado);
            // Si ya se ingresó las horas extra para el empleado, se actualiza el registro y se suman las horas de ese mes
            if(horaExistente != null) {
                horaExistente.setHoras(horaExistente.getHoras() + horas);
                horasExtraRepository.save(horaExistente);
            }else{
                HorasExtraEntity horasExtra = crearHora(horas, empleado);
                horasExtraRepository.save(horasExtra);
            }
            return true;
        } else {
            // Ocurrió un error con los parámetros ingresados
            return false;
        }
    }


    private HorasExtraEntity crearHora(Integer horas, EmpleadoEntity empleado) {
        HorasExtraEntity horasExtra = new HorasExtraEntity();
        horasExtra.setHoras(horas);
        horasExtra.setEmpleado(empleado);
        return horasExtra;
    }
}
