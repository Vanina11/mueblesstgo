package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.services.MarcasRelojService;

@Controller
@RequestMapping
public class MarcasRelojController {
    @Autowired
    private MarcasRelojService cargarMarcasRelojService;

    @GetMapping("/ingresar-marcas")
    public String ingresarMarcas() {
        return "marcas";
    }

    @PostMapping("/cargar")
    public String cargar(@RequestParam("archivos") MultipartFile file, RedirectAttributes ms) {
        boolean mensaje = cargarMarcasRelojService.guardarMarcasReloj(file);
        ms.addFlashAttribute("mensaje", mensaje);
        return "redirect:/ingresar-marcas";
    }
}
