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
	
	@GetMapping(value = "/admin/categorias")
	public String listarCategorias(Model model) {
		model.addAttribute("categorias", categoriaDao.listarCategorias());
		model.addAttribute("titulo", "Crud de Categorias");
		return "listar-categorias-admin";
	}
	
	@GetMapping(value = "/admin/categorias/nuevo")
	public String mostrarFormularioNuevaCategoria(Model model) {
		model.addAttribute("categoria", new Categorias());
		model.addAttribute("titulo", "Crear Nueva Categoria");
		model.addAttribute("boton", "Registrar");
		model.addAttribute("modo", "registro");
		return "formulario-categoria-admin";
	}
	
	@PostMapping(value = "/admin/categorias/guardar")
	public String guardarCategoria(@ModelAttribute("categoria") Categorias categoria, Model model) {
		Categorias categoriaExistente = categoriaDao.obtenerCategoriaPorNombre(categoria.getNombreCategoria());
		if (categoriaExistente != null) {
			String mensaje = "La categoria ya existe";
			model.addAttribute("titulo", "Error");
			model.addAttribute("mensaje", mensaje);
			if (categoria.getIdCategoria() != null) {
				model.addAttribute("direccion", "/admin/categorias/" + categoria.getIdCategoria() + "/editar");
			} else {
				model.addAttribute("direccion", "/admin/categorias/nuevo");
			}
			return "mensaje-error";
		} else {
			categoriaDao.guardarCategoria(categoria);
			return "redirect:/admin/categorias";
		}
	}
	
	@GetMapping(value = "/admin/categorias/{idCategoria}/editar")
	public String mostrarFormularioEditarCategoria(@PathVariable("idCategoria") Long idCategoria, Model model) {
		Categorias categoria = categoriaDao.obtenerCategoria(idCategoria);
		if (categoria != null) {
			model.addAttribute("titulo", "Editar Categoria");
			model.addAttribute("categoria", categoria);
			model.addAttribute("boton", "Actualizar");
			model.addAttribute("modo", "edicion");
			return "formulario-categoria-admin";
		}
		return "redirect:/admin/categorias";
	}
	
	@GetMapping(value = "/admin/categorias/{idCategoria}/eliminar")
	public String eliminarCategoria(@PathVariable("idCategoria") Long idCategoria) {
		categoriaDao.eliminarCategoria(idCategoria);
		return "redirect:/admin/categorias";
	}
}