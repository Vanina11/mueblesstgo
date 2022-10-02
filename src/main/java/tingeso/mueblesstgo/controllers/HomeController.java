package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tingeso.mueblesstgo.services.SueldosService;

@Controller
@RequestMapping
public class HomeController {
    @Autowired
    private SueldosService sueldosService;
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/eliminar")
    public String eliminar() {
        sueldosService.limpiarRegistros();
        return "eliminar";
    }

}