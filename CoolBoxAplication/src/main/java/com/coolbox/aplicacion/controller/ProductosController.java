package com.coolbox.aplicacion.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.coolbox.aplicacion.service.ExcelGenerator;
import com.coolbox.aplicacion.service.PdfGenerator;

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

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private ExcelGenerator excelGenerator;

    @GetMapping("/admin/productos")
    public String listarProductos(Model model) {
    	List<Productos> productos = productosDao.listarProductos();
    	
    	for (Productos producto : productos) {
    		producto.setNombreCategoria(producto.getCategoriaProducto().getNombreCategoria());
    		producto.setNombreMarca(producto.getMarcaProducto().getNombreMarca());
    		producto.setNombreRol(producto.getRolProducto().getNombreRol());
    	}
    	
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Crud de Productos");
        return "listar-productos-admin";
    }

    @GetMapping("/admin/productos/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
    	model.addAttribute("producto", new Productos());
    	model.addAttribute("categorias", categoriasDao.listarCategorias());
    	model.addAttribute("marcas", marcasDao.listarMarcas());
    	model.addAttribute("roles", rolesDao.listarRoles());
    	model.addAttribute("titulo", "Crear Nuevo Producto");
        model.addAttribute("boton", "Registrar");
        model.addAttribute("modo", "registro");
    	return "formulario-producto-admin";
    }

    @PostMapping("/admin/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") @Valid Productos producto, BindingResult result,
                                @RequestParam("fechaProducto") @DateTimeFormat(pattern = "yyyy-MM-dd") String fechaProducto,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Producto");
            model.addAttribute("categorias", categoriasDao.listarCategorias());
            model.addAttribute("marcas", marcasDao.listarMarcas());
            model.addAttribute("roles", rolesDao.listarRoles());
            return "formulario-producto-admin";
        }

        try {
            Marcas marca = marcasDao.obtenerMarca(producto.getMarcaProducto().getIdMarca());
            Categorias categoria = categoriasDao.obtenerCategoria(producto.getCategoriaProducto().getIdCategoria());
            Roles rol = rolesDao.obtenerRol(producto.getRolProducto().getIdRol());
            if (producto.getIdProducto() == null) {
                Productos descripcionExistente = productosDao.obtenerProductoPorDescripcion(producto.getDescripcionProducto());

                if (descripcionExistente != null) {
                    String mensaje = "La descripción ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/admin/productos/nuevo");
                    return "mensaje-error";
                }
                producto.setCategoriaProducto(categoria);
                producto.setMarcaProducto(marca);
                producto.setRolProducto(rol);
                java.sql.Date fecha = java.sql.Date.valueOf(fechaProducto);
                producto.setFechaProducto(fecha);
                productosDao.guardarProducto(producto);
            } else {
                Productos descripcionExistente = productosDao.obtenerProductoPorDescripcion(producto.getDescripcionProducto());
                if (descripcionExistente != null) {
                    String mensaje = "La descripción ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/admin/productos/" + producto.getIdProducto() + "/editar");
                    return "mensaje-error";
                }
                producto.setCategoriaProducto(categoria);
                producto.setMarcaProducto(marca);
                producto.setRolProducto(rol);
                java.sql.Date fecha = java.sql.Date.valueOf(fechaProducto);
                producto.setFechaProducto(fecha);
                productosDao.guardarProducto(producto);
            }
            return "redirect:/admin/productos";
        } catch (Exception e) {
            String mensaje = "Error al guardar el producto: " + e.getMessage();
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/admin/productos/nuevo");
            return "mensaje-error";
        }
    }

    @GetMapping("/admin/productos/{idProducto}/editar")
    public String mostrarFormularioEditarProducto(@PathVariable("idProducto") Long idProducto, Model model) {
        Productos producto = productosDao.obtenerProducto(idProducto);
        if (producto != null) {
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriasDao.listarCategorias());
        	model.addAttribute("marcas", marcasDao.listarMarcas());
        	model.addAttribute("roles", rolesDao.listarRoles());
            model.addAttribute("boton", "Actualizar");
            model.addAttribute("modo", "edicion");
            return "formulario-producto-admin";
        }
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/productos/{idProducto}/eliminar")
    public String eliminarProducto(@PathVariable("idProducto") Long idProducto) {
        productosDao.eliminarProducto(idProducto);
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/productos/exportar-pdf")
    public ResponseEntity<byte[]> exportarPdf() {
        List<Productos> listaProductos = productosDao.listarProductos();
        ByteArrayInputStream pdfBytesStream = pdfGenerator.generatePdf(listaProductos);
        byte[] pdfBytes = pdfBytesStream.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "productos.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/admin/productos/exportar-excel")
    public ResponseEntity<byte[]> exportarExcel() {
        List<Productos> listaProductos = productosDao.listarProductos();
        ByteArrayInputStream excelBytesStream = excelGenerator.generateExcel(listaProductos);
        byte[] excelBytes = excelBytesStream.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "productos.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}