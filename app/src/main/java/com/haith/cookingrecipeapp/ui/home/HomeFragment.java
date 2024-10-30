package com.haith.cookingrecipeapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.adapters.HomeHorAdapter;
import com.haith.cookingrecipeapp.adapters.HomeVerAdapter;
import com.haith.cookingrecipeapp.adapters.UpdateVerticalRec;
import com.haith.cookingrecipeapp.dao.RetrofitClient;
import com.haith.cookingrecipeapp.dao.SpoonacularApiService;
import com.haith.cookingrecipeapp.models.ApiModels.Recipe;
import com.haith.cookingrecipeapp.models.ApiModels.RecipeResponse;
import com.haith.cookingrecipeapp.models.ApiModels.RecipeSuggestion;
import com.haith.cookingrecipeapp.models.HomeHorModel;
import com.haith.cookingrecipeapp.models.HomeVerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import android.widget.Toast;


public class HomeFragment extends Fragment implements UpdateVerticalRec {

    private RecyclerView homeHorizontalRec, homeVerticalRec;
    private ArrayList<HomeHorModel> homeHorModelList;
    private HomeHorAdapter homeHorAdapter;
    private ArrayList<HomeVerModel> homeVerModelList;
    private HomeVerAdapter homeVerAdapter;
    private final String API_KEY = getString(R.string.api_key);

    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int PAGE_SIZE = 10;
    private String selectedRecipeType = null;
    private boolean initialLoadComplete = false;
    private boolean isScrollListenerAdded = false;


    private Map<String, Map<Integer, List<HomeVerModel>>> recipeCache = new HashMap<>();
    private SpoonacularApiService apiService;
    private TextView textHello;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private EditText searchText;
    private List<RecipeSuggestion> suggestionList = new ArrayList<>();
    private List<String> suggestionTitles = new ArrayList<>(); // List to store titles for display in the popup


//    private List<String> suggestionList = new ArrayList<>();
    private ListPopupWindow suggestionPopup;

