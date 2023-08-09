package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Roles;

public interface IRolesDao {
	
	List<Roles> listarRoles();

    void guardarRol(Roles rol);

    Roles obtenerRol(Long idRol);

    void eliminarRol(Long idRol);
    
    Roles obtenerRolPorNombre(String nombreRol);
    
    public List<Roles> listarRolesAlmacen();
}