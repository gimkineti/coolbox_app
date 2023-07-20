package com.coolbox.aplicacion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.coolbox.aplicacion.entity.Productos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductosDaoImp implements IProductosDao{
	
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Productos> listarProductos() {
        return entityManager.createQuery("SELECT p FROM Productos p", Productos.class).getResultList();
    }

    @Override
    @Transactional
    public void guardarProducto(Productos producto) {
        entityManager.persist(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Productos obtenerProducto(Long idProducto) {
        return entityManager.find(Productos.class, idProducto);
    }

    @Override
    @Transactional
    public void actualizarProducto(Productos producto) {
        try {
            entityManager.merge(producto);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
        }
    }

    @Override
    @Transactional
    public void eliminarProducto(Long idProducto) {
        Productos producto = obtenerProducto(idProducto);
        if (producto != null) {
            entityManager.remove(producto);
        }
    }
}
