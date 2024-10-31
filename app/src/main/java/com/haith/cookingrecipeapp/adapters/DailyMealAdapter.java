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
import com.haith.cookingrecipeapp.models.DailyMealModel;

import java.util.List;

public class DailyMealAdapter extends RecyclerView.Adapter<DailyMealAdapter.ViewHolder> {

    Context context;
    List<DailyMealModel> list;

    public DailyMealAdapter(Context context, List<DailyMealModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list == null || list.size() <= position) {
            return; // Avoids potential crash
        }
        DailyMealModel model = list.get(position);


        // Use a default placeholder if image URL is null
        String imageUrl = model.getImage() != null ? model.getImage() : "";
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.icon_main_course)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        // Bind title and health score
        holder.name.setText(model.getDescription()); // Assuming getTitle() provides the title
        holder.description.setText("Health Score: " + String.valueOf(model.getHealScore())); // Convert health score to String

        // Set click listener to open DetailDailyRecipeActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailDailyRecipeActivity.class);
            intent.putExtra("recipeId", Integer.parseInt(model.getId())); // Assuming `getId()` returns a String
            intent.putExtra("title", model.getDescription());
            intent.putExtra("image", model.getImage());
            intent.putExtra("healthScore", model.getHealScore());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView name, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.imageView2);
            description = itemView.findViewById(R.id.desc);
        }


    }
}
