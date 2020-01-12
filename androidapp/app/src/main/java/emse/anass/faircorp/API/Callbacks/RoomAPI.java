package emse.anass.faircorp.API.Callbacks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface  RoomAPI {
    
    @GET("api/rooms")
    Call<List<Room>> getRooms(@HeaderMap Map<String, String> headers);


    @POST("api/rooms")
    Call<HashMap<String, Object>> addRoom(@HeaderMap Map<String, String> headers, @Body Room room);

    @DELETE("api/rooms/{id}")
    Call<HashMap<String, Object>> deleteRoom(@HeaderMap Map<String, String> headers, @Path("id") long id);

}
