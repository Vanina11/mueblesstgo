package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.HorasExtraEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import tingeso.mueblesstgo.repositories.HorasExtraRepository;
import tingeso.mueblesstgo.repositories.MarcasRelojRepository;

@Service
public class HorasExtraService {
    @Autowired
    HorasExtraRepository horasExtraRepository;
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    ValidadorService validadorService;
    @Autowired
    MarcasRelojRepository marcasRelojRepository;
    public boolean guardarHorasExtra(Integer horas, String rut, String fecha) {
        // Verifica que el rut ingresado sea correcto, cantidad de horas sea mayor a 0 y la fecha válida
        if(validadorService.validarRut(rut) && validadorService.validarHoras(horas) && validadorService.validarFecha(fecha)) {
            EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
            String mes = fecha.split("/")[1];
            // Las horas extra ingresadas coinciden con las realizadas
            if(verificaHorasExtra(horas, empleado, fecha)){
                HorasExtraEntity horaExistente = horasExtraRepository.findByEmpleadoAndMes(empleado, mes);
                // Si ya se ingresó las horas extra para el empleado, se actualiza el registro y se suman las horas de ese mes
                if(horaExistente != null) {
                    horaExistente.setHoras(horaExistente.getHoras() + horas);
                }else{
                    crearHora(horas, empleado, mes);
                }
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }

    }

    private boolean verificaHorasExtra(Integer horas, EmpleadoEntity empleado, String fecha){
        MarcasRelojEntity marcaReloj = marcasRelojRepository.findByFechaAndEmpleado(fecha, empleado);
        String horaSalida[] = marcaReloj.getHoraSalida().split(":");
        Integer horasReal = Integer.parseInt(horaSalida[0]) - 18;
        return horasReal > 0 && horasReal.equals(horas);
    }

    private void crearHora(Integer horas, EmpleadoEntity empleado, String mes) {
        HorasExtraEntity horasExtra = new HorasExtraEntity();
        horasExtra.setMes(mes);
        horasExtra.setHoras(horas);
        horasExtra.setEmpleado(empleado);
        horasExtraRepository.save(horasExtra);
    }
}
