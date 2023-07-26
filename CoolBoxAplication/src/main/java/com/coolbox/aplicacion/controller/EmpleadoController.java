package com.coolbox.aplicacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coolbox.aplicacion.dao.ICategoriasDao;
import com.coolbox.aplicacion.dao.IMarcasDao;
import com.coolbox.aplicacion.dao.IProductosDao;
import com.coolbox.aplicacion.entity.Productos;

@Controller
public class EmpleadoController {
    
    @Autowired
    private IMarcasDao marcasDao;

    @Autowired
    private ICategoriasDao categoriaDao;

    @Autowired
    private IProductosDao productosDao;
    

    @GetMapping(value="/home/empleado")
    public String home(Model m){
        return "empleado";
    }

    @GetMapping("/empleado/productos")
    public String listarProductos(Model model) {
    	List<Productos> productos = productosDao.listarProductos();
    	
    	for (Productos producto : productos) {
    		producto.setNombreCategoria(producto.getCategoriaProducto().getNombreCategoria());
    		producto.setNombreMarca(producto.getMarcaProducto().getNombreMarca());
    		producto.setNombreRol(producto.getRolProducto().getNombreRol());
    	}
    	
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Lista de Productos");
        return "listar-productos-empleado";
    }
	
	@GetMapping("/empleado/categorias")
	public String listarCategorias(Model model) {
		model.addAttribute("categorias", categoriaDao.listarCategorias());
		model.addAttribute("titulo", "Lista de Categorias");
		return "listar-categorias-empleado";
	}

    @GetMapping("/empleado/marcas")
    public String listarMarcas(Model model) {
        model.addAttribute("marcas", marcasDao.listarMarcas());
        model.addAttribute("titulo", "Lista de Marcas");
        return "listar-marcas-empleado";
    }
}
