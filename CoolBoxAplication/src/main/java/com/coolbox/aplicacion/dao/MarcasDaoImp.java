package com.coolbox.aplicacion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.coolbox.aplicacion.entity.Marcas;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class MarcasDaoImp implements IMarcasDao {
	
	@PersistenceContext
    private EntityManager entityManager;

	@Override
    @Transactional
    public List<Marcas> listarMarcas() {
        return entityManager.createQuery("SELECT m FROM Marcas m ORDER BY m.idMarca", Marcas.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void guardarMarca(Marcas marca) {
        entityManager.merge(marca);
    }

    @Override
    @Transactional
    public Marcas obtenerMarca(Long idMarca) {
        return entityManager.find(Marcas.class, idMarca);
    }

    @Override
    @Transactional
    public void eliminarMarca(Long idMarca) {
        Marcas marca = obtenerMarca(idMarca);
        if (marca != null) {
            entityManager.remove(marca);
        }
    }
    
    @Override
    @Transactional
    public Marcas obtenerMarcaPorNombre(String nombreMarca) {
    	try {
			return entityManager.createQuery("select m from Marcas m where m.nombreMarca = :nombreMarca", Marcas.class)
					.setParameter("nombreMarca", nombreMarca)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
    }
}