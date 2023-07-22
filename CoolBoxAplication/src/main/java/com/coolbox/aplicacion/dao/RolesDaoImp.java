package com.coolbox.aplicacion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.coolbox.aplicacion.entity.Roles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class RolesDaoImp implements IRolesDao {
	
	@PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Roles> listarRoles() {
        return entityManager.createQuery("SELECT r FROM Roles r", Roles.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void guardarRol(Roles rol) {
        entityManager.persist(rol);
    }

    @Override
    @Transactional
    public Roles obtenerRol(Long idRol) {
        return entityManager.find(Roles.class, idRol);
    }

    @Override
    @Transactional
    public void eliminarRol(Long idRol) {
        Roles rol = obtenerRol(idRol);
        if (rol != null) {
            entityManager.remove(rol);
        }
    }
    
    @Override
    @Transactional
    public Roles obtenerRolPorNombre(String nombreRol) {
    	try {
			return entityManager.createQuery("select r from Roles r where r.nombreRol = :nombreRol", Roles.class)
					.setParameter("nombreRol", nombreRol)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Roles> listarRolesAlmacen() {
        TypedQuery<Roles> query = entityManager.createQuery(
            "SELECT r FROM Roles r WHERE r.nombreRol = 'ALMACEN'", Roles.class);
        return query.getResultList();
    }

}
