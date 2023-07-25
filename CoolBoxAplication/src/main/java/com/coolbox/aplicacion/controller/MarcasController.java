package com.coolbox.aplicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coolbox.aplicacion.dao.IMarcasDao;
import com.coolbox.aplicacion.entity.Marcas;

@Controller
public class MarcasController {
	
	@Autowired
    private IMarcasDao marcasDao;

    @GetMapping("/marcas")
    public String listarMarcas(Model model) {
        model.addAttribute("marcas", marcasDao.listarMarcas());
        model.addAttribute("titulo", "Crud de Marcas");
        return "listar-marcas";
    }

    @GetMapping("/marcas/nuevo")
    public String mostrarFormularioNuevaMarca(Model model) {
        model.addAttribute("marca", new Marcas());
        model.addAttribute("titulo", "Crear Nueva Marca");
        return "formulario-marca";
    }

    @PostMapping("/marcas/guardar")
    public String guardarMarca(@ModelAttribute("marca") Marcas marca, Model model) {
        Marcas marcaExistente = marcasDao.obtenerMarcaPorNombre(marca.getNombreMarca());
        if (marcaExistente != null && !marcaExistente.getIdMarca().equals(marca.getIdMarca())) {
            String mensaje = "La marca ya existe";
            model.addAttribute("titulo", "Error");
			model.addAttribute("mensaje", mensaje);

            if (marca.getIdMarca() != null) {
                model.addAttribute("direccion", "/marcas/" + marca.getIdMarca() + "/editar");
            } else {
                model.addAttribute("direccion", "/marcas/nuevo");
            }
            return "mensaje-error";
        } else {
            marcasDao.guardarMarca(marca);
            return "redirect:/marcas";
        }
    }


    @GetMapping("/marcas/{idMarca}/editar")
    public String mostrarFormularioEditarMarca(@PathVariable("idMarca") Long idMarca, Model model) {
        Marcas marca = marcasDao.obtenerMarca(idMarca);
        if (marca != null) {
        	model.addAttribute("titulo", "Editar Marca");
            model.addAttribute("marca", marca);
            return "formulario-marca";
        }
        return "redirect:/marcas/listar";
    }

    @GetMapping("/marcas/{idMarca}/eliminar")
    public String eliminarMarca(@PathVariable("idMarca") Long idMarca) {
        marcasDao.eliminarMarca(idMarca);
        return "redirect:/marcas";
    }
}
