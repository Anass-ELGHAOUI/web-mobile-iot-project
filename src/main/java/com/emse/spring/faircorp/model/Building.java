package com.emse.spring.faircorp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Building {
    @Id
    @GeneratedValue
    private Long id;

//    @Column(nullable = false)
    @Column
    private String name;

    @Column(nullable = false)
    private int nbOfFloors;

    @OneToMany(mappedBy = "building")
    private List<Room> rooms;

    public Building() {

    }

    public Building(Long id, String name, int nbOfFloors, List<Room> rooms) {
        this.id = id;
        this.name = name;
        this.nbOfFloors = nbOfFloors;
        this.rooms = rooms;
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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
