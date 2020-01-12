package com.emse.spring.faircorp.DTO;

import com.emse.spring.faircorp.model.Building;

import java.util.ArrayList;
import java.util.List;

public class BuildingDto {
    private Long id;

    private String name;

    private int nbOfFloors;

    private List<Long> roomsIds;

    public BuildingDto() {

    }

    public BuildingDto(Building building) {
        this.id = building.getId();
        this.name = building.getName();
        this.nbOfFloors = building.getNbOfFloors();
        this.roomsIds = new ArrayList<Long>();
        if (building.getRooms() != null) {
            for (int i = 0; i < building.getRooms().size(); i++) {
                roomsIds.add(building.getRooms().get(i).getId());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbOfFloors() {
        return nbOfFloors;
    }

    public void setNbOfFloors(int nbOfFloors) {
        this.nbOfFloors = nbOfFloors;
    }

    public List<Long> getRoomsIds() {
        return roomsIds;
    }

    public void setRoomsIds(List<Long> roomsIds) {
        this.roomsIds = roomsIds;
    }
}
