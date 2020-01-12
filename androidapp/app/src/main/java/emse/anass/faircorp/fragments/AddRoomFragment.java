package emse.anass.faircorp.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.BuildingController;
import emse.anass.faircorp.API.Controllers.RoomController;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Building;
import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRoomFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.add_room_name)
    public EditText roomName;

    @Bind(R.id.add_floor_room)
    public EditText floor;

    @Bind(R.id.building_spinner)
    public Spinner buildingName;

    private Call<HashMap<String, Object>> roomCall;
    private Call<List<Building>> buildingCall;
    private List<Building> buildingList;
    private Room room;

    public static AddRoomFragment newInstance() {
        AddRoomFragment fragment = new AddRoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_room, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ContextManagementActivity) getActivity();
        room = new Room();
        getAllBuildings();
    }

    private void sendData(Room room) {

        if(Utils.isNetworkAvailable(activity)){

            RoomController roomController = new RoomController();
            roomCall = roomController.addRoom(room);
            roomCall.enqueue(new Callback<HashMap<String, Object>>() {
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

    private void getAllBuildings(){
        final BuildingController buildingController = new BuildingController();
        buildingCall = buildingController.getBuildings();
        buildingCall.enqueue(new Callback<List<Building>>() {
            @Override
            public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {

                buildingList = new ArrayList<>(response.body());
                populateSpinner();
                Log.i("buildingsResponses", "" +buildingList);

            }

            @Override
            public void onFailure(Call<List<Building>> call, Throwable t) {
                Log.w("Retrofit", "Failure-getCustomers");
                Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner(){
        final List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Select Building");
        for (int i=0; i < buildingList.size(); i++) {
            spinnerArray.add(buildingList.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        buildingName.setAdapter(adapter);

        buildingName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position != 0) {
                    room.setBuildingId(buildingList.get(position-1).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Nullable
    @OnClick(R.id.btn_add_room)
    public void saveRoom(){
        Log.i("saveRoom","saveRoom");

        if(!roomName.getText().toString().isEmpty() && !floor.getText().toString().isEmpty() && room.getBuildingId()!= null){
            Room newRoom = new Room();
            newRoom.setName(roomName.getText().toString());
            newRoom.setFloor(Integer.parseInt(floor.getText().toString()));
            newRoom.setBuildingId(room.getBuildingId());
            sendData(newRoom);
        }

    }

    @Nullable
    @OnClick(R.id.btn_close)
    public void backBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.onBackPressed();
    }
}
