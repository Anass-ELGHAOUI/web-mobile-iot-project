package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Light;
import com.emse.spring.faircorp.model.Room;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class RoomDaoImpl implements RoomDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Room findRoomByName(String roomName) {
        String jpql = "select room from Room room where room.name = :value";
        return em.createQuery(jpql, Room.class)
                .setParameter("value", roomName)
                .getSingleResult();
    }

    @Override
    public Room findRoomById(Long id) {
        String jpql = "select room from Room room where room.id = :value";
        return em.createQuery(jpql, Room.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    @Override
    public List<Light> findLightsByRoomId(Long roomId) {
        String jpql = "select lights from Light lights where lights.room.id = :value";
        return em.createQuery(jpql, Light.class)
                .setParameter("value", roomId)
                .getResultList();
    }

    @Override
    public void updateRoom(Room room) {
        em.merge(room);
    }
}
