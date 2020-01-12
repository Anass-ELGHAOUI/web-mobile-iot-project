package com.emse.spring.faircorp.controller;

import com.emse.spring.faircorp.DAO.AutoControllerDao;
import com.emse.spring.faircorp.DAO.RoomDao;
import com.emse.spring.faircorp.DTO.AutoControllerDto;
import com.emse.spring.faircorp.model.AutoController;
import com.emse.spring.faircorp.model.Room;
import com.emse.spring.faircorp.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/autoLightControllers")
@Transactional
public class AutoControllerController {
    @Autowired
    private AutoControllerDao autoControllerDao;
    @Autowired
    private RoomDao roomDao;

    @GetMapping
    public List<AutoControllerDto> findAll(HttpServletResponse response) {
        return autoControllerDao.findAll()
                .stream()
                .map(AutoControllerDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public AutoControllerDto findById(@PathVariable Long id, HttpServletResponse response) {
        AutoController autoController = autoControllerDao.findAutoLightById(id);
        return new AutoControllerDto(autoController);
    }

    @PutMapping(path = "/{id}/switch")
    public AutoControllerDto switchAutoLightController(@PathVariable Long id, HttpServletResponse response) {
        AutoController autoController = autoControllerDao.findAutoLightById(id);
        Status currentStatus = autoController.getAutoLightControlState();
        if (currentStatus.equals(Status.ON)) {
            autoController.setAutoLightControlState(Status.OFF);
        }
        else {
            autoController.setAutoLightControlState(Status.ON);
        }
        return new AutoControllerDto(autoController);
    }

    @PutMapping(path = "/{id}/switchThermostat")
    public AutoControllerDto switchAutoThermostatController(@PathVariable Long id, HttpServletResponse response) {
        AutoController autoController = autoControllerDao.findAutoLightById(id);
        Status currentStatus = autoController.getAutoThermostatControlState();
        if (currentStatus.equals(Status.ON)) {
            autoController.setAutoThermostatControlState(Status.OFF);
        }
        else {
            autoController.setAutoThermostatControlState(Status.ON);
        }
        return new AutoControllerDto(autoController);
    }

    @PutMapping(path = "/{id}/sunset-sunrise")
    public AutoControllerDto changeSunsetSunrise(@PathVariable Long id, @RequestBody AutoControllerDto body, HttpServletResponse response) {
        AutoController autoController = autoControllerDao.findAutoLightById(id);
        autoController.setSunriseTime(body.getSunriseTime());
        autoController.setSunsetTime(body.getSunsetTime());
        if (body.getLatitude() != null) {
            autoController.setLatitude(body.getLatitude());
        }
        if (body.getLongitude() != null) {
            autoController.setLongitude(body.getLongitude());
        }
        return new AutoControllerDto(autoController);
    }

    @PutMapping(path = "/{id}/minTemperature")
    public AutoControllerDto changeMinMaxTemperatures(@PathVariable Long id, @RequestBody AutoControllerDto body, HttpServletResponse response) {
        AutoController autoController = autoControllerDao.findAutoLightById(id);
        autoController.setMinTemperature(body.getMinTemperature());
        if (body.getLatitude() != null) {
            autoController.setLatitude(body.getLatitude());
        }
        if (body.getLongitude() != null) {
            autoController.setLongitude(body.getLongitude());
        }
        return new AutoControllerDto(autoController);
    }

    @PostMapping
    public AutoControllerDto createAutoLightController(@RequestBody AutoControllerDto autoControllerDto, HttpServletResponse response) {
        Room room = null;
        if (autoControllerDto.getRoomId() != null) {
            room = roomDao.findRoomById(autoControllerDto.getRoomId());
        }
        AutoController autoController = new AutoController(autoControllerDto.getId(), autoControllerDto.getSunriseTime(), autoControllerDto.getSunsetTime(), autoControllerDto.getMinTemperature(), autoControllerDto.getLatitude(), autoControllerDto.getLongitude(), autoControllerDto.getAutoLightControlState(), autoControllerDto.getAutoThermostatControlState(), room);

        if (room != null) {
            roomDao.updateRoom(room);
        }

        autoControllerDao.save(autoController);
        return new AutoControllerDto(autoController);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAutoLightController(@PathVariable Long id, HttpServletResponse response) {
        autoControllerDao.delete(autoControllerDao.findAutoLightById(id));
    }
}