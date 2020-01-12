package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Light;
import com.emse.spring.faircorp.model.Room;

import java.util.List;

public interface RoomDaoCustom {
    Room findRoomByName(String roomName);
    Room findRoomById(Long id);
    List<Light> findLightsByRoomId(Long roomId);
    void updateRoom(Room room);
}
