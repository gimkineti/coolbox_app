package com.coolbox.aplicacion.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "marcas")
public class Marcas implements Serializable{
	private static final Long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_marca")
	private Long idMarca;
	
	@Column(name = "nombre_marca")
	private String nombreMarca;
	
	public Marcas() {}

	public Marcas(Long idMarca, String nombreMarca) {
		super();
		this.idMarca = idMarca;
		this.nombreMarca = nombreMarca;
	}

	public Long getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(Long idMarca) {
		this.idMarca = idMarca;
	}

	public String getNombreMarca() {
		return nombreMarca;
	}

	public void setNombreMarca(String nombreMarca) {
		this.nombreMarca = nombreMarca;
	}

	public static Long getSerialversionuid() {
		return serialVersionUID;
	}
}
