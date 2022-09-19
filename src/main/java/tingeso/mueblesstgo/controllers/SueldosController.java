package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.entities.SueldosEntity;
import tingeso.mueblesstgo.services.SueldosService;

import java.util.List;

@Controller
@RequestMapping
public class SueldosController {
    @Autowired
    private SueldosService sueldosService;

    @GetMapping("/calcular-sueldos")
    public String calcularSueldos(RedirectAttributes ms) {
        boolean mensaje = sueldosService.calcularSueldos();
        ms.addFlashAttribute("mensaje", mensaje);
        return "sueldos";
    }

    @GetMapping("/plantilla-sueldos")
    public String plantillaSueldos(Model model) {
        List<SueldosEntity> sueldos = sueldosService.obtenerSueldos();
        model.addAttribute("sueldos", sueldos);
        return "plantilla";
    }
}
