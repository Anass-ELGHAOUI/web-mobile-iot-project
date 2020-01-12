package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Thermostat;

public interface ThermostatDaoCustom {
    Thermostat findById(Long id);
    Thermostat findByRoomId(Long id);
    void updateThermostat(Thermostat thermostat);
    void removeThermostatFromPreviousRoom(Thermostat thermostat);
}
