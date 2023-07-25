package com.coolbox.aplicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.coolbox.aplicacion.dao.IUsuarioDao;
import com.coolbox.aplicacion.entity.Usuarios;

import jakarta.persistence.NoResultException;

@Controller
public class LoginController {
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@GetMapping(value={"/login", "/"})  // Agregamos la ruta por defecto "/"
    public String login(Model m){
        return "login";
    }
	
	@PostMapping(value="/login")
	public ModelAndView logExitoso(@RequestParam(name="nombreUsuario") String nombreUsuario,
									@RequestParam(name="passwordUsuario") String passwordUsuario) {
		if (nombreUsuario.isEmpty() || passwordUsuario.isEmpty()) {
			ModelAndView modelAndView = new ModelAndView("mensaje-error");
			modelAndView.addObject("titulo", "Login No Exitoso");
			modelAndView.addObject("mensaje", "Debes ingresar un nombre de usuario y una contraseña válida");
			modelAndView.addObject("direccion", "/login");
			return modelAndView;
		}

		try {
			Usuarios usuario = usuarioDao.buscarUsuario(nombreUsuario, passwordUsuario);
			if (usuario == null) {
				ModelAndView modelAndView = new ModelAndView("mensaje-error");
				modelAndView.addObject("titulo", "Login No Exitoso");
				modelAndView.addObject("mensaje", "El Usuario Y La Contraseña No Coinciden");
				modelAndView.addObject("direccion", "/login");
				return modelAndView;
			} else {
				String nombreRol = usuario.getRolUsuario().getNombreRol();

				if ("ADMINISTRADOR".equals(nombreRol)) {
					ModelAndView modelAndView = new ModelAndView("dashboard");
					modelAndView.addObject("rol", usuario.getRolUsuario().getNombreRol());
					modelAndView.addObject("nombreUsuario", usuario.getNombreUsuario());
					modelAndView.addObject("emailUsuario", usuario.getEmailUsuario());
					modelAndView.addObject("mensaje", "Podrás hacer todas las operaciones CRUD.");
					return modelAndView;
				} else if ("ALMACEN".equals(nombreRol)) {
					ModelAndView modelAndView = new ModelAndView("almacen");
					modelAndView.addObject("rol", usuario.getRolUsuario().getNombreRol());
					modelAndView.addObject("nombreUsuario", usuario.getNombreUsuario());
					modelAndView.addObject("emailUsuario", usuario.getEmailUsuario());
					modelAndView.addObject("mensaje", "Podrás agregar y editar registros pero no eliminar.");
					return modelAndView;
				} else if ("EMPLEADO".equals(nombreRol)) {
					ModelAndView modelAndView = new ModelAndView("empleado");
					modelAndView.addObject("rol", usuario.getRolUsuario().getNombreRol());
					modelAndView.addObject("nombreUsuario", usuario.getNombreUsuario());
					modelAndView.addObject("emailUsuario", usuario.getEmailUsuario());
					modelAndView.addObject("mensaje", "Podrás ver la lista de registros pero no hacer ninguna operación.");
					return modelAndView;
				} else {
					// Aquí puedes manejar otras redirecciones o mostrar un mensaje de error si el nombre del rol no coincide con ninguna opción.
					ModelAndView modelAndView = new ModelAndView("mensaje-error");
					modelAndView.addObject("titulo", "Login No Exitoso");
					modelAndView.addObject("mensaje", "El Rol del Usuario no tiene una redirección definida");
					modelAndView.addObject("direccion", "/login");
					return modelAndView;
				}
			}
		} catch (NoResultException e) {
			ModelAndView modelAndView = new ModelAndView("mensaje-error");
			modelAndView.addObject("titulo", "Login No Exitoso");
			modelAndView.addObject("mensaje", "El Usuario Y La Contraseña No Coinciden");
			modelAndView.addObject("direccion", "/login");
			return modelAndView;
		}
	}
}
