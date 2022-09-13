package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.services.HorasExtraService;
import tingeso.mueblesstgo.services.JustificativoService;
import tingeso.mueblesstgo.services.MarcasRelojService;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private MarcasRelojService cargarMarcasRelojService;
    @Autowired
    private JustificativoService justificativoService;
    @Autowired
    private HorasExtraService horasExtraService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/ingresar-justificativo")
    public String ingresarJustificativo() {
        return "justificativo";
    }

    @GetMapping("/ingresar-horas")
    public String ingresarHoras() {
        return "horas";
    }

    @PostMapping("/cargar")
    public String cargar(@RequestParam("archivos") MultipartFile file, RedirectAttributes ms) {
        cargarMarcasRelojService.guardarMarcasReloj(file);
        ms.addFlashAttribute("mensaje", "Archivo guardado correctamente!!");
        return "redirect:/";
    }

    @PostMapping("/justificativo")
    public String justificativo(@RequestParam("fecha") String fecha, @RequestParam("rut") String rut, RedirectAttributes ms) {
        justificativoService.guardarJustificativo(fecha, rut);
        ms.addFlashAttribute("mensaje", "Justificativo guardado correctamente!!");
        return "redirect:/";
    }

    @PostMapping("/horas")
    public String horas(@RequestParam("horas") Integer horas, @RequestParam("rut") String rut, RedirectAttributes ms) {
        horasExtraService.guardarHorasExtra(horas, rut);
        ms.addFlashAttribute("mensaje", "Horas extra guardadas correctamente!!");
        return "redirect:/";
    }

}