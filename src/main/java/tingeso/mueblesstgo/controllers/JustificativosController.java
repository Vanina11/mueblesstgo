package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.services.JustificativoService;
import tingeso.mueblesstgo.services.ValidadorService;

@Controller
@RequestMapping
public class JustificativosController {
    @Autowired
    private JustificativoService justificativoService;
    @Autowired
    private ValidadorService validadorService;

    @GetMapping("/ingresar-justificativo")
    public String ingresarJustificativo() {
        return "justificativo";
    }

    @PostMapping("/justificativo")
    public String justificativo(@RequestParam("fecha") String fecha, @RequestParam("rut") String rut, RedirectAttributes ms) {
        boolean mensaje = justificativoService.guardarJustificativo(fecha, rut);
        boolean validarFecha = validadorService.validarFecha(fecha);
        boolean validarRut = validadorService.validarRut(rut);
        ms.addFlashAttribute("mensaje", mensaje);
        ms.addFlashAttribute("validarFecha", validarFecha);
        ms.addFlashAttribute("validarRut", validarRut);
        return "redirect:/ingresar-justificativo";
    }
}
