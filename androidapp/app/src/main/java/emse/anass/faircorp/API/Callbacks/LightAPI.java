package emse.anass.faircorp.API.Callbacks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emse.anass.faircorp.models.Light;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LightAPI {

    @GET("api/lights")
    Call<List<Light>> getLights(@HeaderMap Map<String, String> headers);


    @POST("api/lights")
    Call<HashMap<String, Object>> addLight(@HeaderMap Map<String, String> headers, @Body Light light);

    @DELETE("api/lights/{id}")
    Call<HashMap<String, Object>> deleteLight(@HeaderMap Map<String, String> headers, @Path("id") long id);

    @GET("api/lights/{id}")
    Call<Light> getLight(@HeaderMap Map<String, String> headers, @Path("id") long id);

    @PUT("api/lights/{id}/switchLightArduino")
    Call<HashMap<String, Object>> switchLight(@HeaderMap Map<String, String> headers, @Path("id") long id);

    @PUT("api/lights/{id}/changeLevelArduino")
    Call<HashMap<String, Object>> changeLevel(@HeaderMap Map<String, String> headers, @Path("id") long id, @Body Light light);

    @PUT("api/lights/{id}/lightColorMqtt")
    Call<HashMap<String, Object>> changeColor(@HeaderMap Map<String, String> headers, @Path("id") long id, @Body Light light);

}
