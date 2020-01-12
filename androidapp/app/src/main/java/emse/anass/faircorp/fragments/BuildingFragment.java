package emse.anass.faircorp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import emse.anass.faircorp.API.Controllers.BuildingController;
import emse.anass.faircorp.Adapters.BuildingsAdapter;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.Helper.RecyclerItemTouchHelper;
import emse.anass.faircorp.Helper.Utils;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Building;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuildingFragment extends Fragment {

    ContextManagementActivity activity;

    @Bind(R.id.searchB)
    public SearchView searchView;

    @Bind(R.id.buildings_recyclerview)
    public RecyclerView mainRecycler;

    @Bind(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private Call<List<Building>> buildingCall;
    private List<Building> data;
    BuildingsAdapter buildingsAdapter;

    public static BuildingFragment newInstance() {
        BuildingFragment fragment = new BuildingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buildings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ContextManagementActivity) getActivity();
        initUI();
    }

    private void initUI() {
        configSearchView();
        Utils.configureRecycleView(getActivity(),mainRecycler);
        getData();
        configSwipeRefreshLayout();

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
    }


    private void getData() {


        if(Utils.isNetworkAvailable(activity)){

            final BuildingController buildingController = new BuildingController();
            buildingCall = buildingController.getBuildings();
            buildingCall.enqueue(new Callback<List<Building>>() {
                @Override
                public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {

                    data = response.body();

                    Log.i("buildingsResponses", "" +data);
                    if(data != null){

                        buildingsAdapter = new BuildingsAdapter(activity,data);
                        mainRecycler.setAdapter(buildingsAdapter);
                        buildingsAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<List<Building>> call, Throwable t) {
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

        List<Building> buildingList = new ArrayList<>(data);
        if(buildingsAdapter != null){
            buildingsAdapter.removeAllItems();
            if (s.isEmpty()) {
                buildingsAdapter.addAllItems(buildingList);
            }else {
                for (int i = 0; i < buildingList.size(); i++) {
                    if (buildingList.get(i).getName().toLowerCase().contains(s.toLowerCase())){
                        buildingsAdapter.addItem(buildingList.get(i));
                    }
                }
            }
        }
    }


    @Nullable
    @OnClick(R.id.btn_add)
    public void addNewBuilding(){
        activity.navigateTo(AddBuildingFragment.newInstance());
    }

}
