package com.haith.cookingrecipeapp.ui.home;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;
    private final int pageSize;
    private static final long DEBOUNCE_DELAY_MS = 500; // Increase debounce to 0.7s to prevent rapid calls
    private long lastLoadTime = 0;

    protected PaginationScrollListener(LinearLayoutManager layoutManager, int pageSize) {
        this.layoutManager = layoutManager;
        this.pageSize = pageSize;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy <= 0) return; // Only load more when scrolling down

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        Log.d("Pagination", "Visible: " + visibleItemCount + " Total: " + totalItemCount + " FirstVisible: " + firstVisibleItemPosition);
        // Load more when reaching the bottom of the list
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= pageSize) {
                loadMoreItems();
            }
        }
    }


    protected abstract void loadMoreItems();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}
