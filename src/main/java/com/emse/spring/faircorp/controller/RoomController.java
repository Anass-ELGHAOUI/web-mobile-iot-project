package com.emse.spring.faircorp.controller;

import com.emse.spring.faircorp.DAO.*;
import com.emse.spring.faircorp.DTO.RoomDto;
import com.emse.spring.faircorp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/rooms")
@Transactional
public class RoomController {
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private LightDao lightDao;
    @Autowired
    private RingerDao ringerDao;
    @Autowired
    private ThermostatDao thermostatDao;
    @Autowired
    private BuildingDao buildingDao;
    @Autowired
    private AutoControllerDao autoControllerDao;

    @GetMapping
    public List<RoomDto> findAll(HttpServletResponse response) {
        return roomDao.findAll()
                .stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public RoomDto findById(@PathVariable Long id, HttpServletResponse response) {
        Room room = roomDao.findRoomById(id);
        return new RoomDto(room);
    }

    @PutMapping(path = "/{id}/switchLight")
    public RoomDto switchRoomLights(@PathVariable Long id, HttpServletResponse response) {
        Room room = roomDao.findRoomById(id);
        List<Light> roomLights = room.getLights();
        for (int i = 0; i < roomLights.size(); i++) {
            Status currentStatus = roomLights.get(i).getStatus();
            if (currentStatus.equals(Status.ON)) {
                roomLights.get(i).setStatus(Status.OFF);
            } else {
                roomLights.get(i).setStatus(Status.ON);
            }
        }
        return new RoomDto(room);
    }

    @PostMapping
    public RoomDto createRoom(@RequestBody RoomDto roomDto, HttpServletResponse response) {
        List<Light> roomLights = new ArrayList<Light>();
        if (roomDto.getLightsIds() != null) {
            for (int i = 0; i < roomDto.getLightsIds().size(); i++) {
                roomLights.add(lightDao.findById(roomDto.getLightsIds().get(i)));
            }
        }
        Ringer ringer = null;
        if (roomDto.getRingerId() != null) {
            ringer = ringerDao.findById(roomDto.getRingerId());
        }
        Thermostat thermostat = null;
        if (roomDto.getThermostatId() != null) {
            thermostat = thermostatDao.findById(roomDto.getThermostatId());
        }
        Building building = null;
        if (roomDto.getBuildingId() != null) {
            building = buildingDao.findBuildingById(roomDto.getBuildingId());
        }
        int floor = -999;
        if (roomDto.getFloor() != -999) {
            floor = roomDto.getFloor();
        }
        AutoController autoController = null;
        if (roomDto.getAutoLightControlId() != null) {
            autoController = autoControllerDao.findAutoLightById(roomDto.getAutoLightControlId());
        }

        Room room = new Room(roomDto.getId(), roomDto.getName(), floor, autoController, roomLights, ringer, thermostat, building);
        roomDao.save(room);

        if (ringer != null) {
            ringer.setRoom(room);
            ringerDao.updateRinger(ringer);
        }
        if (thermostat != null) {
            thermostat.setRoom(room);
            thermostatDao.updateThermostat(thermostat);
        }
        if (roomLights != null) {
            for (int i = 0; i < roomLights.size(); i++) {
                roomLights.get(i).setRoom(room);
                lightDao.updateLight(roomLights.get(i));
            }
        }
        return new RoomDto(room);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteRoom(@PathVariable Long id, HttpServletResponse response) {
        Room room = roomDao.findRoomById(id);
        /* Delete the room's lights */
        if (room.getLights() != null) {
            for (int i = 0; i < room.getLights().size(); i++) {
                lightDao.delete(room.getLights().get(i));
            }
        }
        /* Delete the room's ringer */
        if (room.getRinger() != null) {
            ringerDao.delete(room.getRinger());
        }
        /* Delete the room's thermostat */
        if (room.getThermostat() != null) {
            thermostatDao.delete(room.getThermostat());
        }
        /* Delete the room */
        roomDao.delete(room);
    }
}