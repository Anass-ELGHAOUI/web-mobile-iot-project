package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Building;
import com.emse.spring.faircorp.model.Light;

import java.util.List;

public interface BuildingDaoCustom {
    List<Light> findBuildingLights(Long id);
    Building findBuildingById(Long id);
}
