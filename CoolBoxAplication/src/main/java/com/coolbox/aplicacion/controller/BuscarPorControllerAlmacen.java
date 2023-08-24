package com.coolbox.aplicacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coolbox.aplicacion.dao.ICategoriasDao;
import com.coolbox.aplicacion.dao.IMarcasDao;
import com.coolbox.aplicacion.dao.IProductosDao;
import com.coolbox.aplicacion.entity.Categorias;
import com.coolbox.aplicacion.entity.Marcas;
import com.coolbox.aplicacion.entity.Productos;

@Controller
public class BuscarPorControllerAlmacen {
    @Autowired
    private IProductosDao productosDao;

    @Autowired
    private IMarcasDao marcasDao;

    @Autowired
    private ICategoriasDao categoriasDao;

    @GetMapping("/almacen/productos/descripcion")
    public String mostrarFormularioDescripcion(Model model){
        model.addAttribute("titulo", "Buscar Productos Por Descripción");
        return "buscar-descripcion-almacen";
    }

    @PostMapping("/almacen/productos/descripcion/resultados")
    public String buscarResultadosPorDescripcion(@RequestParam("descripcion") String descripcion, Model model) {
        List<Productos> productosEncontrados = productosDao.buscarProductosPorDescripcion(descripcion);

        if (productosEncontrados.isEmpty()) {
            String mensaje = "No hay productos que coincidan con la descripción.";
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/almacen/productos/descripcion");
            return "mensaje-error"; // Vista para mostrar el mensaje de error
        } else {
            model.addAttribute("productos", productosEncontrados); // Cambio aquí
            model.addAttribute("titulo", "RESULTADOS DE LA BÚSQUEDA");
            return "resultado-descripcion-almacen"; // Vista para mostrar los resultados
        }
    }

    @GetMapping("/almacen/productos/marca")
    public String mostrarFormularioMarca(Model model) {
        model.addAttribute("titulo", "Buscar Productos Por Marca");
        List<Marcas> marcas = marcasDao.listarMarcas(); // Obtener la lista de marcas
        model.addAttribute("marcas", marcas); // Agregar la lista de marcas al modelo
        return "buscar-marca-almacen";
    }

    @PostMapping("/almacen/productos/marca/resultados")
    public String buscarPorMarca(@RequestParam("marca") Long idMarca, Model model) {
        Marcas marcaSeleccionada = marcasDao.obtenerMarca(idMarca);
        
        List<Productos> productosEncontrados = productosDao.buscarProductosPorMarca(marcaSeleccionada);

        if (productosEncontrados.isEmpty()) {
            String mensaje = "No hay productos de la marca " + marcaSeleccionada.getNombreMarca();
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/almacen/productos/marca");
            return "mensaje-error";
        } else {
            model.addAttribute("productos", productosEncontrados);
            model.addAttribute("titulo", "PRODUCTOS ENCONTRADOS CON LA MARCA " + marcaSeleccionada.getNombreMarca());
            return "resultado-marca-almacen";
        }
    }

    @GetMapping("/almacen/productos/categoria")
    public String mostrarFormularioCategoria(Model model) {
        model.addAttribute("titulo", "Buscar Productos Por Categoria");
        List<Categorias> categorias = categoriasDao.listarCategorias();
        model.addAttribute("categorias", categorias);
        return "buscar-categoria-almacen";
    }

    @PostMapping("almacen/productos/categoria/resultados")
    public String buscarPorCategoria(@RequestParam("categoria") Long idCategoria, Model model) {
        Categorias categoriaSeleccionada = categoriasDao.obtenerCategoria(idCategoria);
        List<Productos> productosEncontrados = productosDao.buscarProductosPorCategoria(categoriaSeleccionada);
        if (productosEncontrados.isEmpty()) {
            String mensaje = "No hay productos de la categoria " + categoriaSeleccionada.getNombreCategoria();
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/almacen/productos/categoria");
            return "mensaje-error";
        } else {
            model.addAttribute("productos", productosEncontrados);
            model.addAttribute("titulo", "PRODUCTOS ENCONTRADOS CON LA CATEGORIA " + categoriaSeleccionada.getNombreCategoria());
            return "resultado-categoria-almacen";
        }
    }
}