package com.emse.spring.faircorp.model;

import javax.persistence.*;

@Entity
public class Light {
    @Id
    private Long id;

    @Column
    private Integer level;

    @Column
    private Integer color;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Room room;

    public Light() {

    }

    public Light(Long id, Integer level, Integer color, Status status, Room room) {
        this.id = id;
        this.level = level;
        this.color = color;
        this.status = status;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}