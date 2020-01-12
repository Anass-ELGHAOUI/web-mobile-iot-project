package com.emse.spring.faircorp.model;

import javax.persistence.*;

@Entity
public class Thermostat {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer level;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String onTemperature;

    @Column
    private String offTemperature;

    @OneToOne
    private Room room;

    public Thermostat() {

    }

    public Thermostat(Long id, Integer level, Status status, String onTemperature, String offTemperature, Room room) {
        this.id = id;
        this.level = level;
        this.status = status;
        this.onTemperature = onTemperature;
        this.offTemperature = offTemperature;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOnTemperature() {
        return onTemperature;
    }

    public void setOnTemperature(String onTemperature) {
        this.onTemperature = onTemperature;
    }

    public String getOffTemperature() {
        return offTemperature;
    }

    public void setOffTemperature(String offTemperature) {
        this.offTemperature = offTemperature;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}