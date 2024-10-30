package com.haith.cookingrecipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.models.HomeVerModel;

import java.util.ArrayList;
import java.util.List;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {

    Context context;
    ArrayList<HomeVerModel> list;

    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HomeVerModel model = list.get(position);
        Glide.with(context)
                .load(model.getImage())
                .placeholder(R.drawable.icon_main_course)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_food)


        ;
        holder.textView_title.setText(model.getName());
        holder.textView_likes.setText(model.getAggregateLikes()+" Likes");
//        holder.textView_servings.setText(model.getServings()+" Persons");
        holder.textView_times.setText(model.getReadyInMinutes()+" Min");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView random_list_container;
        TextView textView_title, textView_likes, textView_servings, textView_times;
        ImageView imageView_food;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            random_list_container = itemView.findViewById(R.id.random_list_container);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_likes = itemView.findViewById(R.id.textView_likes);
//            textView_servings = itemView.findViewById(R.id.textView_servings);
            textView_times = itemView.findViewById(R.id.textView_times);
            imageView_food = itemView.findViewById(R.id.imageView_food);
        }
    }
}
