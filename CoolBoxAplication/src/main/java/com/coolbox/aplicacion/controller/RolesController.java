package com.coolbox.aplicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coolbox.aplicacion.dao.IRolesDao;
import com.coolbox.aplicacion.entity.Roles;

@Controller
public class RolesController {
	
	@Autowired
    private IRolesDao rolesDao;

    @GetMapping("/admin/roles")
    public String listarRoles(Model model) {
        model.addAttribute("roles", rolesDao.listarRoles());
        model.addAttribute("titulo", "Crud de Roles");
        return "listar-roles-admin";
    }

    @GetMapping("/admin/roles/nuevo")
    public String mostrarFormularioNuevoRol(Model model) {
        model.addAttribute("rol", new Roles());
        model.addAttribute("titulo", "Crear Nuevo Rol");
        model.addAttribute("boton", "Registrar");
        model.addAttribute("modo", "registro");
        return "formulario-rol-admin";
    }

    @PostMapping("/admin/roles/guardar")
    public String guardarRol(@ModelAttribute("rol") Roles rol, Model model) {
        Roles rolExistente = rolesDao.obtenerRolPorNombre(rol.getNombreRol());

        // Verificar si el rol existente tiene el mismo ID que el rol que se está actualizando
        if (rolExistente != null && !rolExistente.getIdRol().equals(rol.getIdRol())) {
            String mensaje = "El rol ya existe";
            model.addAttribute("titulo", "Error");
            model.addAttribute("mensaje", mensaje);

            if (rol.getIdRol() != null) {
                model.addAttribute("direccion", "/admin/roles/" + rol.getIdRol() + "/editar"); // Si es una edición
            } else {
                model.addAttribute("direccion", "/admin/roles/nuevo");
            }
            return "mensaje-error";
        } else {
            rolesDao.guardarRol(rol);
            return "redirect:/admin/roles"; // Redirigir a la lista de roles
        }
    }

    @GetMapping("/admin/roles/{idRol}/editar")
    public String mostrarFormularioEditarRol(@PathVariable("idRol") Long idRol, Model model) {
        Roles rol = rolesDao.obtenerRol(idRol);
        if (rol != null) {
        	model.addAttribute("titulo", "Editar Rol");
            model.addAttribute("rol", rol);
            model.addAttribute("boton", "Actualizar");
            model.addAttribute("modo", "edicion");
            return "formulario-rol-admin";
        }
        return "redirect:/admin/roles";
    }

    @GetMapping("/admin/roles/{idRol}/eliminar")
    public String eliminarRol(@PathVariable("idRol") Long idRol) {
        rolesDao.eliminarRol(idRol);
        return "redirect:/admin/roles";
    }
}