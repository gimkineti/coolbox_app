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
        // Verificar si hay errores de validación en el formulario
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Usuario");
            model.addAttribute("roles", rolesDao.listarRoles());
            return "formulario-usuario";
        }

        try {
            Roles rol = rolesDao.obtenerRol(usuario.getRolUsuario().getIdRol());

            // Verificar si es un usuario nuevo o un usuario existente
            if (usuario.getIdUsuario() == null) {
                // Es un nuevo usuario, verificar si ya existe por nombre o email
                Usuarios usuarioExistentePorNombre = usuarioDao.obtenerUsuarioPorNombre(usuario.getNombreUsuario());
                Usuarios usuarioExistentePorEmail = usuarioDao.obtenerUsuarioPorEmail(usuario.getEmailUsuario());

                if (usuarioExistentePorNombre != null && usuarioExistentePorEmail != null) {
                    String mensaje = "El usuario y el email ya existen";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/usuarios/nuevo");
                    return "mensaje-error";
                } else if (usuarioExistentePorNombre != null) {
                    String mensaje = "El usuario ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/usuarios/nuevo");
                    return "mensaje-error";
                } else if (usuarioExistentePorEmail != null) {
                    String mensaje = "El email ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/usuarios/nuevo");
                    return "mensaje-error";
                }

                usuario.setRolUsuario(rol);
                usuarioDao.guardarUsuario(usuario);
            } else {
                // Es un usuario existente, verificar si ya existe por nombre o email
                Usuarios usuarioExistentePorNombre = usuarioDao.obtenerUsuarioPorNombre(usuario.getNombreUsuario());
                Usuarios usuarioExistentePorEmail = usuarioDao.obtenerUsuarioPorEmail(usuario.getEmailUsuario());

                if (usuarioExistentePorNombre != null && usuarioExistentePorEmail != null) {
                    String mensaje = "El usuario y el email ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/usuarios/" + usuario.getIdUsuario() + "/editar");
                    return "mensaje-error";
                } else if (usuarioExistentePorNombre != null) {
                    String mensaje = "El usuario ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/usuarios/" + usuario.getIdUsuario() + "/editar");
                    return "mensaje-error";
                } else if (usuarioExistentePorEmail != null) {
                    String mensaje = "El email ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/usuarios/" + usuario.getIdUsuario() + "/editar");
                    return "mensaje-error";
                }

                usuario.setRolUsuario(rol);
                usuarioDao.guardarUsuario(usuario);
            }

            return "redirect:/usuarios";
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
