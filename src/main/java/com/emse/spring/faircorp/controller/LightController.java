package com.emse.spring.faircorp.controller;

import com.emse.spring.faircorp.DAO.LightDao;
import com.emse.spring.faircorp.DAO.RoomDao;
import com.emse.spring.faircorp.DTO.LightDto;
import com.emse.spring.faircorp.model.Light;
import com.emse.spring.faircorp.model.Room;
import com.emse.spring.faircorp.model.Status;
import com.emse.spring.faircorp.mqtt.Mqtt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lights")
@Transactional
public class LightController {
    @Autowired
    private LightDao lightDao;
    @Autowired
    private RoomDao roomDao;

    @GetMapping
    public List<LightDto> findAll(HttpServletResponse response) {
        return lightDao.findAll()
                       .stream()
                       .map(LightDto::new)
                       .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public LightDto findById(@PathVariable Long id, HttpServletResponse response) {
        Light light = lightDao.findById(id);
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/switch")
    public LightDto switchLight(@PathVariable Long id, HttpServletResponse response) {
        Light light = lightDao.findById(id);
        Status currentStatus = light.getStatus();
        if (currentStatus.equals(Status.ON)) {
            light.setStatus(Status.OFF);
        }
        else {
            light.setStatus(Status.ON);
        }
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/switchLightArduino")
    public LightDto switchLightArduino(@PathVariable Long id, HttpServletResponse response) {
        Light light = lightDao.findById(id);
        Status currentStatus = light.getStatus();
        if (currentStatus.equals(Status.ON)) {
            /* REST Api */
            light.setStatus(Status.OFF);
            /* Philips hue */
            Mqtt mqtt = new Mqtt();
            String publishMessage = "off::" + String.valueOf(id);
            mqtt.mqttClient(publishMessage);
        }
        else {
            /* REST Api */
            light.setStatus(Status.ON);
            /* Philips hue */
            Mqtt mqtt = new Mqtt();
            String publishMessage = "on::" + String.valueOf(id);
            mqtt.mqttClient(publishMessage);
        }
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/changeLevelArduino")
    public LightDto changeLevelArduino(@PathVariable Long id, @RequestBody LightDto body, HttpServletResponse response) {
        /* REST Api */
        Light light = lightDao.findById(id);
        light.setLevel(body.getLevel());
        /* Philips hue */
        Mqtt mqtt = new Mqtt();
        String level = String.valueOf(body.getLevel());
        String publishMessage = "level::" + level + "::" + String.valueOf(id);
        mqtt.mqttClient(publishMessage);
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/switchOn")
    public LightDto turnOnLight(@PathVariable Long id, HttpServletResponse response) {
        Light light = lightDao.findById(id);
        light.setStatus(Status.ON);
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/switchOff")
    public LightDto turnOffLight(@PathVariable Long id, HttpServletResponse response) {
        Light light = lightDao.findById(id);
        light.setStatus(Status.OFF);
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/level")
    public LightDto changeLevel(@PathVariable Long id, @RequestBody LightDto body, HttpServletResponse response) {
        Light light = lightDao.findById(id);
        light.setLevel(body.getLevel());
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/color")
    public LightDto changeColor(@PathVariable Long id, @RequestBody LightDto body, HttpServletResponse response) {
        Light light = lightDao.findById(id);
        light.setColor(body.getColor());
        return new LightDto(light);
    }

    @PutMapping(path = "/{id}/lightOnMqtt")
    public void lightOnMqtt(@PathVariable Long id, HttpServletResponse response) {
        Mqtt mqtt = new Mqtt();
        String publishMessage = "on::" + String.valueOf(id);
        mqtt.mqttClient(publishMessage);
    }

    @PutMapping(path = "/{id}/lightOffMqtt")
    public void lightOffMqtt(@PathVariable Long id, HttpServletResponse response) {
        Mqtt mqtt = new Mqtt();
        String publishMessage = "off::" + String.valueOf(id);
        mqtt.mqttClient(publishMessage);
    }

    @PutMapping(path = "/{id}/lightLevelMqtt")
    public void lightLevelMqtt(@PathVariable Long id, @RequestBody LightDto body, HttpServletResponse response) {
        Mqtt mqtt = new Mqtt();
        String level = String.valueOf(body.getLevel());
        String publishMessage = "level::" + level + "::" + String.valueOf(id);
        mqtt.mqttClient(publishMessage);
    }

    @PutMapping(path = "/{id}/lightColorMqtt")
    public void lightColorMqtt(@PathVariable Long id, @RequestBody LightDto body, HttpServletResponse response) {
        /* REST Api */
        Light light = lightDao.findById(id);
        light.setColor(body.getColor());
        /* Philips hue */
        Mqtt mqtt = new Mqtt();
        String color = String.valueOf(body.getColor());
        String publishMessage = "color::" + color + "::" + String.valueOf(id);
        mqtt.mqttClient(publishMessage);
    }

    @PutMapping(path = "/{id}/arduino")
    public void controlWithArduino(@PathVariable Long id, HttpServletResponse response) {
        Mqtt mqtt = new Mqtt();
        String publishMessage = "arduino" + String.valueOf(id);
        mqtt.mqttClient(publishMessage);
    }

    @PostMapping
    public LightDto createLight(@RequestBody LightDto lightDto, HttpServletResponse response) {
        Room room = null;
        if (lightDto.getRoomId() != null) {
            room = roomDao.findRoomById(lightDto.getRoomId());
        }

        Light light = new Light(lightDto.getId(), lightDto.getLevel(), lightDto.getColor(), lightDto.getStatus(), room);
        lightDao.save(light);

        if (room != null) {
            roomDao.updateRoom(room);
        }
        return new LightDto(light);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteLight(@PathVariable Long id, HttpServletResponse response) {
        lightDao.delete(lightDao.findById(id));
    }
}
