package com.haith.cookingrecipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.models.InstructionStepModel;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {
    private List<InstructionStepModel> steps;
    private Context context;

    public InstructionAdapter(Context context, List<InstructionStepModel> steps) {
        this.context = context;
        this.steps = steps;
    }


    @NonNull
    @Override
    public InstructionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_step_item, parent, false)));
    }
    @Override
    public void onBindViewHolder(@NonNull InstructionAdapter.ViewHolder holder, int position) {
        InstructionStepModel step = steps.get(position);
        String stepContent = step.getStepContent();
        holder.stepContent.setText(stepContent);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView stepContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stepContent = itemView.findViewById(R.id.step_content);
        }
    }
}
