package emse.anass.faircorp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.RingerController;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Ringer;
import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRingerFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.add_ringer_level)
    public SeekBar ringerLevel;

    @Bind(R.id.ringer_status)
    public Switch switchRinger;

    private Call<HashMap<String, Object>> ringerCall;
    private Room room;

    public static AddRingerFragment newInstance(Room room) {
        AddRingerFragment fragment = new AddRingerFragment();
        Bundle args = new Bundle();
        args.putSerializable("room",room);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ringer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ContextManagementActivity) getActivity();
        this.room = (Room) getArguments().getSerializable("room");

    }

    private void sendData(Ringer ringer) {

        if(Utils.isNetworkAvailable(activity)){

            RingerController ringerController = new RingerController();
            ringerCall = ringerController.addRinger(ringer);
            ringerCall.enqueue(new Callback<HashMap<String, Object>>() {
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
    @OnClick(R.id.save_ringer)
    public void saveRinger(){
        Log.i("saveRinger","saveRinger");
        Ringer ringer = new Ringer();
        ringer.setLevel(ringerLevel.getProgress());
        String sts = switchRinger.isChecked() ? "ON" : "OFF";
        ringer.setStatus(sts);
        ringer.setRoomId(room.getId());
        sendData(ringer);

    }

    @Nullable
    @OnClick(R.id.btn_close)
    public void backBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.onBackPressed();
    }
}
