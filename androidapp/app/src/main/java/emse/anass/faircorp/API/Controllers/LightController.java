package emse.anass.faircorp.API.Controllers;

import java.util.HashMap;
import java.util.List;

import emse.anass.faircorp.API.Callbacks.LightAPI;
import emse.anass.faircorp.API.OperatingApiClient;
import emse.anass.faircorp.models.Light;
import retrofit2.Call;
import retrofit2.Retrofit;

public class LightController {

    private LightAPI lightAPI;

    public LightController(){
        Retrofit retrofit = OperatingApiClient.getClient();
        lightAPI =  retrofit.create(LightAPI.class);
    }

    public Call<List<Light>> getLights(){
        return lightAPI.getLights(OperatingApiClient.getHeader());
    }

    public Call<HashMap<String, Object>> addLight(Light light){
        return lightAPI.addLight(OperatingApiClient.getHeader(), light);
    }

    public Call<HashMap<String, Object>> deleteLight(long id){
        return lightAPI.deleteLight(OperatingApiClient.getHeader(), id);
    }

    public Call<HashMap<String, Object>> switchLight(long id){
        return lightAPI.switchLight(OperatingApiClient.getHeader(), id);
    }

    public Call<HashMap<String, Object>> changeLevel(long id, Light light){
        return lightAPI.changeLevel(OperatingApiClient.getHeader(), id, light);
    }

    public Call<HashMap<String, Object>> changeColor(long id, Light light){
        return lightAPI.changeColor(OperatingApiClient.getHeader(), id, light);
    }
}
