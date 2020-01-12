package emse.anass.faircorp.Adapters;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import emse.anass.faircorp.ContextManagementActivity;
import emse.anass.faircorp.R;
import emse.anass.faircorp.fragments.RoomsFragment;
import emse.anass.faircorp.models.Building;

public class BuildingsAdapter extends RecyclerView.Adapter<BuildingsAdapter.ItemViewHolder> {


    private List<Building> itemsData;
    private ContextManagementActivity activity;


    public BuildingsAdapter(ContextManagementActivity activity, List<Building> itemsData) {
        this.itemsData = new ArrayList<>(itemsData);
        this.activity = activity;
    }


    @Override
    public BuildingsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.building_item, parent, false);
        return new ItemViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Building item = itemsData.get(position);
        holder.nameTxt.setText(item.getName() + " (" + item.getNbOfFloors() + " floors)");
    }


    @Override
    public int getItemCount() {
        return itemsData.size();
    }


    public void removeAllItems() {
        itemsData.clear();
        notifyDataSetChanged();
    }


    public Building getSelectedItem(int position) {
        return itemsData.get(position);
    }

    public void addItem(Building item) {
        itemsData.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int indice){
        itemsData.remove(indice);
        notifyItemRemoved(indice);
    }

    public void addAllItems(List<Building> buildings){
        itemsData.addAll(buildings);
        notifyDataSetChanged();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTxt;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameTxt = itemView.findViewById(R.id.building_name);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Building building = getSelectedItem(position);

            Log.i("Building", "" + building.getName());
            highlight(view);
            activity.navigateTo(RoomsFragment.newInstance(itemsData.get(position)));
        }

        private void highlight(final View view) {
            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.selected_item));
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }
            }, 100);
        }
    }
}
