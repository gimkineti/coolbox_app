package com.coolbox.aplicacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coolbox.aplicacion.dao.ICategoriasDao;
import com.coolbox.aplicacion.dao.IMarcasDao;
import com.coolbox.aplicacion.dao.IProductosDao;
import com.coolbox.aplicacion.dao.IRolesDao;
import com.coolbox.aplicacion.entity.Categorias;
import com.coolbox.aplicacion.entity.Marcas;
import com.coolbox.aplicacion.entity.Productos;
import com.coolbox.aplicacion.entity.Roles;

import jakarta.validation.Valid;

@Controller
public class ProductosController {

	@Autowired
    private IProductosDao productosDao;
	
	@Autowired
	private ICategoriasDao categoriasDao;
	
	@Autowired
	private IMarcasDao marcasDao;
	
	@Autowired
	private IRolesDao rolesDao;

    @GetMapping("/productos")
    public String listarProductos(Model model) {
    	List<Productos> productos = productosDao.listarProductos();
    	
    	for (Productos producto : productos) {
    		producto.setNombreCategoria(producto.getCategoriaProducto().getNombreCategoria());
    		producto.setNombreMarca(producto.getMarcaProducto().getNombreMarca());
    		producto.setNombreRol(producto.getRolProducto().getNombreRol());
    	}
    	
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Crud de Productos");
        return "listar-productos";
    }

    @GetMapping("/productos/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
    	model.addAttribute("producto", new Productos());
    	model.addAttribute("categorias", categoriasDao.listarCategorias());
    	model.addAttribute("marcas", marcasDao.listarMarcas());
    	model.addAttribute("roles", rolesDao.listarRoles());
    	model.addAttribute("titulo", "Crear Nuevo Producto");
    	return "formulario-producto";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") @Valid Productos producto, BindingResult result,
            @RequestParam("fechaProducto") @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaProducto, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("categorias", categoriasDao.listarCategorias());
            model.addAttribute("marcas", marcasDao.listarMarcas());
            model.addAttribute("roles", rolesDao.listarRoles());
            return "formulario-producto";
        }

        try {
        	Categorias categoria = categoriasDao.obtenerCategoria(producto.getCategoriaProducto().getIdCategoria());
        	Marcas marca = marcasDao.obtenerMarca(producto.getMarcaProducto().getIdMarca());
        	Roles rol = rolesDao.obtenerRol(producto.getRolProducto().getIdRol());
        	Productos descripcionExistente = productosDao.obtenerProductoPorDescripcion(producto.getDescripcionProducto());
        	if (descripcionExistente != null) {
        		String mensaje = "La descripción ya existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/productos/nuevo");
                return "mensaje-error";
        	}
        	else if (categoria == null) {
        		String mensaje = "La categoria seleccionada no existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/productos/nuevo");
                return "mensaje-error";
        	}
        	else if (marca == null) {
        		String mensaje = "La marca seleccionada no existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/productos/nuevo");
                return "mensaje-error";
        	}
        	else if (rol == null) {
        		String mensaje = "El rol seleccionado no existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/productos/nuevo");
                return "mensaje-error";
        	} else {
        		producto.setCategoriaProducto(categoria);
            	producto.setMarcaProducto(marca);
            	producto.setRolProducto(rol);
                java.sql.Date fecha = java.sql.Date.valueOf(fechaProducto);
                producto.setFechaProducto(fecha);
                productosDao.guardarProducto(producto);
                return "redirect:/productos";
        	}
        } catch (Exception e) {
            String mensaje = "Error al guardar el producto: " + e.getMessage();
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/productos/nuevo");
            return "mensaje-error";
        }
    }

    @GetMapping("/productos/{idProducto}/editar")
    public String mostrarFormularioEditarProducto(@PathVariable("idProducto") Long idProducto, Model model) {
        Productos producto = productosDao.obtenerProducto(idProducto);
        if (producto != null) {
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriasDao.listarCategorias());
        	model.addAttribute("marcas", marcasDao.listarMarcas());
        	model.addAttribute("roles", rolesDao.listarRoles());
            return "formulario-producto";
        }
        return "redirect:/productos";
    }

    @GetMapping("/productos/{idProducto}/eliminar")
    public String eliminarProducto(@PathVariable("idProducto") Long idProducto) {
        productosDao.eliminarProducto(idProducto);
        return "redirect:/productos";
    }
}