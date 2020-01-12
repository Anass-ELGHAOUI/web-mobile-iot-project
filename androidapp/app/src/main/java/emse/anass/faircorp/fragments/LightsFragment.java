package emse.anass.faircorp.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.LightController;
import emse.anass.faircorp.Adapters.LightAdapter;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Light;
import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LightsFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.lights_recyclerview)
    public RecyclerView mainRecycler;

    @Bind(R.id.swipeRefreshLayoutLights)
    public SwipeRefreshLayout swipeRefreshLayout;

    private Call<List<Light>> lightCall;
    private List<Light> data;
    private Room room;
    LightAdapter lightAdapter;

    public static LightsFragment newInstance(Room room) {
        LightsFragment fragment = new LightsFragment();
        Bundle args = new Bundle();
        args.putSerializable("room",room);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lights, container, false);
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

    private void initUI() {
        Utils.configureRecycleView(getActivity(),mainRecycler);
        getData();
        configSwipeRefreshLayout();
/*
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =  new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
                final BuildingController buildingController = new BuildingController();
                Call<HashMap<String, Object>> deleteCall = buildingController.deleteBuilding(buildingsAdapter.getSelectedItem(position).getId());
                deleteCall.enqueue(new Callback<HashMap<String, Object>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                        if(response != null){
                            buildingsAdapter.removeItem(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {

                    }
                });
            }
        });

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mainRecycler);

        */
    }


    private void getData() {


        if(Utils.isNetworkAvailable(activity)){

            final LightController lightController = new LightController();
            lightCall = lightController.getLights();
            lightCall.enqueue(new Callback<List<Light>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<List<Light>> call, Response<List<Light>> response) {

                    data = response.body();

                    Log.i("lightsResponses", "" +data);
                    if(data != null){
                        data = data.stream()
                                .filter(light->light.getRoomId() == room.getId())
                                .collect(Collectors.toList());
                        lightAdapter = new LightAdapter(activity,data);
                        mainRecycler.setAdapter(lightAdapter);
                        lightAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<List<Light>> call, Throwable t) {
                    Log.w("Retrofit", "Failure-getCustomers");
                    Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    private void configSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Nullable
    @OnClick(R.id.btn_back_lights)
    public void backBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.onBackPressed();
    }


    @Nullable
    @OnClick(R.id.btn_add_light)
    public void addNewLight(){
        activity.navigateTo(AddLightFragment.newInstance(room));
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

}
