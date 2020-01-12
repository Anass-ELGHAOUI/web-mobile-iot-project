package emse.anass.faircorp.models;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable{

    private Long id;

    private String name;

    private int floor;

    private List<Long> lightsIds;

    private Long ringerId;

    private Long thermostatId;

    private Long autoLightControlId;

    private Long buildingId;


    public Room() {

    }

    public Room(Long id, String name, int floor, List<Long> lights, long ringerId, long autoLightControlId, long buildingId, long thermostatId) {
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.lightsIds = lightsIds;
        this.ringerId = ringerId;
        this.autoLightControlId = autoLightControlId;
        this.buildingId = buildingId;
        this.thermostatId = thermostatId;
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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<Long> getLightsIds() {
        return lightsIds;
    }

    public void setLights(List<Long> lightsIds) {
        this.lightsIds = lightsIds;
    }

    public Long getRingerId() {
        return ringerId;
    }

    public void setRingerId(long ringerId) {
        this.ringerId = ringerId;
    }

    public long getAutoLightControlId() {
        return autoLightControlId;
    }

    public void setAutoLightControlId(long autoLightControlId) {
        this.autoLightControlId = autoLightControlId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public long getThermostatId() {
        return thermostatId;
    }

    public void setThermostatId(long thermostatId) {
        this.thermostatId = thermostatId;
    }
}