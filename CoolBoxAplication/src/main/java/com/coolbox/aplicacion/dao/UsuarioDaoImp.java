package com.coolbox.aplicacion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.coolbox.aplicacion.entity.Usuarios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class UsuarioDaoImp implements IUsuarioDao {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
    @Transactional(readOnly = true)
	public List<Usuarios> listarUsuarios() {
        TypedQuery<Usuarios> query = entityManager.createQuery(
                "SELECT u FROM Usuarios u JOIN FETCH u.rolUsuario", Usuarios.class);
        return query.getResultList();
    }
	
	@Override
	@Transactional
	public void guardarUsuario(Usuarios usuario) {
	    entityManager.merge(usuario);
	}
	
	@Override
    @Transactional
    public Usuarios obtenerUsuario(Long idUsuario) {
        return entityManager.find(Usuarios.class, idUsuario);
    }
	
	@Override
    @Transactional
	public void eliminarUsuario(Long idUsuario) {
		Usuarios usuario = obtenerUsuario(idUsuario);
		if (usuario != null) {
			entityManager.remove(usuario);
		}
	}
	
	@Transactional
    @Override
    public Usuarios buscarUsuario(String usuario, String password) {
		try {
			return entityManager.createQuery("select u from Usuarios u where u.nombreUsuario = :nombreUsuario and u.passwordUsuario = :passwordUsuario", Usuarios.class)
				    .setParameter("nombreUsuario", usuario)
				    .setParameter("passwordUsuario", password)
				    .getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
    @Transactional
    public Usuarios obtenerUsuarioPorNombre(String nombreUsuario) {
        try {
            return entityManager.createQuery("SELECT u FROM Usuarios u WHERE u.nombreUsuario = :nombre", Usuarios.class)
                .setParameter("nombre", nombreUsuario)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
	
	@Override
    @Transactional
    public Usuarios obtenerUsuarioPorEmail(String emailUsuario) {
        try {
            return entityManager.createQuery("SELECT u FROM Usuarios u WHERE u.emailUsuario = :email", Usuarios.class)
                .setParameter("email", emailUsuario)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}