package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao extends JpaRepository<Room, String>, RoomDaoCustom {

}
