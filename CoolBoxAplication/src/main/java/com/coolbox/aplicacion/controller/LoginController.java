package com.coolbox.aplicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.coolbox.aplicacion.dao.IUsuarioDao;
import com.coolbox.aplicacion.entity.Usuarios;

import jakarta.persistence.NoResultException;

@Controller
public class LoginController {
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@GetMapping(value={"/"})
    public String login(Model m){
        return "login";
    }
	
	@PostMapping(value="/")
	public ModelAndView logExitoso(@RequestParam(name="nombreUsuario") String nombreUsuario,
									@RequestParam(name="passwordUsuario") String passwordUsuario) {
		if (nombreUsuario.isEmpty() || passwordUsuario.isEmpty()) {
			ModelAndView modelAndView = new ModelAndView("mensaje-error");
			modelAndView.addObject("titulo", "Login No Exitoso");
			modelAndView.addObject("mensaje", "Debes ingresar un nombre de usuario y una contraseña válida");
			modelAndView.addObject("direccion", "/");
			return modelAndView;
		}

		try {
			Usuarios usuario = usuarioDao.buscarUsuario(nombreUsuario, passwordUsuario);
			if (usuario == null) {
				ModelAndView modelAndView = new ModelAndView("mensaje-error");
				modelAndView.addObject("titulo", "Login No Exitoso");
				modelAndView.addObject("mensaje", "El Usuario Y La Contraseña No Coinciden");
				modelAndView.addObject("direccion", "/");
				return modelAndView;
			} else {
				String nombreRol = usuario.getRolUsuario().getNombreRol();

				if ("ADMINISTRADOR".equals(nombreRol)) {
					return new ModelAndView(new RedirectView("/admin/home"));
				} else if ("ALMACEN".equals(nombreRol)) {
					return new ModelAndView(new RedirectView("/home/almacen"));
				} else if ("EMPLEADO".equals(nombreRol)) {
					return new ModelAndView(new RedirectView("/home/empleado"));
				} else {
					ModelAndView modelAndView = new ModelAndView("mensaje-error");
					modelAndView.addObject("titulo", "Login No Exitoso");
					modelAndView.addObject("mensaje", "El Rol del Usuario no tiene una redirección definida");
					modelAndView.addObject("direccion", "/");
					return modelAndView;
				}
			}
		} catch (NoResultException e) {
			ModelAndView modelAndView = new ModelAndView("mensaje-error");
			modelAndView.addObject("titulo", "Login No Exitoso");
			modelAndView.addObject("mensaje", "El Usuario Y La Contraseña No Coinciden");
			modelAndView.addObject("direccion", "/");
			return modelAndView;
		}
	}
}