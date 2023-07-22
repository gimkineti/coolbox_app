package com.coolbox.aplicacion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "productos")
public class Productos implements Serializable{
	private static final Long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_producto")
	private Long idProducto;
	
	@Column(name = "descripcion_producto")
	private String descripcionProducto;
	
	@Column(name = "stock_producto")
	private int stockProducto;
	
	@ManyToOne
	@JoinColumn(name = "categoria_producto")
	private Categorias categoriaProducto;
	
	@Transient
	private String nombreCategoria;
	
	@ManyToOne
	@JoinColumn(name = "marca_producto")
	private Marcas marcaProducto;
	
	@Transient
	private String nombreMarca;
	
	@Column(name = "precio_venta")
	private BigDecimal precioVenta;
	
	@ManyToOne
	@JoinColumn(name = "rol_producto")
	private Roles rolProducto;
	
	@Transient
	private String nombreRol;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "fecha_producto")
    private Date fechaProducto;
	
	public Productos() {}

	public Productos(Long idProducto, String descripcionProducto, int stockProducto, Categorias categoriaProducto,
			String nombreCategoria, Marcas marcaProducto, String nombreMarca, BigDecimal precioVenta, Roles rolProducto,
			String nombreRol, Date fechaProducto) {
		super();
		this.idProducto = idProducto;
		this.descripcionProducto = descripcionProducto;
		this.stockProducto = stockProducto;
		this.categoriaProducto = categoriaProducto;
		this.nombreCategoria = nombreCategoria;
		this.marcaProducto = marcaProducto;
		this.nombreMarca = nombreMarca;
		this.precioVenta = precioVenta;
		this.rolProducto = rolProducto;
		this.nombreRol = nombreRol;
		this.fechaProducto = fechaProducto;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getDescripcionProducto() {
		return descripcionProducto;
	}

	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}

	public int getStockProducto() {
		return stockProducto;
	}

	public void setStockProducto(int stockProducto) {
		this.stockProducto = stockProducto;
	}

	public Categorias getCategoriaProducto() {
		return categoriaProducto;
	}

	public void setCategoriaProducto(Categorias categoriaProducto) {
		this.categoriaProducto = categoriaProducto;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public Marcas getMarcaProducto() {
		return marcaProducto;
	}

	public void setMarcaProducto(Marcas marcaProducto) {
		this.marcaProducto = marcaProducto;
	}

	public String getNombreMarca() {
		return nombreMarca;
	}

	public void setNombreMarca(String nombreMarca) {
		this.nombreMarca = nombreMarca;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Roles getRolProducto() {
		return rolProducto;
	}

	public void setRolProducto(Roles rolProducto) {
		this.rolProducto = rolProducto;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	public Date getFechaProducto() {
		return fechaProducto;
	}

	public void setFechaProducto(Date fechaProducto) {
		this.fechaProducto = fechaProducto;
	}

	public static Long getSerialversionuid() {
		return serialVersionUID;
	}
}
