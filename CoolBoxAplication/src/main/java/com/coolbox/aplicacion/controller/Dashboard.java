package com.coolbox.aplicacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Dashboard {
	@RequestMapping("/home/dashboard")
	public String home(Model m) {
		return "dashboard";
	}
}
