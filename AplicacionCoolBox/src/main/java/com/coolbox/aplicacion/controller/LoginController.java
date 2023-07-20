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
	
	@GetMapping(value="/login")
    public String login(Model m){
        return "login";
    }
	
	@PostMapping(value="/login")
	public ModelAndView logExitoso(@RequestParam(name="nombreUsuario") String nombreUsuario,
								@RequestParam(name="passwordUsuario") String passwordUsuario) {
		try {
			Usuarios usuario = usuarioDao.buscarUsuario(nombreUsuario, passwordUsuario);
			if (usuario == null) {
				ModelAndView modelAndView = new ModelAndView("mensaje-error");
				modelAndView.addObject("titulo", "Login no exitoso");
				modelAndView.addObject("mensaje", "El usuario y la contraseña no coinciden");
				modelAndView.addObject("direccion", "/login");
				return modelAndView;
			} else {
				return new ModelAndView(new RedirectView("/home/dashboard"));
			}
		} catch (NoResultException e) {
			ModelAndView modelAndView = new ModelAndView("mensaje-error");
			modelAndView.addObject("titulo", "Login no exitoso");
			modelAndView.addObject("mensaje", "El usuario y la contraseña no coinciden");
			modelAndView.addObject("direccion", "/login");
			return modelAndView;
		}
	}
	
	@GetMapping(value = "/login-no-exitoso")
	public String logFallido(Model m) {
		return "login-no-exitoso";
	}
}
