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

    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<HomeHorModel> list;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<HomeHorModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent,false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());

        if(check) {
            ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
            homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizza 1", "4.5"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizza 2", "4.5"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizza 3", "4.5"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizza 4", "4.5"));


            updateVerticalRec.callBack(position, homeVerModels);

            check = false;
        }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index = position;
                    notifyDataSetChanged();

                    if(position == 0){
                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                        homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizza 1", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizza 2", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizza 3", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizza 4", "4.5"));
                        updateVerticalRec.callBack(position, homeVerModels);
                    }
                    else if (position == 1){

                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                        homeVerModels.add(new HomeVerModel(R.drawable.burger1, "Pizza 5", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.burger2, "Pizza 6", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.burger4, "Pizza 8", "4.5"));
                        updateVerticalRec.callBack(position, homeVerModels);
                    }
                    else if (position == 2){

                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                        homeVerModels.add(new HomeVerModel(R.drawable.fries1, "Fies 1", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.fries2, "Fies 2", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.fries3, "Fies 3", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.fries4, "Fies 4", "4.5"));
                        updateVerticalRec.callBack(position, homeVerModels);
                    }
                    else if (position == 3){

                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                        homeVerModels.add(new HomeVerModel(R.drawable.icecream1, "Ice Cream 1", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.icecream2, "Ice Cream 2", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.icecream3, "Ice Cream 3", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.icecream4, "Ice Cream 4", "4.5"));
                        updateVerticalRec.callBack(position, homeVerModels);
                    }
                    else if (position == 4){

                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                        homeVerModels.add(new HomeVerModel(R.drawable.sandwich1, "Sandwich 1", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.sandwich2, "Sandwich 2", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.sandwich3, "Sandwich 3", "4.5"));
                        homeVerModels.add(new HomeVerModel(R.drawable.sandwich4, "Sandwich 4", "4.5"));
                        updateVerticalRec.callBack(position, homeVerModels);
                    }
                }
            });

            if(select){
                if(position == 0){
                    holder.cardView.setBackgroundResource(R.drawable.change_bg);
                    select = false;
                }
            }
            else {
                if(row_index == position){
                    holder.cardView.setBackgroundResource(R.drawable.change_bg);
                }else{
                    holder.cardView.setBackgroundResource(R.drawable.default_bg);
                }
            }
        }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
