package emse.anass.faircorp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.BuildingController;
import emse.anass.faircorp.API.Controllers.RingerController;
import emse.anass.faircorp.API.Controllers.RoomController;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Building;
import emse.anass.faircorp.models.Ringer;
import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRoomFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.et_room_name)
    public EditText roomName;

    @Bind(R.id.et_floor)
    public EditText floor;

    @Bind(R.id.et_room_building)
    public EditText buildingName;

    @Bind(R.id.room_ringer)
    public Switch ringerSwitch;

    @Bind(R.id.add_ringer)
    public ImageView addRinger;

    @Bind(R.id.tv_ringer)
    public TextView tvRinger;

    @Bind(R.id.room_title)
    public TextView roomTitle;

    private Room room;
    private Building building;
    private Ringer ringer;

    private Call<HashMap<String, Object>> deleteCall;

    public static EditRoomFragment newInstance(Room room) {
        EditRoomFragment fragment = new EditRoomFragment();
        Bundle args = new Bundle();
        args.putSerializable("room",room);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ContextManagementActivity) getActivity();
        this.room = (Room) getArguments().getSerializable("room");
        initUI();
    }

    private void initUI(){
        roomName.setText(room.getName());
        floor.setText(""+room.getFloor());
        roomTitle.setText(room.getName());
        if(room.getRingerId() == null){
            ringerSwitch.setVisibility(View.GONE);
            addRinger.setVisibility(View.VISIBLE);
        }else{
            ringerSwitch.setVisibility(View.VISIBLE);
            addRinger.setVisibility(View.GONE);
            getRinger();
        }
        getBuilding();
    }

    private void getBuilding(){
        BuildingController buildingController = new BuildingController();
        Call<Building> buildingCall = buildingController.getBuilding(this.room.getBuildingId());
        buildingCall.enqueue(new Callback<Building>() {
            @Override
            public void onResponse(Call<Building> call, Response<Building> response) {

                building = response.body();

                if(building != null){
                    Utils.hideSoftKeyboard(activity);
                    buildingName.setText(building.getName());
                }
            }

            @Override
            public void onFailure(Call<Building> call, Throwable t) {
                Log.w("Retrofit", "Failure-getCustomers");
                Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRinger(){
        RingerController ringerController = new RingerController();
        Call<Ringer> ringerCall = ringerController.getRinger(this.room.getRingerId());
        ringerCall.enqueue(new Callback<Ringer>() {
            @Override
            public void onResponse(Call<Ringer> call, Response<Ringer> response) {

                ringer = response.body();

                if(ringer != null){
                    Utils.hideSoftKeyboard(activity);
                    tvRinger.setText("Ringer ("+ringer.getLevel()+")");
                    if(ringer.getStatus().equals("ON")){
                        ringerSwitch.setChecked(true);
                    }else{
                        ringerSwitch.setChecked(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Ringer> call, Throwable t) {
                Log.w("Retrofit", "Failure-getCustomers");
                Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @OnClick(R.id.add_ringer)
    public void addRinger(){
        Utils.hideSoftKeyboard(activity);
        activity.navigateTo(AddRingerFragment.newInstance(room));
    }

    @Nullable
    @OnClick(R.id.btn_delete_room)
    public void deleteRoom(){
        Log.i("deleteRoom","deleteRoom");
        RoomController roomController = new RoomController();
        deleteCall = roomController.deleteRoom(this.room.getId());
        deleteCall.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                Utils.hideSoftKeyboard(activity);
                activity.onBackPressed();
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                Log.w("Retrofit", "Failure-getCustomers");
                Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @OnClick(R.id.btn_save_room)
    public void saveRoom(){
        if(ringer != null){
            if(room.getRingerId() == null){
                room.setRingerId(ringer.getId());
            }
            if((ringer.getStatus().equals("ON") && !ringerSwitch.isChecked()) || (ringer.getStatus().equals("OFF") && ringerSwitch.isChecked())){
                RingerController ringerController = new RingerController();
                Call<HashMap<String, Object>> ringerCall = ringerController.switchRinger(ringer.getId());
                ringerCall.enqueue(new Callback<HashMap<String, Object>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {

                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                        Log.w("Retrofit", "Failure-getCustomers");
                        Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        activity.onBackPressed();
    }

    @Nullable
    @OnClick(R.id.btn_close)
    public void backBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.onBackPressed();
    }

    @Nullable
    @OnClick(R.id.lights_layout)
    public void lightsBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.navigateTo(LightsFragment.newInstance(room));
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }
}
