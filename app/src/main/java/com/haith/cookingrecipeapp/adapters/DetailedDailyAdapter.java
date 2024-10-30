package com.haith.cookingrecipeapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haith.cookingrecipeapp.DetailDailyRecipeActivity;
import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.models.DetailedDailyModel;

import java.util.List;

public class DetailedDailyAdapter extends RecyclerView.Adapter<DetailedDailyAdapter.ViewHolder> {
    List<DetailedDailyModel> list;

    public DetailedDailyAdapter(List<DetailedDailyModel> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.nameView.setText(list.get(position).getName());
        holder.desView.setText(list.get(position).getDescription());
        holder.ratingView.setText(list.get(position).getRating());
        holder.timingView.setText(list.get(position).getTiming());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, DetailDailyRecipeActivity);
//            }
//        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_daily_meal_item,parent,false)));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView,desView,ratingView,timingView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            nameView = itemView.findViewById(R.id.detailed_name);
            desView = itemView.findViewById(R.id.detailed_des);
            ratingView = itemView.findViewById(R.id.detailed_rating);
            timingView = itemView.findViewById(R.id.detailed_timing);
        }
    }
}
