package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Productos;

public interface IProductosDao {
	
	public List<Productos> listarProductos();
	
	void guardarProducto(Productos producto);
	
	public Productos obtenerProducto(Long idProducto);
	
	void eliminarProducto(Long idProducto);
	
	public Productos obtenerProductoPorDescripcion(String descripcionProducto);
}
