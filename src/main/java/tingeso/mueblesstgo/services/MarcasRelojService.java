package tingeso.mueblesstgo.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MarcasRelojService {
    // Indica la ubicación del archivo de texto que contiene las marcas de reloj
    private String directorio="mueblesStgo//cargas//";
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Archivo guardado correctamente";
    }
}
