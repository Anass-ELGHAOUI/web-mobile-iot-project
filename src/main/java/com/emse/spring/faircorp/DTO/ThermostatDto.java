package com.emse.spring.faircorp.DTO;

import com.emse.spring.faircorp.model.Status;
import com.emse.spring.faircorp.model.Thermostat;

public class ThermostatDto {
    private Long id;

    private Integer level;

    private Status status;

    private String onTemperature;

    private String offTemperature;

    private Long roomId;

    public ThermostatDto() {

    }

    public ThermostatDto(Thermostat thermostat) {
        this.id = thermostat.getId();
        this.level = thermostat.getLevel();
        this.status = thermostat.getStatus();
        this.onTemperature = thermostat.getOnTemperature();
        this.offTemperature = thermostat.getOffTemperature();
        if (thermostat.getRoom() != null) {
            this.roomId = thermostat.getRoom().getId();
        }
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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}