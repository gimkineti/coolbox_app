package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Categorias;
import com.coolbox.aplicacion.entity.Marcas;
import com.coolbox.aplicacion.entity.Productos;

public interface IProductosDao {
	
	public List<Productos> listarProductos();

	public List<Productos> buscarProductosPorDescripcion(String descripcionProducto);

	public List<Productos> buscarProductosPorMarca(Marcas marca);

	public List<Productos> buscarProductosPorCategoria(Categorias categorias);
	
	void guardarProducto(Productos producto);
	
	public Productos obtenerProducto(Long idProducto);
	
	void eliminarProducto(Long idProducto);
	
	public Productos obtenerProductoPorDescripcion(String descripcionProducto);
}