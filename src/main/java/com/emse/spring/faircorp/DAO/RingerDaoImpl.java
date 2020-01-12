package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Ringer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RingerDaoImpl implements RingerDaoCustom {
    @Autowired
    private RingerDao ringerDao;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Ringer findById(Long id) {
        String jpql = "select ringer from Ringer ringer where ringer.id = :value";
        return em.createQuery(jpql, Ringer.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    @Override
    public Ringer findByRoomId(Long id) {
        String jpql = "select ringer from Ringer ringer where ringer.room.id = :value";
        return em.createQuery(jpql, Ringer.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    @Override
    public void updateRinger(Ringer ringer) {
        em.merge(ringer);
    }

    @Override
    public void removeRingerFromPreviousRoom(Ringer ringer) {
        ringer.setRoom(null);
        em.merge(ringer);
    }
}
