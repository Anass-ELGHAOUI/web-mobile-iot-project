package emse.anass.faircorp.fragments;

import android.annotation.TargetApi;
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
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.RoomController;
import emse.anass.faircorp.Adapters.RoomAdapter;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Building;
import emse.anass.faircorp.models.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomsFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.searchR)
    public SearchView searchView;

    @Bind(R.id.rooms_recyclerview)
    public RecyclerView mainRecycler;

    @Bind(R.id.swipeRefreshLayoutRooms)
    public SwipeRefreshLayout swipeRefreshLayout;

    private Call<List<Room>> roomCall;
    private List<Room> data;
    private Building building;
    RoomAdapter roomAdapter;

    public static RoomsFragment newInstance(Building building) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putSerializable("building",building);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ContextManagementActivity) getActivity();
        this.building = (Building) getArguments().get("building");
        initUI();
    }

    private void initUI() {
        configSearchView();
        Utils.configureRecycleView(getActivity(),mainRecycler);
        getData();
        configSwipeRefreshLayout();
    }


    private void getData() {


        if(Utils.isNetworkAvailable(activity)){

            final RoomController roomController = new RoomController();
            roomCall = roomController.getRooms();
            roomCall.enqueue(new Callback<List<Room>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {

                    data = response.body();

                    Log.i("RoomsResponses", "" +data);
                    if(data != null){
                        data = data.stream()
                                .filter(room->room.getBuildingId() == building.getId())
                                .collect(Collectors.toList());

                        roomAdapter = new RoomAdapter(activity,data);
                        mainRecycler.setAdapter(roomAdapter);
                        roomAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable t) {
                    Log.w("Retrofit", "Failure-getCustomers");
                    Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    private void configSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                searchView.clearFocus();
                Utils.hideSoftKeyboard(getActivity());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
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

    public void filterList(String s){

        List<Room> roomsList = new ArrayList<>(data);
        if(roomAdapter != null){
            roomAdapter.removeAllItems();
            if (s.isEmpty()) {
                roomAdapter.addAllItems(roomsList);
            }else {
                for (int i = 0; i < roomsList.size(); i++) {
                    if (roomsList.get(i).getName().toLowerCase().contains(s.toLowerCase())){
                        roomAdapter.addItem(roomsList.get(i));
                    }
                }
            }
        }
    }


    @Nullable
    @OnClick(R.id.btn_add_room)
    public void addNewRoom(){
        activity.navigateTo(AddRoomFragment.newInstance());
    }

    @Nullable
    @OnClick(R.id.btn_back_rooms)
    public void backBtnClick(){
        Utils.hideSoftKeyboard(getActivity());
        activity.onBackPressed();
    }
}
