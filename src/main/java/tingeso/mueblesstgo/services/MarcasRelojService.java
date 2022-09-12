package tingeso.mueblesstgo.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.repositories.EmpleadoRepository;

@Service
public class MarcasRelojService {
    // Indica la ubicación del archivo de texto que contiene las marcas de reloj
    private String directorio="..//mueblesstgo//cargas//";
    private final Logger logg = LoggerFactory.getLogger(MarcasRelojService.class);

    // Importa el archivo de marcas de reloj
    public String guardarMarcasReloj(MultipartFile file) {
        // Debe verificar que el archivo exista y que sea de tipo texto
        if (!file.isEmpty()) {
            try {
                byte [] bytes= file.getBytes();
                Path path = Paths.get( directorio + file.getOriginalFilename() );
                Files.write(path, bytes);
                logg.info("Archivo guardado");
                leerMarcasReloj(path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Archivo guardado correctamente";
    }

    // Lee el contenido del archivo
    private void leerMarcasReloj(Path path) throws IOException {
        BufferedReader br = null;
        br = new BufferedReader(new java.io.FileReader(path.toString()));

        String linea = br.readLine();
        while (linea != null){
            leerLinea(linea);
            linea = br.readLine();
        }
    }

    @Autowired
    EmpleadoRepository empleadoRepository;
    // Lee cada linea del archivo
    private void leerLinea(String linea){
        //Separa los datos por el caracter ;
        String[] datos = linea.split(";");
        Calendar fecha = fechaIngreso(datos[0]);
        String hora = datos[1];
        String rut = datos[2];
        // Si el empleado existe, se crea la marca de reloj
        if(verificaEmpleado(rut)) {

        }




    }

    private Calendar fechaIngreso(String fechaS){
        String fecha[] = fechaS.split("/");
        Calendar fechaI = Calendar.getInstance();
        fechaI.set(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
        return fechaI;
    }

    // Verifica que existe el empleado en la base de datos
    private boolean verificaEmpleado(String rut){
        EmpleadoEntity empleado = empleadoRepository.findByRut(rut);
        if (empleado != null){
            //System.out.println(empleado.getNombres());
            return true;
        }
        return false;
    }
}
