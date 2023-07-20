package com.coolbox.aplicacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coolbox.aplicacion.dao.ICategoriasDao;
import com.coolbox.aplicacion.dao.IMarcasDao;
import com.coolbox.aplicacion.dao.IProductosDao;
import com.coolbox.aplicacion.dao.IRolesDao;
import com.coolbox.aplicacion.dao.IUsuarioDao;
import com.coolbox.aplicacion.entity.Usuarios;

@Controller
public class EmpleadoController {
    
    @Autowired
    private IMarcasDao marcasDao;

    @Autowired
    private ICategoriasDao categoriaDao;

    @Autowired
    private IProductosDao productosDao;
    
    @Autowired
    private IRolesDao rolesDao;
    
    @Autowired
    IUsuarioDao usuarioDao;

    @GetMapping(value="/home/empleado")
    public String login(Model m){
        return "empleado";
    }
	
	@GetMapping("/usuarios/empleado")
        public String listarUsuarios(Model model) {
            List<Usuarios> usuarios = usuarioDao.listarUsuarios();
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("titulo", "Lista de Usuarios");
            return "listar-usuarios-empleado";
    }

    @GetMapping("/roles/empleado")
    public String listarRoles(Model model) {
        model.addAttribute("roles", rolesDao.listarRoles());
        model.addAttribute("titulo", "Lista de Roles");
        return "listar-roles-empleado";
    }

    @GetMapping("/productos/empleado")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productosDao.listarProductos());
        model.addAttribute("titulo", "Lista de Productos");
        return "listar-productos-empleado";
    }
	
	@GetMapping(value = "/categorias/empleado")
	public String listarCategorias(Model model) {
		model.addAttribute("categorias", categoriaDao.listarCategorias());
		model.addAttribute("titulo", "Lista de Categorias");
		return "listar-categorias-empleado";
	}

    @GetMapping("/marcas/empleado")
    public String listarMarcas(Model model) {
        model.addAttribute("marcas", marcasDao.listarMarcas());
        model.addAttribute("titulo", "Lista de Marcas");
        return "listar-marcas-empleado";
    }
}
