package com.coolbox.aplicacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlmacenController {
    
    @GetMapping(value="/home/almacen")
    public String login(Model m){
        return "almacen";
    }
    
}
