package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.services.HorasExtraService;
import tingeso.mueblesstgo.services.ValidadorService;

@Controller
@RequestMapping
public class HorasExtraController {
    @Autowired
    private HorasExtraService horasExtraService;
    @Autowired
    private ValidadorService validadorService;

    @GetMapping("/ingresar-horas")
    public String ingresarHoras() {
        return "horas";
    }

    @PostMapping("/horas")
    public String horas(@RequestParam("horas") Integer horas, @RequestParam("rut") String rut, @RequestParam("fecha") String fecha, RedirectAttributes ms) {
        boolean mensaje = horasExtraService.guardarHorasExtra(horas, rut, fecha);
        boolean validarHoras = validadorService.validarHoras(horas);
        boolean validarRut = validadorService.validarRut(rut);
        boolean validarFecha = validadorService.validarFecha(fecha);
        ms.addFlashAttribute("mensaje", mensaje);
        ms.addFlashAttribute("validarHoras", validarHoras);
        ms.addFlashAttribute("validarRut", validarRut);
        ms.addFlashAttribute("validarFecha", validarFecha);
        return "redirect:/ingresar-horas";
    }
}
