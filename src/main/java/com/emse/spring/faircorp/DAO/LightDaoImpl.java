package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Light;
import com.emse.spring.faircorp.model.Status;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class LightDaoImpl implements LightDaoCustom {
    @Autowired
    private LightDao lightDao;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Light> findOnLights() {
        String jpql = "select light from Light light where light.status = :value";
        return em.createQuery(jpql, Light.class)
                .setParameter("value", Status.ON)
                .getResultList();
    }

    @Override
    public Light findById(Long id) {
        String jpql = "select light from Light light where light.id = :value";
        return em.createQuery(jpql, Light.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    @Override
    public void updateLight(Light light) {
        em.merge(light);
    }
}
