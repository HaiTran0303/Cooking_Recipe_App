package com.haith.cookingrecipeapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.models.HomeHorModel;
import com.haith.cookingrecipeapp.models.HomeVerModel;

import java.util.ArrayList;
import java.util.List;


public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {

    private final UpdateVerticalRec updateVerticalRec;
    private final ArrayList<HomeHorModel> list;
    private int row_index = -1;
    private int selectedPosition = -1;  // Changed to allow deselection

//    UpdateVerticalRec updateVerticalRec;
//    Activity activity;
//    ArrayList<HomeHorModel> list;

    boolean check = true;
    boolean select = true;
//    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, ArrayList<HomeHorModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getTypeName().toUpperCase());
        holder.imageView.setImageResource(list.get(position).getImage());

        // Set background and text color based on selection
        if (selectedPosition == position) {
            holder.cardView.setBackgroundResource(R.drawable.change_bg);
            holder.name.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        } else {
            holder.cardView.setBackgroundResource(R.drawable.default_bg);
            holder.name.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
        }


        holder.cardView.setOnClickListener(view -> {
            row_index = position;
            if(selectedPosition == position) {
                selectedPosition = -1;
                updateVerticalRec.onRecipeTypeSelected(null);
                // Show popular recipes when deselected
            } else {
                selectedPosition = position;
                updateVerticalRec.onRecipeTypeSelected(list.get(position).getTypeName());
            }
            notifyDataSetChanged();
        });
        }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearSelection() {
        selectedPosition = RecyclerView.NO_POSITION;   // Reset the selection index
        notifyDataSetChanged(); // Update the adapter to reflect the cleared selection
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
