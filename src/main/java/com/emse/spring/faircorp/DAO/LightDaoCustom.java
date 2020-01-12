package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Light;
import com.emse.spring.faircorp.model.Ringer;

import java.util.List;

public interface LightDaoCustom {
    List<Light> findOnLights();
    Light findById(Long id);
    void updateLight (Light light);
}
