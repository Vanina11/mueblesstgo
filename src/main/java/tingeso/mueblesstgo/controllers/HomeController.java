package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.entities.SueldosEntity;
import tingeso.mueblesstgo.services.*;

import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private MarcasRelojService cargarMarcasRelojService;
    @Autowired
    private JustificativoService justificativoService;
    @Autowired
    private HorasExtraService horasExtraService;

    @Autowired
    private ValidadorService validadorService;
    @Autowired
    private OficinaRRHHService oficinaRRHHService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/ingresar-marcas")
    public String ingresarMarcas() {
        return "marcas";
    }

    @GetMapping("/ingresar-justificativo")
    public String ingresarJustificativo() {
        return "justificativo";
    }

    @GetMapping("/ingresar-horas")
    public String ingresarHoras() {
        return "horas";
    }

    @GetMapping("/calcular-sueldos")
    public String calcularSueldos(RedirectAttributes ms) {
        boolean mensaje = oficinaRRHHService.calcularSueldos();
        ms.addFlashAttribute("mensaje", mensaje);
        return "sueldos";
    }

    @GetMapping("/plantilla-sueldos")
    public String plantillaSueldos(Model model) {
        List<SueldosEntity> sueldos = oficinaRRHHService.obtenerSueldos();
        model.addAttribute("sueldos", sueldos);
        return "plantilla";
    }

    @PostMapping("/cargar")
    public String cargar(@RequestParam("archivos") MultipartFile file, RedirectAttributes ms) {
        boolean mensaje = cargarMarcasRelojService.guardarMarcasReloj(file);
        ms.addFlashAttribute("mensaje", mensaje);
        return "redirect:/ingresar-marcas";
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