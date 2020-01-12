package emse.anass.faircorp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import emse.anass.faircorp.API.Controllers.LightController;
import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.R;
import emse.anass.faircorp.models.Light;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LightAdapter extends RecyclerView.Adapter<LightAdapter.ItemViewHolder> {


    private List<Light> itemsData;
    private ContextManagementActivity activity;
    private Call<HashMap<String, Object>> lightCall;
    private LightController lightController;


    public LightAdapter(ContextManagementActivity activity, List<Light> itemsData) {
        this.itemsData = new ArrayList<>(itemsData);
        this.activity = activity;
        lightController = new LightController();
    }


    @Override
    public LightAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.light_item, parent, false);
        return new ItemViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Light item = itemsData.get(position);
        if(item.getStatus().equals("ON")){
            holder.ivBulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_bulb_on));
        }else{
            holder.ivBulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_bulb_off));
        }
        holder.level.setProgress(item.getLevel());
        holder.color.setProgress(item.getColor());

        holder.ivBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getStatus().equals("ON")){
                    holder.ivBulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_bulb_off));
                    item.setStatus("OFF");
                }else{
                    holder.ivBulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_bulb_on));
                    item.setStatus("ON");
                }
                lightCall = lightController.switchLight(item.getId());
                lightCall.enqueue(new Callback<HashMap<String, Object>>() {
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
        });

        holder.level.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                item.setLevel(seekBar.getProgress());
                lightCall = lightController.changeLevel(item.getId(),item);
                lightCall.enqueue(new Callback<HashMap<String, Object>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {

                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                        Log.w("Retrofit", "Failure-getCustomers");
                        Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(activity,""+seekBar.getProgress(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.color.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                item.setColor(seekBar.getProgress());
                lightCall = lightController.changeColor(item.getId(),item);
                lightCall.enqueue(new Callback<HashMap<String, Object>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {

                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                        Log.w("Retrofit", "Failure-getCustomers");
                        Toast.makeText(activity, "An error has occured", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(activity,""+seekBar.getProgress(),Toast.LENGTH_SHORT).show();            }
        });
    }


    @Override
    public int getItemCount() {
        return itemsData.size();
    }


    public void removeAllItems() {
        itemsData.clear();
        notifyDataSetChanged();
    }


    public Light getSelectedItem(int position) {
        return itemsData.get(position);
    }

    public void addItem(Light item) {
        itemsData.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int indice){
        itemsData.remove(indice);
        notifyItemRemoved(indice);
    }

    public void addAllItems(List<Light> lights){
        itemsData.addAll(lights);
        notifyDataSetChanged();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivBulb;
        public SeekBar level;
        public SeekBar color;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivBulb = itemView.findViewById(R.id.iv_light);
            level = itemView.findViewById(R.id.sb_level);
            color = itemView.findViewById(R.id.sb_color);
        }

        @Override
        public void onClick(View view) {

        }


    }
}
