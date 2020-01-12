package com.emse.spring.faircorp.DTO;

import com.emse.spring.faircorp.model.AutoController;
import com.emse.spring.faircorp.model.Status;

public class AutoControllerDto {
    private Long id;

    private String sunriseTime;

    private String sunsetTime;

    private String minTemperature;

    private String latitude;

    private String longitude;

    private Status autoLightControlState;

    private Status autoThermostatControlState;

    private Long roomId;

    public AutoControllerDto() {

    }

    public AutoControllerDto(AutoController autoController) {
        this.id = autoController.getId();
        if (autoController.getSunriseTime() != null) {
            this.sunriseTime = autoController.getSunriseTime();
        }
        if (autoController.getSunsetTime() != null) {
            this.sunsetTime = autoController.getSunsetTime();
        }
        if (autoController.getMinTemperature() != null) {
            this.minTemperature = autoController.getMinTemperature();
        }
        if (autoController.getLatitude() != null) {
            this.latitude = autoController.getLatitude();
        }
        if (autoController.getLongitude() != null) {
            this.longitude = autoController.getLongitude();
        }
        this.autoLightControlState = autoController.getAutoLightControlState();
        this.autoThermostatControlState = autoController.getAutoThermostatControlState();
        if (autoController.getRoom() != null) {
            this.roomId = autoController.getRoom().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Status getAutoLightControlState() {
        return autoLightControlState;
    }

    public void setAutoLightControlState(Status autoLightControlState) {
        this.autoLightControlState = autoLightControlState;
    }

    public Status getAutoThermostatControlState() {
        return autoThermostatControlState;
    }

    public void setAutoThermostatControlState(Status autoThermostatControlState) {
        this.autoThermostatControlState = autoThermostatControlState;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
