package com.mr_apps.androidbase.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by denis on 11/03/16.
 */
public abstract class RecyclerViewFooterScrollView<T> extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    boolean limit_reached=false;

    LinearLayoutManager layoutManager;
    AbstractHeaderFooterAdapter adapter;

    public RecyclerViewFooterScrollView(AbstractHeaderFooterAdapter adapter, LinearLayoutManager layoutManager)
    {
        this.adapter=adapter;
        this.layoutManager=layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (limit_reached || adapter==null || layoutManager==null)
            return;

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        //swipeContainer.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 || adapterProdotti.getItemCount() == 0);

        if (loading) {

            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem)) {
            // End has been reached

            //progressFooter.setVisibility(View.VISIBLE);

            if (adapter.getItemCount() > 0 && !adapter.getItem(adapter.getItemCount() - 1).isFooter()) {
                load();
                adapter.addItem(new RecyclerItem<T>(false, null, true));
            }

            loading = true;
        }
    }

    public abstract void load();

}
