package com.emse.spring.faircorp.DTO;

import com.emse.spring.faircorp.model.Light;
import com.emse.spring.faircorp.model.Status;

public class LightDto {
    private Long id;

    private Integer level;

    private Integer color;

    private Status status;

    private Long roomId;

    public LightDto() {

    }

    public LightDto(Light light) {
        this.id = light.getId();
        this.level = light.getLevel();
        this.color = light.getColor();
        this.status = light.getStatus();
        if (light.getRoom() != null) {
            this.roomId = light.getRoom().getId();
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

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}