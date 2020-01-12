package emse.anass.faircorp.models;

public class Light {

    private Long id;

    private Integer level;

    private Integer color;

    private String status;

    private Long roomId;

    public Light() {

    }

    public Light(Long id, Integer level, Integer color, String status, Long roomId) {
        this.id = id;
        this.level = level;
        this.color = color;
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

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
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