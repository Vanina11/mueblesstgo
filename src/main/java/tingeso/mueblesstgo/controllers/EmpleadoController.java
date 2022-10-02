package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.services.ValidadorService;

@Controller
@RequestMapping
public class EmpleadoController {
    @Autowired
    private ValidadorService validadorService;

    @GetMapping("/ingresar-empleado")
    public String home() {
        return "empleado";
    }

    @PostMapping("/empleado")
    public String empleado(@RequestParam("nombres") String nombres, @RequestParam("apellidos") String apellidos,
                           @RequestParam("rut") String rut, @RequestParam("categoria") String categoria,
                           @RequestParam("fechaIngreso") String fechaIngreso,  @RequestParam("fechaNacimiento") String fechaNacimiento,
                           RedirectAttributes ms) {
        boolean resultado = validadorService.crearEmpleado(nombres, apellidos, rut, categoria, fechaIngreso, fechaNacimiento);
        boolean validarCategoria = validadorService.validarCategoria(categoria);
        boolean validarFechaIngreso = validadorService.validarFecha(fechaIngreso);
        boolean validarFechaNacimiento = validadorService.validarFecha(fechaNacimiento);
        ms.addFlashAttribute("resultado", resultado);
        ms.addFlashAttribute("validarFechaIngreso", validarFechaIngreso);
        ms.addFlashAttribute("validarFechaNacimiento", validarFechaNacimiento);
        ms.addFlashAttribute("validarCategoria", validarCategoria);
        return "redirect:/ingresar-empleado";
    }
}
