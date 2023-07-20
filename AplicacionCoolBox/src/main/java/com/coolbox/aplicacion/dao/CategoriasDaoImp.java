package com.coolbox.aplicacion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.coolbox.aplicacion.entity.Categorias;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CategoriasDaoImp implements ICategoriasDao {
	
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Categorias> listarCategorias() {
        return entityManager.createQuery("SELECT c FROM Categorias c", Categorias.class).getResultList();
    }

    @Override
    @Transactional
    public void guardarCategoria(Categorias categoria) {
        entityManager.persist(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Categorias obtenerCategoria(Long idCategoria) {
        return entityManager.find(Categorias.class, idCategoria);
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long idCategoria) {
        Categorias categoria = obtenerCategoria(idCategoria);
        if (categoria != null) {
            entityManager.remove(categoria);
        }
    }
    
    @Override
    @Transactional
    public Categorias obtenerCategoriaPorNombre(String nombreCategoria) {
    	try {
			return entityManager.createQuery("select c from Categorias c where c.nombreCategoria = :nombreCategoria", Categorias.class)
					.setParameter("nombreCategoria", nombreCategoria)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
    }
}
