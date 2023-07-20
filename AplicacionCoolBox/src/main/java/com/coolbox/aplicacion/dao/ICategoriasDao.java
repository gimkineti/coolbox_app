package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Categorias;

public interface ICategoriasDao {
	
	List<Categorias> listarCategorias();
	
	void guardarCategoria(Categorias categoria);
	
	Categorias obtenerCategoria(Long idCategoria);
	
	void eliminarCategoria(Long idCategoria);
	
	Categorias obtenerCategoriaPorNombre(String nombreCategoria);

}
