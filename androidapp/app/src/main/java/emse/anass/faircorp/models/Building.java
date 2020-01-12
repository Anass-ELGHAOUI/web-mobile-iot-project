package emse.anass.faircorp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Building implements Serializable{

    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("nbOfFloors")
    private int nbOfFloors;

    @SerializedName("roomsIds")
    private List<Long> roomsIds;

    public Building() {

    }

    public Building(Long id, String name, int nbOfFloors, List<Long> roomsIds) {
        this.id = id;
        this.name = name;
        this.nbOfFloors = nbOfFloors;
        this.roomsIds = roomsIds;
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

    public int getNbOfFloors() {
        return nbOfFloors;
    }

    public void setNbOfFloors(int nbOfFloors) {
        this.nbOfFloors = nbOfFloors;
    }

    public List<Long> getRoomsIds() { return roomsIds; }

    public void setRoomsIds(List<Long> roomsIds) { this.roomsIds = roomsIds; }
}
