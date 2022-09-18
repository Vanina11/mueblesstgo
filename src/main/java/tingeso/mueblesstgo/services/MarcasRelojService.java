package tingeso.mueblesstgo.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;
import tingeso.mueblesstgo.repositories.MarcasRelojRepository;

@Service
public class MarcasRelojService {
    // Indica la ubicación del archivo de texto que contiene las marcas de reloj
    private String directorio="..//mueblesstgo//cargas//";

    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    MarcasRelojRepository marcasRelojRepository;


    // Descripción: Permite importar el archivo de marcas de reloj, para esto verifica que no esté vacío y que la extensiòn sea .txt
    // Entrada: Archivo de texto con las marcas de reloj
    // Salida: void
    public boolean guardarMarcasReloj(MultipartFile file) {
        if (!file.isEmpty() && Objects.requireNonNull(file.getOriginalFilename()).endsWith(".txt")) {
            try {
                byte [] bytes= file.getBytes();
                Path path = Paths.get( directorio + file.getOriginalFilename() );
                Files.write(path, bytes);
                leerMarcasReloj(path);
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    // Descripción: Lee el contenido del archivo de las marcas de reloj
    // Entrada: Ruta del archivo de texto
    // Salida: void
    private void leerMarcasReloj(Path path) throws IOException {
        BufferedReader br = null;
        br = new BufferedReader(new java.io.FileReader(path.toString()));
        String linea = br.readLine();
        // Hasta que no se llegue al final del archivo
        while (linea != null){
            leerLinea(linea);
            linea = br.readLine();
        }
    }

    // Descipción: Lee una linea del archivo y analiza la información, separa en fecha, hora y rut del empleado. Además verifica que el rut exista
    // en la base de datos, en caso contrario no se guarda la marca de reloj.
    // Entrada: Línea como string
    // Salida: void
    private void leerLinea(String linea){
        String[] datos = linea.split(";");
        String fecha = datos[0];
        String hora = datos[1];
        String rut = datos[2];
        // Si el empleado existe, se crea la marca de reloj
        EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
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
    // Entrada: Calendar con la fecha, String con la hora y String con el rut del empleado
    // Salida: void
    public void crearMarcaReloj(String fecha, String hora, String rut) {
        EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
        Integer descuento = calcularDescuentoAtraso(hora);
        MarcasRelojEntity marcaReloj = new MarcasRelojEntity();
        marcaReloj.setFecha(fecha);
        marcaReloj.setHora(hora);
        marcaReloj.setEmpleado(empleado);
        if(descuento != 15){
            empleadoService.incrementaDescuentoAtraso(empleado, descuento);
        }else {
            empleadoService.incrementaInasistencias(empleado);
        }
        marcasRelojRepository.save(marcaReloj);
    }

    private Integer calcularDescuentoAtraso(String hora) {
        String[] horaMinuto = hora.split(":");
        Integer horaInt = Integer.parseInt(horaMinuto[0]);
        Integer minutoInt = Integer.parseInt(horaMinuto[1]);
        if(horaInt <= 8 && minutoInt <= 10){
            return 0;
        } else {
            Integer minutosAtraso = (horaInt - 8) * 60 + minutoInt - 10;
            return montoDescuentoAtrasos(minutosAtraso);
        }
    }
    private Integer montoDescuentoAtrasos(Integer minutos){
        if(10 < minutos && minutos <= 25){
            return 1;
        } else if(25 < minutos && minutos <= 45){
            return 3;
        } else if(45 < minutos && minutos <= 70){
            return 6;
        } else {
            return 15;
        }
    }
    private boolean determinarInasistencia(Integer minutosAtraso){
        return minutosAtraso > 70;
    }

    public List<MarcasRelojEntity> obtenerMarcasRelojPorEmpleado(EmpleadoEntity empleado){
        return marcasRelojRepository.findByRut(empleado.getRut());
    }
}
