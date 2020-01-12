package com.emse.spring.faircorp.repository;

import com.emse.spring.faircorp.DAO.BuildingDao;
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
class BuildingDaoCustomImplTest {

    @Configuration
    @ComponentScan("com.emse.spring.faircorp")
    public static class BuildingDaoCustomImplTestConfig{}

    @Autowired
    private BuildingDao buildingDao;

    @Test
    public void shouldFindOnLights() {
        Assertions.assertThat(buildingDao.findBuildingLights((long) -20));

        List<Light> buildingLights = buildingDao.findBuildingLights((long) -20);
        for (int i = 0; i < buildingLights.size(); i++) {
            System.out.println("Building's all lights: " + buildingLights.get(i).getId());
        }
    }
}