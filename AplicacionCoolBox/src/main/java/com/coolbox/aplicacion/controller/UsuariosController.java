package com.coolbox.aplicacion.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coolbox.aplicacion.dao.IUsuarioDao;
import com.coolbox.aplicacion.entity.Usuarios;

@Controller
public class UsuariosController {
	
	@Autowired
    IUsuarioDao usuarioDao;
	
	@GetMapping("/usuarios")
        public String listarUsuarios(Model model) {
            List<Usuarios> usuarios = usuarioDao.listarUsuarios();
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("titulo", "Crud de Usuarios");
            return "listar-usuarios";
}
	
	@GetMapping(value = "/usuarios/nuevo")
	public String mostrarFormularioNuevoUsuario(Model model) {
		model.addAttribute("usuario", new Usuarios());
		model.addAttribute("titulo", "Crear Nuevo Usuario");
		return "formulario-usuario";
	}
	
	@PostMapping(value = "/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuarios usuario, Model model) {
        Usuarios usuarioExistenteNombre = usuarioDao.obtenerUsuarioPorNombre(usuario.getNombreUsuario());
        Usuarios usuarioExistenteEmail = usuarioDao.obtenerUsuarioPorEmail(usuario.getEmailUsuario());

        if (usuarioExistenteNombre != null && usuarioExistenteEmail != null) {
            String mensaje = "El usuario y el email ya existen";
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/usuarios/nuevo");
            return "mensaje-error";
        } else if (usuarioExistenteNombre != null) {
            String mensaje = "El usuario ya existe";
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/usuarios/nuevo");
            return "mensaje-error";
        } else if (usuarioExistenteEmail != null) {
            String mensaje = "El email ya existe";
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/usuarios/nuevo");
            return "mensaje-error";
        } else {
            usuarioDao.guardarUsuario(usuario);
            return "redirect:/usuarios";
        }
    }
    
    @GetMapping(value = "/usuarios/{idUsuario}/editar")
    public String mostrarFormularioEditarUusuario(@PathVariable("idUsuario") Long idUsuario, Model model) {
    	Usuarios usuario = usuarioDao.obtenerUsuario(idUsuario);
    	if (usuario != null) {
    		model.addAttribute("titulo", "Editar Usuario");
    		model.addAttribute("usuario", usuario);
    		return "formulario-usuario";
    	}
    	return "redirect:/usuarios/listar";
    }
    
    @GetMapping(value = "usuarios/{idUsuario}/eliminar")
    public String eliminarUsuario(@PathVariable(value = "idUsuario") Long idUsuario) {
    	usuarioDao.eliminarUsuario(idUsuario);
    	return "redirect:/usuarios";
    }
}
