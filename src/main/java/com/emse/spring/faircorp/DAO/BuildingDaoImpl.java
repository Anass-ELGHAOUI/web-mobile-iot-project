package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Building;
import com.emse.spring.faircorp.model.Light;
import com.emse.spring.faircorp.model.Room;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class BuildingDaoImpl implements BuildingDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Light> findBuildingLights(Long id) {
        String jpql = "select rooms from Room rooms where rooms.building.id = :value";
        List<Room> roomsInBuilding = em.createQuery(jpql, Room.class)
                                       .setParameter("value", id)
                                       .getResultList();

        List<Light> buildingLights = new ArrayList<Light>();
        for (int i = 0; i < roomsInBuilding.size(); i++) {
            String jpql2 = "select light from Light light where light.room.id = :value";
            List<Light> roomLights = em.createQuery(jpql2, Light.class)
                            .setParameter("value", roomsInBuilding.get(i).getId())
                            .getResultList();
            for (int j = 0; j < roomLights.size(); j++) {
                buildingLights.add(roomLights.get(j));
            }
        }
        return buildingLights;
    }

    @Override
    public Building findBuildingById(Long id) {
        String jpql = "select building from Building building where building.id = :value";
        return em.createQuery(jpql, Building.class)
                .setParameter("value", id)
                .getSingleResult();
    }
}
