package com.emse.spring.faircorp.controller;

import com.emse.spring.faircorp.DAO.RoomDao;
import com.emse.spring.faircorp.DAO.ThermostatDao;
import com.emse.spring.faircorp.DTO.ThermostatDto;
import com.emse.spring.faircorp.model.Room;
import com.emse.spring.faircorp.model.Status;
import com.emse.spring.faircorp.model.Thermostat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/thermostats")
@Transactional
public class ThermostatController {
    @Autowired
    private ThermostatDao thermostatDao;
    @Autowired
    private RoomDao roomDao;

    @GetMapping
    public List<ThermostatDto> findAll(HttpServletResponse response) {
        return thermostatDao.findAll()
                .stream()
                .map(ThermostatDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ThermostatDto findById(@PathVariable Long id, HttpServletResponse response) {
        Thermostat thermostat = thermostatDao.findById(id);
        return new ThermostatDto(thermostat);
    }

    @PutMapping(path = "/{id}/switch")
    public ThermostatDto switchThermostat(@PathVariable Long id, HttpServletResponse response) {
        Thermostat thermostat = thermostatDao.findById(id);
        Status currentStatus = thermostat.getStatus();
        if (currentStatus.equals(Status.ON)) {
            thermostat.setStatus(Status.OFF);
        }
        else {
            thermostat.setStatus(Status.ON);
        }
        return new ThermostatDto(thermostat);
    }

    @PutMapping(path = "/{id}/switchOn")
    public ThermostatDto turnOnThermostat(@PathVariable Long id, HttpServletResponse response) {
        Thermostat thermostat = thermostatDao.findById(id);
        thermostat.setStatus(Status.ON);
        return new ThermostatDto(thermostat);
    }

    @PutMapping(path = "/{id}/switchOff")
    public ThermostatDto turnOffThermostat(@PathVariable Long id, HttpServletResponse response) {
        Thermostat thermostat = thermostatDao.findById(id);
        thermostat.setStatus(Status.OFF);
        return new ThermostatDto(thermostat);
    }

    @PutMapping(path = "/{id}/level")
    public ThermostatDto changeLevel(@PathVariable Long id, @RequestBody ThermostatDto body, HttpServletResponse response) {
        Thermostat thermostat = thermostatDao.findById(id);
        thermostat.setLevel(body.getLevel());
        return new ThermostatDto(thermostat);
    }

    @PutMapping(path = "/{id}/temperatures")
    public ThermostatDto changeFunctioningTemperatures(@PathVariable Long id, @RequestBody ThermostatDto body, HttpServletResponse response) {
        Thermostat thermostat = thermostatDao.findById(id);
        thermostat.setOnTemperature(body.getOnTemperature());
        thermostat.setOffTemperature(body.getOffTemperature());
        return new ThermostatDto(thermostat);
    }

    @PostMapping
    public ThermostatDto createThermostat(@RequestBody ThermostatDto thermostatDto, HttpServletResponse response) {
        Room room = null;
        if (thermostatDto.getRoomId() != null) {
            room = roomDao.findRoomById(thermostatDto.getRoomId());
        }

        Thermostat thermostat = new Thermostat(thermostatDto.getId(), thermostatDto.getLevel(), thermostatDto.getStatus(), thermostatDto.getOnTemperature(), thermostatDto.getOffTemperature(), room);
        if (room != null) {
            roomDao.updateRoom(room);
            thermostatDao.removeThermostatFromPreviousRoom(thermostatDao.findByRoomId(room.getId()));
        }

        thermostatDao.save(thermostat);
        return new ThermostatDto(thermostat);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteThermostat(@PathVariable Long id, HttpServletResponse response) {
        thermostatDao.delete(thermostatDao.findById(id));
    }
}