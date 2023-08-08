package com.coolbox.aplicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coolbox.aplicacion.dao.ICategoriasDao;
import com.coolbox.aplicacion.entity.Categorias;

@Controller
public class CategoriasController {
	
	@Autowired
	private ICategoriasDao categoriaDao;
	
	@GetMapping(value = "/categorias")
	public String listarCategorias(Model model) {
		model.addAttribute("categorias", categoriaDao.listarCategorias());
		model.addAttribute("titulo", "Crud de Categorias");
		return "listar-categorias";
	}
	
	@GetMapping(value = "/categorias/nuevo")
	public String mostrarFormularioNuevaCategoria(Model model) {
		model.addAttribute("categoria", new Categorias());
		model.addAttribute("titulo", "Crear Nueva Categoria");
		model.addAttribute("boton", "Registrar");
		model.addAttribute("modo", "registro");
		return "formulario-categoria";
	}
	
	@PostMapping(value = "/categorias/guardar")
	public String guardarCategoria(@ModelAttribute("categoria") Categorias categoria, Model model) {
		Categorias categoriaExistente = categoriaDao.obtenerCategoriaPorNombre(categoria.getNombreCategoria());
		if (categoriaExistente != null) {
			String mensaje = "La categoria ya existe";
			model.addAttribute("titulo", "Error");
			model.addAttribute("mensaje", mensaje);
			// Guardamos el ID de la categoría para redireccionar al formulario de edición
			if (categoria.getIdCategoria() != null) {
				model.addAttribute("direccion", "/categorias/" + categoria.getIdCategoria() + "/editar");
			} else {
				// Si el idCategoria es nulo, significa que es una nueva categoría, así que volvemos al formulario para agregar una nueva categoría
				model.addAttribute("direccion", "/categorias/nuevo");
			}
			return "mensaje-error";
		} else {
			categoriaDao.guardarCategoria(categoria);
			return "redirect:/categorias";
		}
	}
	
	@GetMapping(value = "/categorias/{idCategoria}/editar")
	public String mostrarFormularioEditarCategoria(@PathVariable("idCategoria") Long idCategoria, Model model) {
		Categorias categoria = categoriaDao.obtenerCategoria(idCategoria);
		if (categoria != null) {
			model.addAttribute("titulo", "Editar Categoria");
			model.addAttribute("categoria", categoria);
			model.addAttribute("boton", "Actualizar");
			model.addAttribute("modo", "edicion");
			return "formulario-categoria";
		}
		return "redirect:/categorias/listar";
	}
	
	@GetMapping(value = "/categorias/{idCategoria}/eliminar")
	public String eliminarCategoria(@PathVariable("idCategoria") Long idCategoria) {
		categoriaDao.eliminarCategoria(idCategoria);
		return "redirect:/categorias";
	}
}
