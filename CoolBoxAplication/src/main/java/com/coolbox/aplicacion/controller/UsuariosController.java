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

    @GetMapping("/admin/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuarios> usuarios = usuarioDao.listarUsuarios();

        for (Usuarios usuario : usuarios) {
            usuario.setNombreRol(usuario.getRolUsuario().getNombreRol());
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Crud de Usuarios");
        return "listar-usuarios-admin";
    }

    @GetMapping("/admin/usuarios/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuarios());
        model.addAttribute("roles", rolesDao.listarRoles());
        model.addAttribute("titulo", "Crear Nuevo Usuario");
        model.addAttribute("boton", "Registrar");
        model.addAttribute("modo", "registro");
        return "formulario-usuario-admin";
    }

    @PostMapping(value = "/admin/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") @Valid Usuarios usuario, BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Usuario");
            model.addAttribute("roles", rolesDao.listarRoles());
            return "formulario-usuario-admin";
        }

        try {
            Roles rol = rolesDao.obtenerRol(usuario.getRolUsuario().getIdRol());

            if (usuario.getIdUsuario() != null) {
                // Verificar si el nombre de usuario ya existe (excepto para el mismo usuario)
                Usuarios usuarioExistentePorNombre = usuarioDao.obtenerUsuarioPorNombre(usuario.getNombreUsuario());
                if (usuarioExistentePorNombre != null && !usuarioExistentePorNombre.getIdUsuario().equals(usuario.getIdUsuario())) {
                    String mensaje = "El usuario ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/admin/usuarios/" + usuario.getIdUsuario() + "/editar");
                    return "mensaje-error";
                }

                // Verificar si el correo electrónico ya existe (excepto para el mismo usuario)
                Usuarios usuarioExistentePorEmail = usuarioDao.obtenerUsuarioPorEmail(usuario.getEmailUsuario());
                if (usuarioExistentePorEmail != null && !usuarioExistentePorEmail.getIdUsuario().equals(usuario.getIdUsuario())) {
                    String mensaje = "El email ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/admin/usuarios/" + usuario.getIdUsuario() + "/editar");
                    return "mensaje-error";
                }

                // Actualizar el usuario
                usuario.setRolUsuario(rol);
                usuarioDao.guardarUsuario(usuario);
            } else {
                // Verificar si el nombre de usuario y el email ya existen (para un nuevo usuario)
                Usuarios usuarioExistentePorEmail = usuarioDao.obtenerUsuarioPorEmail(usuario.getEmailUsuario());
                Usuarios usuarioExistentePorNombre = usuarioDao.obtenerUsuarioPorNombre(usuario.getNombreUsuario());
                if (usuarioExistentePorNombre != null && usuarioExistentePorEmail != null){
                    String mensaje = "El usuario y el email ya existen";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/admin/usuarios/nuevo");
                    return "mensaje-error";
                }
                
                if (usuarioExistentePorNombre != null) {
                    String mensaje = "El usuario ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/admin/usuarios/nuevo");
                    return "mensaje-error";
                }
            
                if (usuarioExistentePorEmail != null) {
                    String mensaje = "El email ya existe";
                    model.addAttribute("titulo", "Error");
                    model.addAttribute("mensaje", mensaje);
                    model.addAttribute("direccion", "/admin/usuarios/nuevo");
                    return "mensaje-error";
                }

                // Crear un nuevo usuario
                usuario.setRolUsuario(rol);
                usuarioDao.guardarUsuario(usuario);
            }

            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            String mensaje = "Error al guardar el usuario: " + e.getMessage();
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("direccion", "/admin/usuarios/nuevo");
            return "mensaje-error";
        }
    }

    @GetMapping(value = "/admin/usuarios/{idUsuario}/editar")
    public String mostrarFormularioEditarUsuario(@PathVariable("idUsuario") Long idUsuario, Model model) {
        Usuarios usuario = usuarioDao.obtenerUsuario(idUsuario);
        if (usuario != null) {
            model.addAttribute("titulo", "Editar Usuario");
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", rolesDao.listarRoles());
            model.addAttribute("boton", "Actualizar");
            model.addAttribute("modo", "edicion");
            return "formulario-usuario-admin";
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping(value = "/admin/usuarios/{idUsuario}/eliminar")
    public String eliminarUsuario(@PathVariable(value = "idUsuario") Long idUsuario) {
        usuarioDao.eliminarUsuario(idUsuario);
        return "redirect:/admin/usuarios";
    }
}