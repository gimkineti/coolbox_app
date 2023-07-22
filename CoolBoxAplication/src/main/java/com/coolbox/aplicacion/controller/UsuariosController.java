package com.coolbox.aplicacion.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coolbox.aplicacion.dao.IRolesDao;
import com.coolbox.aplicacion.dao.IUsuarioDao;
import com.coolbox.aplicacion.entity.Roles;
import com.coolbox.aplicacion.entity.Usuarios;

import jakarta.validation.Valid;

@Controller
public class UsuariosController {
	
	@Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IRolesDao rolesDao;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuarios> usuarios = usuarioDao.listarUsuarios();

        for (Usuarios usuario : usuarios) {
            usuario.setNombreRol(usuario.getRolUsuario().getNombreRol());
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Crud de Usuarios");
        return "listar-usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuarios());
        model.addAttribute("roles", rolesDao.listarRoles());
        model.addAttribute("titulo", "Crear Nuevo Usuario");
        return "formulario-usuario";
    }

    @PostMapping(value = "/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") @Valid Usuarios usuario, BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Usuario");
            model.addAttribute("roles", rolesDao.listarRoles());
            return "formulario-usuario";
        }

        try {
            Roles rol = rolesDao.obtenerRol(usuario.getRolUsuario().getIdRol());
            Usuarios usuarioExistente = usuarioDao.obtenerUsuarioPorNombre(usuario.getNombreUsuario());
            Usuarios emailExistente = usuarioDao.obtenerUsuarioPorEmail(usuario.getEmailUsuario());
            if (rol == null) {
                String mensaje = "El rol seleccionado no existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/usuarios/nuevo");
                return "mensaje-error";
            }
            else if (usuarioExistente != null && emailExistente != null) {
                String mensaje = "El usuario y el email ya existen";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/usuarios/nuevo");
                return "mensaje-error";
            }
            else if (usuarioExistente != null) {
            	String mensaje = "El usuario ya existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/usuarios/nuevo");
                return "mensaje-error";
            } 
            else if (emailExistente != null) {
            	String mensaje = "El email ya existe";
                model.addAttribute("titulo", "Error");
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("direccion", "/usuarios/nuevo");
                return "mensaje-error";
            }
            else {
            	usuario.setRolUsuario(rol);
                usuarioDao.guardarUsuario(usuario);
                return "redirect:/usuarios";
            }
        } catch (Exception e) {
            String mensaje = "Error al guardar el usuario: " + e.getMessage();
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/usuarios/nuevo");
            return "mensaje-error";
        }
    }

    @GetMapping(value = "/usuarios/{idUsuario}/editar")
    public String mostrarFormularioEditarUsuario(@PathVariable("idUsuario") Long idUsuario, Model model) {
        Usuarios usuario = usuarioDao.obtenerUsuario(idUsuario);
        if (usuario != null) {
            model.addAttribute("titulo", "Editar Usuario");
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", rolesDao.listarRoles());
            return "formulario-usuario";
        }
        return "redirect:/usuarios";
    }

    @GetMapping(value = "usuarios/{idUsuario}/eliminar")
    public String eliminarUsuario(@PathVariable(value = "idUsuario") Long idUsuario) {
        usuarioDao.eliminarUsuario(idUsuario);
        return "redirect:/usuarios";
    }
}
