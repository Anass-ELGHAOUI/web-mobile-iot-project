package com.emse.spring.faircorp.repository;

import com.emse.spring.faircorp.DAO.RoomDao;
import com.emse.spring.faircorp.model.Light;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RoomDaoCustomImplTest {

    @Configuration
    @ComponentScan("com.emse.spring.faircorp")
    public static class RoomDaoCustomImplTestConfig{}

    @Autowired
    private RoomDao roomDao;

    @Test
    public void findRoomByName() {
        Assertions.assertThat(roomDao.findRoomByName("Room1"));

        System.out.println("Get room by name: " + roomDao.findRoomByName("Room1").getName());
    }

    @Test
    public void findLightsOfRoom() {
        Assertions.assertThat(roomDao.findLightsByRoomId((long) -10));

        List<Light> roomLights = roomDao.findLightsByRoomId((long) -10);
        for (int i = 0; i < roomLights.size(); i++) {
            System.out.println("Room's lights ids: " + roomLights.get(i).getId());
        }
    }
}