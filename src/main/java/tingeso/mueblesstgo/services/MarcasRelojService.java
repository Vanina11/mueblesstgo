package tingeso.mueblesstgo.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logg = LoggerFactory.getLogger(MarcasRelojService.class);

    @Autowired
    EmpleadoRepository empleadoRepository;
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
        // Separa la línea por ;
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
            }
        }
    }

    // Descripción: Crea la marca de reloj
    // Entrada: Calendar con la fecha, String con la hora y String con el rut del empleado
    // Salida: void
    public void crearMarcaReloj(String fecha, String hora, String rut) {
        EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
        MarcasRelojEntity marcaReloj = new MarcasRelojEntity();
        marcaReloj.setFecha(fecha);
        marcaReloj.setHora(hora);
        marcaReloj.setEmpleado(empleado);
        marcasRelojRepository.save(marcaReloj);
    }
}
