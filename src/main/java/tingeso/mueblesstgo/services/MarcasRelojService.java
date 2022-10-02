package tingeso.mueblesstgo.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;
import tingeso.mueblesstgo.repositories.MarcasRelojRepository;

@Service
public class MarcasRelojService {
    private static final Integer HORA_LLEGADA = 8;
    private static final Integer MINUTO_LLEGADA = 10;
    private static final Integer DESCUENTO_1 = 1;
    private static final Integer DESCUENTO_2 = 3;
    private static final Integer DESCUENTO_3 = 6;
    private static final Integer DESCUENTO_4 = 15;

    // Indica la ubicación del archivo de texto que contiene las marcas de reloj
    private String directorio="cargas" + File.pathSeparator;
    private final Logger logg = LoggerFactory.getLogger(MarcasRelojService.class);

    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    MarcasRelojRepository marcasRelojRepository;


    // Descripción: Permite importar el archivo de marcas de reloj, para esto verifica que no esté vacío y que la extensiòn sea .txt
    // Entrada: Multifile para el archivo de texto con las marcas de reloj
    // Salida: booleano que indica si el archivo fue importado o no
    public boolean guardarMarcasReloj(MultipartFile file) {
        if (!file.isEmpty() && Objects.requireNonNull(file.getOriginalFilename()).endsWith(".txt")) {
            try {
                byte [] bytes= file.getBytes();
                Path path = Paths.get( directorio + file.getOriginalFilename() );
                Files.write(path, bytes);
                leerMarcasReloj(path);
                return true;

            } catch (IOException e) {
                logg.error("ERROR", e);
                return false;
            }
        }else{
            return false;
        }
    }

    // Descripción: Lee el contenido del archivo de las marcas de reloj
    // Entrada: Path con la ruta del archivo de texto
    // Salida: void
    public void leerMarcasReloj(Path path) throws IOException {
        try(BufferedReader br = new BufferedReader(new java.io.FileReader(path.toString()))){
            String linea = br.readLine();
            // Hasta que no se llegue al final del archivo
            while (linea != null){
                leerLinea(linea);
                linea = br.readLine();
            }
        } catch (IOException f){
            logg.error("ERROR", f);
        }
    }

    // Descipción: Lee una linea del archivo y analiza la información, separa en fecha, hora y rut del empleado. Además verifica que el rut exista
    // en la base de datos, en caso contrario no se guarda la marca de reloj.
    // Entrada: String para la línea
    // Salida: void
    public void leerLinea(String linea){
        String[] datos = linea.split(";");
        String fecha = datos[0];
        String hora = datos[1];
        String rut = datos[2];
        // Si el empleado existe, se crea la marca de reloj
        EmpleadoEntity empleado = empleadoService.obtenerPorRut(rut);
        MarcasRelojEntity marcas = marcasRelojRepository.findByFechaAndEmpleado(fecha, empleado);
        if(empleado != null){
            if(marcas == null) {
                crearMarcaReloj(fecha, hora, rut);
            } else {
                marcas.setHoraSalida(hora);
                marcasRelojRepository.save(marcas);
            }
        }
    }

    // Descripción: Crea la marca de reloj
    // Entrada: String para la fecha en el formato AAAA/MM/DD, String para la hora en el formato HH:MM y String con el rut
    // Salida: void
    public void crearMarcaReloj(String fecha, String hora, String rut) {
        EmpleadoEntity empleado = empleadoService.obtenerPorRut(rut);
        Integer descuento = calcularDescuentoAtraso(hora);
        MarcasRelojEntity marcaReloj = new MarcasRelojEntity();
        marcaReloj.setFecha(fecha);
        marcaReloj.setHora(hora);
        marcaReloj.setEmpleado(empleado);
        if(!Objects.equals(descuento, DESCUENTO_4)){
            empleadoService.incrementaDescuentoAtraso(empleado, descuento);
        }else {
            empleadoService.incrementaInasistencias(empleado);
        }
        marcasRelojRepository.save(marcaReloj);
    }

    // Descripción: Calcula el descuento por atraso
    // Entrada: String con la hora en el formato HH:MM
    // Salida: Integer con el descuento
    public Integer calcularDescuentoAtraso(String hora) {
        String[] horaMinuto = hora.split(":");
        Integer horaInt = Integer.parseInt(horaMinuto[0]);
        Integer minutoInt = Integer.parseInt(horaMinuto[1]);
        if(horaInt <= HORA_LLEGADA && minutoInt <= MINUTO_LLEGADA){
            return 0;
        } else {
            Integer minutosAtraso = (horaInt - HORA_LLEGADA) * 60 + minutoInt - MINUTO_LLEGADA;
            return montoDescuentoAtrasos(minutosAtraso);
        }
    }

    // Descripción: Calcula el monto del descuento por minutos de atraso
    // Entrada: Integer con los minutos de atraso
    // Salida: Integer con el descuento
    public Integer montoDescuentoAtrasos(Integer minutos){
        if(10 < minutos && minutos <= 25){
            return DESCUENTO_1;
        } else if(25 < minutos && minutos <= 45){
            return DESCUENTO_2;
        } else if(45 < minutos && minutos <= 70){
            return DESCUENTO_3;
        } else {
            return DESCUENTO_4;
        }
    }

    // Descripción: Obtiene todas las marcas de reloj
    // Entrada: EmpleadoEntity para el empleado
    // Salida: List con las marcas de reloj
    public List<MarcasRelojEntity> obtenerMarcasRelojPorEmpleado(EmpleadoEntity empleado){
        return marcasRelojRepository.findByRut(empleado.getRut());
    }

    public MarcasRelojEntity obtenerMarcaRelojPorFechaYEmpleado(String fecha, EmpleadoEntity empleado){
        return marcasRelojRepository.findByFechaAndEmpleado(fecha, empleado);
    }

    public void eliminarMarcasReloj(){
        marcasRelojRepository.deleteAll();
    }

}
