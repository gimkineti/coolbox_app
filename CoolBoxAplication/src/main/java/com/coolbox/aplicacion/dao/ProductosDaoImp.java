package com.coolbox.aplicacion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.coolbox.aplicacion.entity.Categorias;
import com.coolbox.aplicacion.entity.Marcas;
import com.coolbox.aplicacion.entity.Productos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class ProductosDaoImp implements IProductosDao{
	
	@PersistenceContext
    private EntityManager entityManager;

	@Override
    @Transactional(readOnly = true)
    public List<Productos> listarProductos() {
        TypedQuery<Productos> query = entityManager.createQuery(
            "SELECT p FROM Productos p " +
            "JOIN FETCH p.categoriaProducto " +
            "JOIN FETCH p.marcaProducto " +
            "JOIN FETCH p.rolProducto " +
            "ORDER BY p.idProducto", Productos.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void guardarProducto(Productos producto) {
        entityManager.merge(producto);
    }

    @Override
    @Transactional
    public Productos obtenerProducto(Long idProducto) {
        return entityManager.find(Productos.class, idProducto);
    }

    @Override
    @Transactional
    public void eliminarProducto(Long idProducto) {
        Productos producto = obtenerProducto(idProducto);
        if (producto != null) {
            entityManager.remove(producto);
        }
    }
    
    @Override
    @Transactional
    public Productos obtenerProductoPorDescripcion(String descripcionProducto) {
        try {
            return entityManager.createQuery("SELECT p from Productos p WHERE p.descripcionProducto = :descripcion", Productos.class)
                    .setParameter("descripcion", descripcionProducto)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Productos> buscarProductosPorDescripcion(String descripcionProducto) {
        TypedQuery<Productos> query = entityManager.createQuery(
            "SELECT p FROM Productos p " +
            "JOIN FETCH p.categoriaProducto " +
            "JOIN FETCH p.marcaProducto " +
            "JOIN FETCH p.rolProducto " +
            "WHERE upper(p.descripcionProducto) LIKE :descripcion", Productos.class);
        query.setParameter("descripcion", "%" + descripcionProducto.toUpperCase() + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Productos> buscarProductosPorMarca(Marcas marca) {
        TypedQuery<Productos> query = entityManager.createQuery(
            "SELECT p FROM Productos p " +
            "JOIN FETCH p.categoriaProducto " +
            "JOIN FETCH p.marcaProducto " +
            "JOIN FETCH p.rolProducto " +
            "WHERE p.marcaProducto = :marca", Productos.class);
        query.setParameter("marca", marca);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Productos> buscarProductosPorCategoria(Categorias categorias) {
        TypedQuery<Productos> query = entityManager.createQuery(
            "SELECT p FROM Productos p " +
            "JOIN FETCH p.categoriaProducto " +
            "JOIN FETCH p.marcaProducto " +
            "JOIN FETCH p.rolProducto " +
            "WHERE p.categoriaProducto = :categorias", Productos.class);
        query.setParameter("categorias", categorias);
        return query.getResultList();
    }
}