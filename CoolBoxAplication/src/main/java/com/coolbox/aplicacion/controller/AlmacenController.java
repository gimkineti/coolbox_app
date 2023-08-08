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
import com.coolbox.aplicacion.entity.Roles;

import jakarta.validation.Valid;

import com.coolbox.aplicacion.entity.Productos;

@Controller
public class AlmacenController {
	
	@Autowired
    private IMarcasDao marcasDao;

    @Autowired
    private ICategoriasDao categoriasDao;

    @Autowired
    private IProductosDao productosDao;
    
    @Autowired
    private IRolesDao rolesDao;
    
    @GetMapping(value="/home/almacen")
    public String home(Model m){
        return "almacen";
    }
    
    @GetMapping("/almacen/productos")
    public String listarProductos(Model model) {
    	List<Productos> productos = productosDao.listarProductos();
    	
    	for (Productos producto : productos) {
    		producto.setNombreCategoria(producto.getCategoriaProducto().getNombreCategoria());
    		producto.setNombreMarca(producto.getMarcaProducto().getNombreMarca());
    		producto.setNombreRol(producto.getRolProducto().getNombreRol());
    	}
    	
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Lista de Productos");
        return "listar-productos-almacen";
    }
    
    @GetMapping("/almacen/productos/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Productos());
        model.addAttribute("categorias", categoriasDao.listarCategorias());
        model.addAttribute("marcas", marcasDao.listarMarcas());
        model.addAttribute("roles", rolesDao.listarRolesAlmacen());
        model.addAttribute("titulo", "Crear Nuevo Producto");
        model.addAttribute("boton", "Registrar");
        model.addAttribute("modo", "registro");
        return "formulario-producto-almacen";
    }
    
