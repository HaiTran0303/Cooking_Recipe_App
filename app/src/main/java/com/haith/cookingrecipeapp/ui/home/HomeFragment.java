package com.haith.cookingrecipeapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.adapters.HomeHorAdapter;
import com.haith.cookingrecipeapp.models.HomeHorModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView homeHorizontalRec;
    List<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);

        homeHorModelList = new ArrayList<>();

        homeHorModelList.add(new HomeHorModel(R.drawable.pizza1, "Pizza"));
        homeHorModelList.add(new HomeHorModel(R.drawable.burger1, "HamBurger"));
        homeHorModelList.add(new HomeHorModel(R.drawable.fries1, "Fries"));
        homeHorModelList.add(new HomeHorModel(R.drawable.icecream2, "Ice Cream"));
        homeHorModelList.add(new HomeHorModel(R.drawable.sandwich1, "Sandwich"));

        homeHorAdapter = new HomeHorAdapter(getActivity(), homeHorModelList);
        homeHorizontalRec.setAdapter(homeHorAdapter);

        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);

        return root;
    }
}
