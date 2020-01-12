package emse.anass.faircorp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.BuildingController;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Building;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBuildingFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.et_building_name)
    public EditText buildingName;

    @Bind(R.id.et_nbr_floors)
    public EditText nbrFloors;

    private Call<HashMap<String, Object>> buildingCall;

    public static AddBuildingFragment newInstance() {
        AddBuildingFragment fragment = new AddBuildingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_building, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ContextManagementActivity) getActivity();

    }

    private void sendData(Building building) {

        if(Utils.isNetworkAvailable(activity)){

            BuildingController buildingController = new BuildingController();
            buildingCall = buildingController.addbuilding(building);
            buildingCall.enqueue(new Callback<HashMap<String, Object>>() {
                @Override
                public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {

                    HashMap<String, Object> objectHashMap = response.body();

                    if(objectHashMap != null){
                        Utils.hideSoftKeyboard(activity);
                        activity.onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                    Log.w("Retrofit", "Failure-getCustomers");
                    Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Nullable
    @OnClick(R.id.btn_save_building)
    public void saveBuilding(){
        Log.i("saveBuilding","saveBuilding");

        if(!buildingName.getText().toString().isEmpty() && !nbrFloors.getText().toString().isEmpty()){
            Building building = new Building();

            building.setNbOfFloors(Integer.parseInt(nbrFloors.getText().toString()));
            building.setName(buildingName.getText().toString());
            sendData(building);
        }

    }

    @Nullable
    @OnClick(R.id.btn_close)
    public void backBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.onBackPressed();
    }
}
