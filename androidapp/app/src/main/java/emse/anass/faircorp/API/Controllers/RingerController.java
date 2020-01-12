package emse.anass.faircorp.API.Controllers;

import java.util.HashMap;
import java.util.List;

import emse.anass.faircorp.API.Callbacks.RingerAPI;
import emse.anass.faircorp.API.OperatingApiClient;
import emse.anass.faircorp.models.Ringer;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RingerController {

    private RingerAPI ringerAPI;

    public RingerController(){
        Retrofit retrofit = OperatingApiClient.getClient();
        ringerAPI =  retrofit.create(RingerAPI.class);
    }

    public Call<List<Ringer>> getRingers(){
        return ringerAPI.getRingers(OperatingApiClient.getHeader());
    }

    public Call<HashMap<String, Object>> addRinger(Ringer ringer){
        return ringerAPI.addRinger(OperatingApiClient.getHeader(), ringer);
    }

    public Call<HashMap<String, Object>> deleteRinger(long id){
        return ringerAPI.deleteRinger(OperatingApiClient.getHeader(), id);
    }

    public Call<Ringer> getRinger(long id){
        return ringerAPI.getRinger(OperatingApiClient.getHeader(), id);
    }

    public Call<HashMap<String, Object>> switchRinger(long id){
        return ringerAPI.switchRinger(OperatingApiClient.getHeader(), id);
    }

}