package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Ringer;

public interface RingerDaoCustom {
    Ringer findById(Long id);
    Ringer findByRoomId(Long id);
    void updateRinger(Ringer ringer);
    void removeRingerFromPreviousRoom(Ringer ringer);
}