    private final Handler debounceHandler = new Handler();
    private Runnable debounceRunnable;
    private static final long DEBOUNCE_DELAY_MS = 300; // 300ms delay for debouncing

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        searchText = root.findViewById(R.id.searchText);
        setupSuggestionPopup();
        // Set up text listener for autocomplete
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2) {
                    debounceHandler.removeCallbacks(debounceRunnable);
                    debounceRunnable = () -> fetchAutocompleteSuggestions(charSequence.toString());
                    debounceHandler.postDelayed(debounceRunnable, DEBOUNCE_DELAY_MS);
                } else {
                    suggestionPopup.dismiss();
                }
                homeHorAdapter.clearSelection();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchText.getText().toString()); // Pass the query from the EditText
                // Clear suggestion list and dismiss popup after search
                suggestionList.clear();
                suggestionTitles.clear();
                suggestionPopup.dismiss();
                return true;
            }
            return false;
        });




            // Initialize SwipeRefreshLayout
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            resetToDefaultState(); // Reset the view to its default state
            swipeRefreshLayout.setRefreshing(false); // End the refresh animation
        });

        // Initialize Firebase Auth and Firestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        textHello = root.findViewById(R.id.textHello);
        // Call method to retrieve user data
        loadUserData();


        // Set up RecyclerView and adapter
        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        homeVerticalRec = root.findViewById(R.id.home_ver_rec);
        // Initialize Retrofit API service
        apiService = RetrofitClient.getApiService();

        setupHorizontalRecyclerView();
        setupVerticalRecyclerView();

        // Add scroll listener if not added already
        if (!isScrollListenerAdded && homeVerticalRec.getLayoutManager() instanceof LinearLayoutManager ) {
            homeVerticalRec.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) homeVerticalRec.getLayoutManager(), PAGE_SIZE) {
                @Override
                protected void loadMoreItems() {
                    Log.d("PaginationScrollListener", "Loading more items...");
                    isLoading = true; // Set to true when loading starts
                    currentPage++;
                    loadRecipes(currentPage); // Load the next page
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });
            isScrollListenerAdded = true;
        }



        //Initial load
        if (!initialLoadComplete) {
            fetchPopularRecipes(currentPage);
            initialLoadComplete = true;
        }
        return root;
    }
    private void setupSuggestionPopup() {
        // Initialize ListPopupWindow and set it to anchor to the search EditText
        suggestionPopup = new ListPopupWindow(requireContext());
        suggestionPopup.setAnchorView(searchText);

        // Set initial width and height for the ListPopupWindow
        suggestionPopup.setWidth(ListPopupWindow.WRAP_CONTENT);
        suggestionPopup.setHeight(ListPopupWindow.WRAP_CONTENT);

        // Set item click listener to handle suggestion selection
        suggestionPopup.setOnItemClickListener((parent, view, position, id) -> {
            searchText.setText(suggestionTitles.get(position));
            searchText.setSelection(searchText.getText().length());
            performSearch(suggestionTitles.get(position));
            suggestionPopup.dismiss();
        });
    }
    private void performSearch(String query) {
        // Hide the keyboard after the search
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
        }
        // Clear suggestion list and dismiss popup after search
        suggestionList.clear();
        suggestionTitles.clear();
        suggestionPopup.dismiss();
        // Execute the search with the query
        fetchRecipesByQuery(query);
    }

    private void fetchRecipesByQuery(String query) {
        Call<RecipeResponse> call = apiService.searchRecipesByQuery(API_KEY, query, PAGE_SIZE, currentPage,true);
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().results;
                    cacheAndDisplayRecipes("search", recipes, currentPage);
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch recipes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAutocompleteSuggestions(String query) {
        Call<List<RecipeSuggestion>> call = apiService.getAutocompleteRecipeNames(API_KEY, query, 10);
        call.enqueue(new Callback<List<RecipeSuggestion>>() {
            @Override
            public void onResponse(Call<List<RecipeSuggestion>> call, Response<List<RecipeSuggestion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    suggestionList.clear();
                    suggestionTitles.clear();
                    suggestionList.addAll(response.body());
                    for (RecipeSuggestion suggestion : suggestionList) {
                        suggestionTitles.add(suggestion.getTitle()); // Only add titles to display
                    }
                    updateSuggestionPopup(); // Update and show the popup

                }
            }

            @Override
            public void onFailure(Call<List<RecipeSuggestion>> call, Throwable t) {
                Log.e("Autocomplete", "Failed to fetch suggestions", t);
            }
        });


    }
    private void updateSuggestionPopup() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, suggestionTitles);
        suggestionPopup.setAdapter(adapter);

        if (!suggestionTitles.isEmpty()) {
            suggestionPopup.show();
        } else {
            suggestionPopup.dismiss();
        }
    }



    private void resetToDefaultState() {
        if (!isLoading) {
            searchText.setText("");
            selectedRecipeType = null;
            currentPage = 0;
            isLastPage = false;
            homeVerModelList.clear();
            recipeCache.clear(); // Clear cache to force new data load
            homeHorAdapter.clearSelection();
            fetchPopularRecipes(currentPage);
        }
    }

    private void loadRecipes(int page) {
        if (selectedRecipeType == null) {
            fetchPopularRecipes(page);
        } else {
            fetchRecipesByType(selectedRecipeType, page);
        }
        Log.d("Pagination", "loadRecipes called - Page: " + page);
        Log.d("Pagination", "Flags - isLoading: " + isLoading + ", isLastPage: " + isLastPage);
    }

    private void setupHorizontalRecyclerView() {
        homeHorModelList = new ArrayList<>();
        homeHorModelList.add(new HomeHorModel("main course", R.drawable.icon_main_course));
        homeHorModelList.add(new HomeHorModel("side dish", R.drawable.icon_side_dish));
        homeHorModelList.add(new HomeHorModel("appetizer", R.drawable.icon_appetizer));
        homeHorModelList.add(new HomeHorModel("bread", R.drawable.icon_bread));
        homeHorModelList.add(new HomeHorModel("beverage", R.drawable.icon_beverage));
        homeHorModelList.add(new HomeHorModel("breakfast", R.drawable.icon_breakfast));
        homeHorModelList.add(new HomeHorModel("snack", R.drawable.icon_snack));
        homeHorModelList.add(new HomeHorModel("soup", R.drawable.icon_soup));
        homeHorModelList.add(new HomeHorModel("dessert", R.drawable.icon_dessert));

        homeHorAdapter = new HomeHorAdapter(this, homeHorModelList);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
    }

    private void setupVerticalRecyclerView() {
        homeVerModelList = new ArrayList<>(); // Initialize empty list
        homeVerAdapter = new HomeVerAdapter(getActivity(), homeVerModelList);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
    }


    @Override
    public void onRecipeTypeSelected(String type) {
        selectedRecipeType = type;
        refreshData(); // Refresh the data based on the new type
    }

    private void refreshData() {
        currentPage = 0;
        isLastPage = false;
        homeVerModelList.clear();
        loadRecipes(currentPage);
    }


    private void fetchPopularRecipes(int page) {
        String cacheKey = "popular";
        if (recipeCache.containsKey(cacheKey) && recipeCache.get(cacheKey).containsKey(page)) {
            // Retrieve the specific list of recipes for the page
            homeVerModelList.addAll(recipeCache.get(cacheKey).get(page));
            homeVerAdapter.notifyDataSetChanged();
            isLoading = false;
            return;
        }

        int offset = page * PAGE_SIZE;
        Call<RecipeResponse> call = apiService.getPopularRecipes(
                API_KEY, "popularity", "desc", offset, PAGE_SIZE, true
        );
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().results;
                    cacheAndDisplayRecipes(cacheKey, recipes, page);
                    isLoading = false; // Reset loading flag here
                    if (recipes.size() < PAGE_SIZE) {
                        isLastPage = true;
                    }
//
//                    updateVerticalRecyclerView(recipes);
//                    isLoading = false;
//                    if (recipes.size() < PAGE_SIZE) {
//                        isLastPage = true;
//                    }
                } else {
                    isLoading = false;
                }
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.e("HomeFragment", "Failed to fetch popular recipes", t);
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Failed to fetch popular recipes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecipesByType(String typeName,int page) {
        String cacheKey = typeName;

        if (recipeCache.containsKey(cacheKey) && recipeCache.get(cacheKey).containsKey(page)) {
            // Retrieve the specific list of recipes for the page
            homeVerModelList.addAll(recipeCache.get(cacheKey).get(page));
            homeVerAdapter.notifyDataSetChanged();
            isLoading = false;
            return;
        }

        isLoading = true;
        int offset = page * PAGE_SIZE;

        Call<RecipeResponse> call = apiService
                .getRecipesByType(typeName,API_KEY,offset,PAGE_SIZE,true,"popularity","desc");
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().results;
                    cacheAndDisplayRecipes(cacheKey, recipes, page);

//                    updateVerticalRecyclerView(recipes);
//                    isLoading = false;
//                    if (recipes.size() < PAGE_SIZE) {
//                        isLastPage = true;
//                    }

                } else {
                    Toast.makeText(getContext(), "No recipes found for this type", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), "Failed to fetch recipes", Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });

    }

    private void cacheAndDisplayRecipes(String type, List<Recipe> recipes, int page) {
        if (page == 0) {
            homeVerModelList.clear();
            isLastPage = false; // Reset last page flag for fresh load

        }
        List<HomeVerModel> models = new ArrayList<>();
        for (Recipe recipe : recipes) {
            models.add(new HomeVerModel(
                    recipe.image, recipe.title,
                    String.valueOf(recipe.servings),
                    String.valueOf(recipe.aggregateLikes),
                    String.valueOf(recipe.readyInMinutes)
            ));
        }

        homeVerModelList.addAll(models);

        // Cache the results by type and page
        recipeCache.computeIfAbsent(type, k -> new HashMap<>()).put(page, models);

        homeVerAdapter.notifyDataSetChanged();
        isLoading = false;
        if (recipes.size() < PAGE_SIZE) {
            isLastPage = true;
        }

    }

    private void updateVerticalRecyclerView(List<Recipe> recipes) {
        if (currentPage == 0) {
            homeVerModelList.clear(); // Clear only if this is the first page
        }
        for (Recipe recipe : recipes) {
            String imageUrl = recipe.image;
            String title = recipe.title;
            String servings = String.valueOf(recipe.servings);
            String aggregateLikes = String.valueOf(recipe.aggregateLikes);
            String readyInMinutes = String.valueOf(recipe.readyInMinutes);

            homeVerModelList.add(new HomeVerModel(imageUrl, title, servings, aggregateLikes, readyInMinutes));
        }
        homeVerAdapter.notifyDataSetChanged();
    }


//    @Override
//    public void onRecipeTypeSelected(String type) {
//        fetchRecipesByType(type);
//    }

//    private void fetchRecipesByType(String typeName) {
//        SpoonacularApiService spoonacularApiService = RetrofitClient.getApiService();
//        Call<DocumentsContract.Root> call = apiService.getRecipesByType(type, API_KEY);
//        call.enqueue(new Callback {
//
//        });
//
//    }

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

//    @Override
//    public void callBack(int position, ArrayList<HomeVerModel> list) {
//
//        homeVerAdapter = new HomeVerAdapter(getContext(), list);
//        homeVerAdapter.notifyItemChanged(position);
//        homeVerticalRec.setAdapter(homeVerAdapter);
//    }


}
