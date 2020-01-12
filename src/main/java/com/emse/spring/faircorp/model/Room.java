package com.emse.spring.faircorp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue
    private Long id;

//    @Column(nullable = false)
    @Column
    private String name;

    @Column(nullable = false)
    private int floor;

    @OneToMany(mappedBy = "room")
    private List<Light> lights;

    @OneToOne(mappedBy = "room")
    private Ringer ringer;

    @OneToOne(mappedBy = "room")
    private Thermostat thermostat;

    @OneToOne(mappedBy = "room")
    private AutoController autoControllerControl;

    @ManyToOne
    private Building building;

    public Room() {

    }

    public Room(Long id, String name, int floor, AutoController autoControllerControl, List<Light> lights, Ringer ringer, Thermostat thermostat, Building building) {
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.autoControllerControl = autoControllerControl;
        this.lights = lights;
        this.ringer = ringer;
        this.thermostat = thermostat;
        this.building = building;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    public Ringer getRinger() {
        return ringer;
    }

    public void setRinger(Ringer ringer) {
        this.ringer = ringer;
    }

    public Thermostat getThermostat() {
        return thermostat;
    }

    public void setThermostat(Thermostat thermostat) {
        this.thermostat = thermostat;
    }

    public AutoController getAutoControllerControl() {
        return autoControllerControl;
    }

    public void setAutoControllerControl(AutoController autoControllerControl) {
        this.autoControllerControl = autoControllerControl;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
