package emse.anass.faircorp.API.Callbacks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emse.anass.faircorp.models.Building;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BuildingAPI {

    @GET("api/buildings")
    Call<List<Building>> getBuildings(@HeaderMap Map<String, String> headers);


    @POST("api/buildings")
    Call<HashMap<String, Object>> addBuilding(@HeaderMap Map<String, String> headers, @Body Building building);

    @DELETE("api/buildings/{id}")
    Call<HashMap<String, Object>> deleteBuilding(@HeaderMap Map<String, String> headers, @Path("id") long id);

    @GET("api/buildings/{id}")
    Call<Building> getBuilding(@HeaderMap Map<String, String> headers, @Path("id") long id);

}
