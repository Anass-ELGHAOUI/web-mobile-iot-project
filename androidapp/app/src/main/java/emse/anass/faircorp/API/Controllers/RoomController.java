package emse.anass.faircorp.API.Controllers;

import java.util.HashMap;
import java.util.List;

import emse.anass.faircorp.API.Callbacks.RoomAPI;
import emse.anass.faircorp.API.OperatingApiClient;
import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RoomController {

    private RoomAPI roomAPI;

    public RoomController(){
        Retrofit retrofit = OperatingApiClient.getClient();
        roomAPI =  retrofit.create(RoomAPI.class);
    }

    public Call<List<Room>> getRooms(){
        return roomAPI.getRooms(OperatingApiClient.getHeader());
    }

    public Call<HashMap<String, Object>> addRoom(Room room){
        return roomAPI.addRoom(OperatingApiClient.getHeader(), room);
    }

    public Call<HashMap<String, Object>> deleteRoom(long id){
        return roomAPI.deleteRoom(OperatingApiClient.getHeader(), id);
    }
}
