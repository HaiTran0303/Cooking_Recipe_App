package com.haith.cookingrecipeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haith.cookingrecipeapp.DetailDailyRecipeActivity;
import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.models.DetailedDailyModel;
import com.haith.cookingrecipeapp.models.HomeVerModel;

import java.util.List;

public class DetailedDailyAdapter extends RecyclerView.Adapter<DetailedDailyAdapter.ViewHolder> {
    private List<DetailedDailyModel> list;
    private Context context;

    public DetailedDailyAdapter(Context context, List<DetailedDailyModel> recipes) {
        this.context = context;
        this.list = recipes;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list == null || list.size() <= position) {
            return; // Avoids potential crash
        }
        DetailedDailyModel model = list.get(position);
        // Construct the Spoonacular image URL
        String imageUrl = "https://spoonacular.com/cdn/ingredients_100x100/" + model.getImage();

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.icon_main_course)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView)
        ;
        // Set ingredient name
        holder.ingredient_name.setText(model.getName());

        // Set amount with unit
        holder.ingredient_quantity.setText(model.getAmount());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_daily_meal_item, parent, false)));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView ingredient_name, ingredient_quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            ingredient_name = itemView.findViewById(R.id.ingredient_name);
            ingredient_quantity = itemView.findViewById(R.id.ingredient_quantity);

        }
    }
}
