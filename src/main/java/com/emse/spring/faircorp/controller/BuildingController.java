package com.emse.spring.faircorp.controller;

import com.emse.spring.faircorp.DAO.*;
import com.emse.spring.faircorp.DTO.BuildingDto;
import com.emse.spring.faircorp.model.Building;
import com.emse.spring.faircorp.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/buildings")
@Transactional
public class BuildingController {
    @Autowired
    private BuildingDao buildingDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private LightDao lightDao;
    @Autowired
    private RingerDao ringerDao;
    @Autowired
    private ThermostatDao thermostatDao;

    @GetMapping
    public List<BuildingDto> findAll(HttpServletResponse response) {
        return buildingDao.findAll()
                .stream()
                .map(BuildingDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public BuildingDto findById(@PathVariable Long id, HttpServletResponse response) {
        Building building = buildingDao.findBuildingById(id);
        return new BuildingDto(building);
    }

    @PostMapping
    public BuildingDto createBuilding(@RequestBody BuildingDto buildingDto, HttpServletResponse response) {
        List<Room> buildingRooms = new ArrayList<Room>();
        if (buildingDto.getRoomsIds() != null) {
            for (int i = 0; i < buildingDto.getRoomsIds().size(); i++) {
                buildingRooms.add(roomDao.findRoomById(buildingDto.getRoomsIds().get(i)));
            }
        }

        Building building = new Building(buildingDto.getId(), buildingDto.getName(), buildingDto.getNbOfFloors(), buildingRooms);
        buildingDao.save(building);

        if (buildingRooms != null) {
            for (int i = 0; i < buildingRooms.size(); i++) {
                buildingRooms.get(i).setBuilding(building);
                roomDao.updateRoom(buildingRooms.get(i));
            }
        }
        return new BuildingDto(building);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteBuilding(@PathVariable Long id, HttpServletResponse response) {
        Building building = buildingDao.findBuildingById(id);
        if (building.getRooms() != null) {
            /* Delete building's lights, ringers and thermostats */
            for (int i = 0; i < building.getRooms().size(); i++) {
                /* Delete the building's lights */
                if (building.getRooms().get(i).getLights() != null) {
                    for (int j = 0; j < building.getRooms().get(i).getLights().size(); j++) {
                        lightDao.delete(building.getRooms().get(i).getLights().get(j));
                    }
                }
                /* Delete the building's ringers */
                if (building.getRooms().get(i).getRinger() != null) {
                    ringerDao.delete(building.getRooms().get(i).getRinger());
                }
                /* Delete the building's thermostats */
                if (building.getRooms().get(i).getThermostat() != null) {
                    thermostatDao.delete(building.getRooms().get(i).getThermostat());
                }
            }

            /* Delete building's rooms */
            for (int i = 0; i < building.getRooms().size(); i++) {
                roomDao.delete(building.getRooms().get(i));
            }
        }

        /* Delete the building */
        buildingDao.delete(building);
    }
}
