package emse.anass.faircorp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.LightController;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Light;
import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLightFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.add_light_level)
    public SeekBar lightLevel;

    @Bind(R.id.add_light_color)
    public SeekBar lightColor;

    @Bind(R.id.switch_light)
    public Switch switchLight;

    @Bind(R.id.et_id_light)
    public EditText idLight;

    private Call<HashMap<String, Object>> lightCall;
    private Room room;

    public static AddLightFragment newInstance(Room room) {
        AddLightFragment fragment = new AddLightFragment();
        Bundle args = new Bundle();
        args.putSerializable("room",room);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_light, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ContextManagementActivity) getActivity();
        this.room = (Room) getArguments().getSerializable("room");

    }

    private void sendData(Light light) {

        if(Utils.isNetworkAvailable(activity)){

            LightController lightController = new LightController();
            lightCall = lightController.addLight(light);
            lightCall.enqueue(new Callback<HashMap<String, Object>>() {
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
    @OnClick(R.id.save_light)
    public void saveLight(){
        Log.i("saveLight","saveLight-"+room.getName());
        if(!idLight.getText().toString().isEmpty()){
            Light light = new Light();
            light.setColor(lightColor.getProgress());
            light.setLevel(lightLevel.getProgress());
            String status = switchLight.isChecked() ? "ON" : "OFF";
            light.setRoomId(room.getId());
            light.setStatus(status);
            light.setId(Long.parseLong(idLight.getText().toString()));
            sendData(light);
        }


    }

    @Nullable
    @OnClick(R.id.btn_close)
    public void backBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.onBackPressed();
    }
}
