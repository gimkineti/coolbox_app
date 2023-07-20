package com.coolbox.aplicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coolbox.aplicacion.dao.IProductosDao;
import com.coolbox.aplicacion.entity.Productos;

@Controller
public class ProductosController {

	@Autowired
    private IProductosDao productosDao;

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productosDao.listarProductos());
        model.addAttribute("titulo", "Crud de Productos");
        return "listar-productos";
    }

    @GetMapping("/productos/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Productos());
        model.addAttribute("titulo", "Crear Nuevo Producto");
        return "formulario-producto";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") @Validated Productos producto, BindingResult result,
            @RequestParam("fechaProducto") @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaProducto, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Error de validación. Por favor, verifica los campos.");
            return "formulario-producto";
        }

        try {
            java.sql.Date fecha = java.sql.Date.valueOf(fechaProducto);
            producto.setFechaProducto(fecha);
            productosDao.guardarProducto(producto);
            return "redirect:/productos";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al guardar el producto.");
            return "formulario-producto";
        }
    }

    @GetMapping("/productos/{idProducto}/editar")
    public String mostrarFormularioEditarProducto(@PathVariable("idProducto") Long idProducto, Model model) {
        Productos producto = productosDao.obtenerProducto(idProducto);
        if (producto != null) {
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("producto", producto);
            return "formulario-producto";
        }
        return "redirect:/productos";
    }
    
    @PostMapping("/productos/actualizar")
    public String actualizarProducto(@ModelAttribute("producto") @Validated Productos producto, BindingResult result,
                                     @RequestParam("fechaProducto") @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaProducto, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Error de validación. Por favor, verifica los campos.");
            model.addAttribute("titulo", "Editar Producto");
            return "formulario-producto";
        }

        try {
            java.sql.Date fecha = java.sql.Date.valueOf(fechaProducto);
            producto.setFechaProducto(fecha);
            productosDao.actualizarProducto(producto);
            return "redirect:/productos";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al actualizar el producto.");
            model.addAttribute("titulo", "Editar Producto");
            return "formulario-producto";
        }
    }

    @GetMapping("/productos/{idProducto}/eliminar")
    public String eliminarProducto(@PathVariable("idProducto") Long idProducto) {
        productosDao.eliminarProducto(idProducto);
        return "redirect:/productos";
    }
}
