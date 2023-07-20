package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Productos;

public interface IProductosDao {
	
	List<Productos> listarProductos();
	
	void guardarProducto(Productos producto);
	
	Productos obtenerProducto(Long idProducto);
	
	void actualizarProducto(Productos producto);
	
	void eliminarProducto(Long idProducto);
}
