package com.emse.spring.faircorp.DTO;

import com.emse.spring.faircorp.model.Ringer;
import com.emse.spring.faircorp.model.Status;

public class RingerDto {
    private Long id;

    private Integer level;

    private Status status;

    private Long roomId;

    public RingerDto() {

    }

    public RingerDto(Ringer ringer) {
        this.id = ringer.getId();
        this.level = ringer.getLevel();
        this.status = ringer.getStatus();
        if (ringer.getRoom() != null) {
            this.roomId = ringer.getRoom().getId();
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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}