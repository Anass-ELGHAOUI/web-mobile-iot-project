package emse.anass.faircorp.API.Callbacks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emse.anass.faircorp.models.Ringer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RingerAPI {

    @GET("api/ringers")
    Call<List<Ringer>> getRingers(@HeaderMap Map<String, String> headers);

    @POST("api/ringers")
    Call<HashMap<String, Object>> addRinger(@HeaderMap Map<String, String> headers, @Body Ringer ringer);

    @DELETE("api/ringers/{id}")
    Call<HashMap<String, Object>> deleteRinger(@HeaderMap Map<String, String> headers, @Path("id") long id);

    @GET("api/ringers/{id}")
    Call<Ringer> getRinger(@HeaderMap Map<String, String> headers, @Path("id") long id);

    @PUT("api/ringers/{id}/switch")
    Call<HashMap<String, Object>> switchRinger(@HeaderMap Map<String, String> headers, @Path("id") long id);

}