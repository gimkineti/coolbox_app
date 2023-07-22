package com.coolbox.aplicacion.dao;

import java.util.List;

import com.coolbox.aplicacion.entity.Usuarios;

public interface IUsuarioDao {
	
	public List<Usuarios> listarUsuarios();
		
	void guardarUsuario(Usuarios usario);
		
	public Usuarios obtenerUsuario(Long idUsuario);
		
	void eliminarUsuario(Long idUsuario);
		
	public Usuarios buscarUsuario(String usuario, String pass);
		
	Usuarios obtenerUsuarioPorNombre(String nombreUsuario);
		
	Usuarios obtenerUsuarioPorEmail(String emailUsuario);
}