    @PostMapping("/almacen/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") @Valid Productos producto, BindingResult result,
                                  @RequestParam("fechaProducto") @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaProducto,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Agregar Producto");
            model.addAttribute("categorias", categoriasDao.listarCategorias());
            model.addAttribute("marcas", marcasDao.listarMarcas());
            return "formulario-producto-almacen";
        }

        try {
            Categorias categoria = categoriasDao.obtenerCategoria(producto.getCategoriaProducto().getIdCategoria());
            Marcas marca = marcasDao.obtenerMarca(producto.getMarcaProducto().getIdMarca());
            Roles rol = rolesDao.obtenerRolPorNombre("ALMACEN");

            if (producto.getIdProducto() == null) {
                Productos descripcionExistente = productosDao.obtenerProductoPorDescripcion(producto.getDescripcionProducto());

                if (descripcionExistente != null) {
                    String mensaje = "La descripción ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/almacen/productos/nuevo");
                    return "mensaje-error";
                }
                producto.setCategoriaProducto(categoria);
                producto.setMarcaProducto(marca);
                producto.setRolProducto(rol);
                java.sql.Date fecha = java.sql.Date.valueOf(fechaProducto);
                producto.setFechaProducto(fecha);
                productosDao.guardarProducto(producto);
            } else {
                // Es un producto existente, verificar si la descripción ya existe para otro producto
                Productos descripcionExistente = productosDao.obtenerProductoPorDescripcion(producto.getDescripcionProducto());
                if (descripcionExistente != null) {
                    String mensaje = "La descripción ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/almacen/productos/" + producto.getIdProducto() + "/editar");
                    return "mensaje-error";
                }
                producto.setCategoriaProducto(categoria);
                producto.setMarcaProducto(marca);
                producto.setRolProducto(rol);
                java.sql.Date fecha = java.sql.Date.valueOf(fechaProducto);
                producto.setFechaProducto(fecha);
                productosDao.guardarProducto(producto);
            }
            return "redirect:/almacen/productos";
        } catch (Exception e) {
            String mensaje = "Error al guardar el producto: " + e.getMessage();
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/almacen/productos/nuevo");
            return "mensaje-error";
        }
    }
    
    @GetMapping("/almacen/productos/{idProducto}/editar")
    public String mostrarFormularioEditarProducto(@PathVariable("idProducto") Long idProducto, Model model) {
        Productos producto = productosDao.obtenerProducto(idProducto);
        if (producto != null) {
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriasDao.listarCategorias());
            model.addAttribute("marcas", marcasDao.listarMarcas());
            model.addAttribute("boton", "Actualizar");
            model.addAttribute("modo", "edicion");

            List<Roles> rolesAlmacen = rolesDao.listarRolesAlmacen();
            if (rolesAlmacen.isEmpty()) {
                String mensaje = "El rol no existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/almacen/productos");
                return "mensaje-error";
            } else {
                model.addAttribute("roles", rolesAlmacen);
            }

            return "formulario-producto-almacen";
        }
        return "redirect:/almacen/productos";
    }

    @GetMapping(value = "/almacen/categorias")
	public String listarCategorias(Model model) {
		model.addAttribute("categorias", categoriasDao.listarCategorias());
		model.addAttribute("titulo", "Lista de Categorias");
		return "listar-categorias-almacen";
	}
	
	@GetMapping(value = "/almacen/categorias/nuevo")
	public String mostrarFormularioNuevaCategoria(Model model) {
		model.addAttribute("categoria", new Categorias());
		model.addAttribute("titulo", "Crear Nueva Categoria");
        model.addAttribute("boton", "Registrar");
        model.addAttribute("modo", "registro");
		return "formulario-categoria-almacen";
	}
	
	@PostMapping(value = "/almacen/categorias/guardar")
	public String guardarCategoria(@ModelAttribute("categoria") Categorias categoria, Model model) {
		Categorias categoriaExistente = categoriasDao.obtenerCategoriaPorNombre(categoria.getNombreCategoria());
		if (categoriaExistente != null) {
			String mensaje = "La categoria ya existe";
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            if (categoria.getIdCategoria() != null) {
				model.addAttribute("direccion", "/almacen/categorias/" + categoria.getIdCategoria() + "/editar");
			} else {
				// Si el idCategoria es nulo, significa que es una nueva categoría, así que volvemos al formulario para agregar una nueva categoría
				model.addAttribute("direccion", "/almacen/categorias/nuevo");
			}
			return "mensaje-error";
	    } else {
	        categoriasDao.guardarCategoria(categoria);
	        return "redirect:/almacen/categorias";
	    }
	}
	
	@GetMapping(value = "/almacen/categorias/{idCategoria}/editar")
	public String mostrarFormularioEditarCategoria(@PathVariable("idCategoria") Long idCategoria, Model model) {
		Categorias categoria = categoriasDao.obtenerCategoria(idCategoria);
		if (categoria != null) {
			model.addAttribute("titulo", "Editar Categoria");
			model.addAttribute("categoria", categoria);
            model.addAttribute("boton", "Actualizar");
            model.addAttribute("modo", "edicion");
			return "formulario-categoria-almacen";
		}
		return "redirect:/almacen/categorias/listar";
	}

	@GetMapping("/almacen/marcas")
    public String listarMarcas(Model model) {
        model.addAttribute("marcas", marcasDao.listarMarcas());
        model.addAttribute("titulo", "Lista de Marcas");
        return "listar-marcas-almacen";
    }

    @GetMapping("/almacen/marcas/nuevo")
    public String mostrarFormularioNuevaMarca(Model model) {
        model.addAttribute("marca", new Marcas());
        model.addAttribute("titulo", "Crear Nueva Marca");
        model.addAttribute("boton", "Registrar");
        model.addAttribute("modo", "registro");
        return "formulario-marca-almacen";
    }

    @PostMapping("/almacen/marcas/guardar")
    public String guardarMarca(@ModelAttribute("marca") Marcas marca, Model model) {
        Marcas marcaExistente = marcasDao.obtenerMarcaPorNombre(marca.getNombreMarca());
        if (marcaExistente != null) {
            String mensaje = "La marca ya existe";
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            if (marca.getIdMarca() != null) {
                model.addAttribute("direccion", "/almacen/marcas/" + marca.getIdMarca() + "/editar");
            } else {
                model.addAttribute("direccion", "/almacen/marcas/nuevo");
            }
            return "mensaje-error";
        } else {
            marcasDao.guardarMarca(marca);
            return "redirect:/almacen/marcas";
        }
    }


    @GetMapping("/almacen/marcas/{idMarca}/editar")
    public String mostrarFormularioEditarMarca(@PathVariable("idMarca") Long idMarca, Model model) {
        Marcas marca = marcasDao.obtenerMarca(idMarca);
        if (marca != null) {
        	model.addAttribute("titulo", "Editar Marca");
            model.addAttribute("marca", marca);
            model.addAttribute("boton", "Actualizar");
            model.addAttribute("modo", "edicion");
            return "formulario-marca-almacen";
        }
        return "redirect:/almacen/marcas/listar";
    }
}
