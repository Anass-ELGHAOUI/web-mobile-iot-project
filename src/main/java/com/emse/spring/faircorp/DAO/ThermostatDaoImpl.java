package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Thermostat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ThermostatDaoImpl implements ThermostatDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Thermostat findById(Long id) {
        String jpql = "select thermostat from Thermostat thermostat where thermostat.id = :value";
        return em.createQuery(jpql, Thermostat.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    @Override
    public Thermostat findByRoomId(Long id) {
        String jpql = "select thermostat from Thermostat thermostat where thermostat.room.id = :value";
        return em.createQuery(jpql, Thermostat.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    @Override
    public void updateThermostat(Thermostat thermostat) {
        em.merge(thermostat);
    }

    @Override
    public void removeThermostatFromPreviousRoom(Thermostat thermostat) {
        thermostat.setRoom(null);
        em.merge(thermostat);
    }
}
