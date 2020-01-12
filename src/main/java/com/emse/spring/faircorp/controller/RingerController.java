package com.emse.spring.faircorp.controller;

import com.emse.spring.faircorp.DAO.RingerDao;
import com.emse.spring.faircorp.DAO.RoomDao;
import com.emse.spring.faircorp.DTO.RingerDto;
import com.emse.spring.faircorp.model.Ringer;
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
@RequestMapping("/api/ringers")
@Transactional
public class RingerController {
    @Autowired
    private RingerDao ringerDao;
    @Autowired
    private RoomDao roomDao;

    @GetMapping
    public List<RingerDto> findAll(HttpServletResponse response) {
        return ringerDao.findAll()
                .stream()
                .map(RingerDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public RingerDto findById(@PathVariable Long id, HttpServletResponse response) {
        Ringer ringer = ringerDao.findById(id);
        return new RingerDto(ringer);
    }

    @PutMapping(path = "/{id}/switch")
    public RingerDto switchRinger(@PathVariable Long id, HttpServletResponse response) {
        Ringer ringer = ringerDao.findById(id);
        Status currentStatus = ringer.getStatus();
        if (currentStatus.equals(Status.ON)) {
            ringer.setStatus(Status.OFF);
        }
        else {
            ringer.setStatus(Status.ON);
        }
        return new RingerDto(ringer);
    }

    @PutMapping(path = "/{id}/level")
    public RingerDto changeLevel(@PathVariable Long id, @RequestBody RingerDto body, HttpServletResponse response) {
        Ringer ringer = ringerDao.findById(id);
        ringer.setLevel(body.getLevel());
        return new RingerDto(ringer);
    }

    @PostMapping
    public RingerDto createRinger(@RequestBody RingerDto ringerDto, HttpServletResponse response) {
        Room room = null;
        if (ringerDto.getRoomId() != null) {
            room = roomDao.findRoomById(ringerDto.getRoomId());
        }

        Ringer ringer = new Ringer(ringerDto.getId(), ringerDto.getLevel(), ringerDto.getStatus(), room);
        if (room != null) {
            roomDao.updateRoom(room);
            ringerDao.removeRingerFromPreviousRoom(ringerDao.findByRoomId(room.getId()));
        }

        ringerDao.save(ringer);
        return new RingerDto(ringer);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteRinger(@PathVariable Long id, HttpServletResponse response) {
        ringerDao.delete(ringerDao.findById(id));
    }
}