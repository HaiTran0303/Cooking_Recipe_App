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
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.imageView.setImageResource(list.get(i).getImage());
        holder.name.setText(list.get(i).getName());
        holder.description.setText(list.get(i).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailDailyRecipeActivity.class);
                intent.putExtra("type", list.get(i).getType()); // pass the type
                intent.putExtra("title", list.get(i).getName()); // pass the title
                intent.putExtra("imageView", list.get(i).getImage()); // pass the image id
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imageDetailView;
        TextView name, description, nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.imageView2);
            description = itemView.findViewById(R.id.desc);
            nameTextView = itemView.findViewById(R.id.custom_title);
            imageDetailView = itemView.findViewById(R.id.detailed_image);
        }


    }
}
