package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.HorasExtraEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;
import tingeso.mueblesstgo.repositories.HorasExtraRepository;

import java.util.List;

@Service
public class HorasExtraService {
    @Autowired
    HorasExtraRepository horasExtraRepository;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    ValidadorService validadorService;
    @Autowired
    MarcasRelojService marcasRelojService;

    private static final Integer HORA_SALIDA = 18;

    // Descripción: Guarda la autorización de las horas extras ingresadas, para esto verifica que el rut exista en la base de datos, la cantidad de
    // horas mayor a 0 y la fecha sea válida
    // Entradas: Integer para la cantidad de horas, String para el rut del empleado y un String para la fecha en el formato AAAA/MM/DD
    // Salidas: booleano que indica si se guardó o no laS horas extra
    public boolean guardarHorasExtra(Integer horas, String rut, String fecha) {
        if(validadorService.validarRut(rut) && validadorService.validarHoras(horas) && validadorService.validarFecha(fecha)) {
            EmpleadoEntity empleado = empleadoService.obtenerPorRut(rut);
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

    // Descripción: Verifica que las horas extra ingresadas coincidan con las realizadas por el empleado
    // Entradas: Integer para la cantidad de horas, EmpleadoEntity para el empleado y un String para la fecha en el formato AAAA/MM/DD
    // Salidas: booleano que indica si las horas extra coinciden o no
    public boolean verificaHorasExtra(Integer horas, EmpleadoEntity empleado, String fecha){
        MarcasRelojEntity marcaReloj = marcasRelojService.obtenerMarcaRelojPorFechaYEmpleado(fecha, empleado);
        String[] horaSalida = marcaReloj.getHoraSalida().split(":");
        Integer horasReal = Integer.parseInt(horaSalida[0]) - HORA_SALIDA;
        return horasReal > 0 && horasReal.equals(horas);
    }

    // Descripción: Crea un registro de horas extra para el empleado
    // Entradas: Integer para la cantidad de horas, EmpleadoEntity para el empleado y un String para el mes
    // Salidas: void
    public void crearHora(Integer horas, EmpleadoEntity empleado, String mes) {
        HorasExtraEntity horasExtra = new HorasExtraEntity();
        horasExtra.setMes(mes);
        horasExtra.setHoras(horas);
        horasExtra.setEmpleado(empleado);
        horasExtraRepository.save(horasExtra);
    }

    // Descripción: Obtiene todas las horas extra de la base de datos
    // Entradas: void
    // Salidas: Lista de HorasExtraEntity
    public List<HorasExtraEntity> obtenerHorasExtraPorRut(EmpleadoEntity empleado){
        return horasExtraRepository.findByRut(empleado.getRut());
    }
}
