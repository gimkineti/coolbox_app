package com.coolbox.aplicacion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
	
	@Column(name = "marca_producto")
	private Long marcaProducto;
	
	@Column(name = "categoria_producto")
	private Long categoriaProducto;
	
	@Column(name = "precio_venta")
	private BigDecimal precioVenta;
	
	@Column(name = "rol_producto")
	private Long rolRroducto;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "fecha_producto")
    private Date fechaProducto;
	
	public Productos() {}

	public Productos(Long idProducto, String descripcionProducto, int stockProducto, Long marcaProducto,
			Long categoriaProducto, BigDecimal precioVenta, Long rolRroducto, Date fechaProducto) {
		super();
		this.idProducto = idProducto;
		this.descripcionProducto = descripcionProducto;
		this.stockProducto = stockProducto;
		this.marcaProducto = marcaProducto;
		this.categoriaProducto = categoriaProducto;
		this.precioVenta = precioVenta;
		this.rolRroducto = rolRroducto;
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

	public Long getMarcaProducto() {
		return marcaProducto;
	}

	public void setMarcaProducto(Long marcaProducto) {
		this.marcaProducto = marcaProducto;
	}

	public Long getCategoriaProducto() {
		return categoriaProducto;
	}

	public void setCategoriaProducto(Long categoriaProducto) {
		this.categoriaProducto = categoriaProducto;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Long getRolRroducto() {
		return rolRroducto;
	}

	public void setRolRroducto(Long rolRroducto) {
		this.rolRroducto = rolRroducto;
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
