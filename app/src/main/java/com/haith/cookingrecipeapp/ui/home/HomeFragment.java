package com.haith.cookingrecipeapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.adapters.HomeHorAdapter;
import com.haith.cookingrecipeapp.adapters.HomeVerAdapter;
import com.haith.cookingrecipeapp.adapters.UpdateVerticalRec;
import com.haith.cookingrecipeapp.models.HomeHorModel;
import com.haith.cookingrecipeapp.models.HomeVerModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements UpdateVerticalRec {

    RecyclerView homeHorizontalRec, homeVerticalRec;
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;

    ///////Vertical//////
    ArrayList<HomeVerModel> homeVerModelList;
    HomeVerAdapter homeVerAdapter;

    private TextView textHello;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase Auth and Firestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textHello = root.findViewById(R.id.textHello);

        // Call method to retrieve user data
        loadUserData();

        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        homeVerticalRec = root.findViewById(R.id.home_ver_rec);

        ////////////////Horizontal RecyclerView/////////////
        homeHorModelList = new ArrayList<>();

        homeHorModelList.add(new HomeHorModel(R.drawable.pizza1, "Pizza"));
        homeHorModelList.add(new HomeHorModel(R.drawable.burger1, "HamBurger"));
        homeHorModelList.add(new HomeHorModel(R.drawable.fries1, "Fries"));
        homeHorModelList.add(new HomeHorModel(R.drawable.icecream2, "Ice Cream"));
        homeHorModelList.add(new HomeHorModel(R.drawable.sandwich1, "Sandwich"));

        homeHorAdapter = new HomeHorAdapter(this, getActivity(), homeHorModelList);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);


        ////////////////Vertical RecyclerView/////////////
        homeVerModelList = new ArrayList<>();


        homeVerAdapter = new HomeVerAdapter(getActivity(), homeVerModelList);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        return root;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()) {
                            // Retrieve and set the user name
                            String userName = documentSnapshot.getString("name");
                            textHello.setText("Hello " + userName);
                        } else {
                            textHello.setText("Hello User");
                        }
                    }).addOnFailureListener(e -> {
                        textHello.setText("Hello User");
                    });
        } else {
            // If the user is not logged in, show a default message
            textHello.setText("Hello User");
        }

    }

    @Override
    public void callBack(int position, ArrayList<HomeVerModel> list) {

        homeVerAdapter = new HomeVerAdapter(getContext(), list);
        homeVerAdapter.notifyItemChanged(position);
        homeVerticalRec.setAdapter(homeVerAdapter);
    }
}
