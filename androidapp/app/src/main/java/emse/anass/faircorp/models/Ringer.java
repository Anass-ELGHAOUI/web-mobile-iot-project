package emse.anass.faircorp.models;

import java.io.Serializable;

public class Ringer implements Serializable{

    private Long id;

    private Integer level;

    private String status;

    private Long roomId;

    public Ringer() {

    }

    public Ringer(Long id, Integer level, String status, Long roomId) {
        this.id = id;
        this.level = level;
        this.status = status;
        this.roomId = roomId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}