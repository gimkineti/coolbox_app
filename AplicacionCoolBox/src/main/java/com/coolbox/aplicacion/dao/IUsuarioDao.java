package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Usuarios;

public interface IUsuarioDao {
	
	// listar usuarios
	public List<Usuarios> listarUsuarios();
	
	// registrar usuarios
	void guardarUsuario(Usuarios usario);
	
	// editar usuario
	public Usuarios obtenerUsuario(Long idUsuario);
	
	// eliminar usuario
	void eliminarUsuario(Long idUsuario);
	
	// buscar usuario por nombre y password
	public Usuarios buscarUsuario(String usuario, String pass);
	
	// buscar email o usuario
	Usuarios obtenerUsuarioPorNombre(String nombreUsuario);
	
	Usuarios obtenerUsuarioPorEmail(String emailUsuario);
}
