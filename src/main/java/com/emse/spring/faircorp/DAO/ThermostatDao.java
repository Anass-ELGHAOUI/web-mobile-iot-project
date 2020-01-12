package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Thermostat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThermostatDao extends JpaRepository<Thermostat, String>, ThermostatDaoCustom {
}
