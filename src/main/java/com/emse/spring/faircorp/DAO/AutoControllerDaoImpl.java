package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.AutoController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AutoControllerDaoImpl implements AutoControllerDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public AutoController findAutoLightById(Long id) {
        String jpql = "select autoLight from AutoController autoLight where autoLight.id = :value";
        return em.createQuery(jpql, AutoController.class)
                .setParameter("value", id)
                .getSingleResult();
    }
}
