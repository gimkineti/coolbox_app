package com.coolbox.aplicacion.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuarios implements Serializable{
	private static final Long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long idUsuario;
	
	@Column(name = "rol_usuario")
	private Long rolUsuario;
	
	@Column(name = "nombre_usuario")
	private String nombreUsuario;
	
	@Column(name = "password_usuario")
	private String passwordUsuario;
	
	@Column(name = "email_usuario")
	private String emailUsuario;
	
	public Usuarios(Long idUsuario, Long rolUsuario, String nombreUsuario, String passwordUsuario,
			String emailUsuario) {
		super();
		this.idUsuario = idUsuario;
		this.rolUsuario = rolUsuario;
		this.nombreUsuario = nombreUsuario;
		this.passwordUsuario = passwordUsuario;
		this.emailUsuario = emailUsuario;
	}

	public Usuarios() {}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(Long rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getPasswordUsuario() {
		return passwordUsuario;
	}

	public void setPasswordUsuario(String passwordUsuario) {
		this.passwordUsuario = passwordUsuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public static Long getSerialversionuid() {
		return serialVersionUID;
	}
}
