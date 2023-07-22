package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Marcas;

public interface IMarcasDao {
	
	List<Marcas> listarMarcas();

    void guardarMarca(Marcas marca);

    Marcas obtenerMarca(Long idMarca);

    void eliminarMarca(Long idMarca);
    
    Marcas obtenerMarcaPorNombre(String nombreMarca);
}
