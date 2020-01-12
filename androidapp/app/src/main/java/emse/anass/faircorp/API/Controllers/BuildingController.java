package emse.anass.faircorp.API.Controllers;

import java.util.HashMap;
import java.util.List;

import emse.anass.faircorp.API.Callbacks.BuildingAPI;
import emse.anass.faircorp.API.OperatingApiClient;
import emse.anass.faircorp.models.Building;
import retrofit2.Call;
import retrofit2.Retrofit;

public class BuildingController {

    private BuildingAPI buildingAPI;

    public BuildingController(){
        Retrofit retrofit = OperatingApiClient.getClient();
        buildingAPI =  retrofit.create(BuildingAPI.class);
    }

    public Call<List<Building>> getBuildings(){
        return buildingAPI.getBuildings(OperatingApiClient.getHeader());
    }

    public Call<HashMap<String, Object>> addbuilding(Building building){
        return buildingAPI.addBuilding(OperatingApiClient.getHeader(), building);
    }

    public Call<HashMap<String, Object>> deleteBuilding(long id){
        return buildingAPI.deleteBuilding(OperatingApiClient.getHeader(), id);
    }

    public Call<Building> getBuilding(long id){
        return buildingAPI.getBuilding(OperatingApiClient.getHeader(), id);
    }
}
