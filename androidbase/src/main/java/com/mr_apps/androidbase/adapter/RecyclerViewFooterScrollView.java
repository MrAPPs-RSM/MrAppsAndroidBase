package com.mr_apps.androidbase.adapter;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Class that manage a scroll listener for the recycler view when the last element is a footer (should be a progress) that has to be managed while scrolling
 *
 * @author Denis Brandi
 */
public abstract class RecyclerViewFooterScrollView<T> extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    private boolean limit_reached = false;

    LinearLayoutManager layoutManager;
    AbstractHeaderFooterAdapter adapter;

    /**
     * Constructor that takes the adapter and the layout manager of the recycler view
     *
     * @param adapter       the adapter of the recycler view
     * @param layoutManager the layout manager of the recycler view
     */
    public RecyclerViewFooterScrollView(AbstractHeaderFooterAdapter adapter, LinearLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (limit_reached || adapter == null || layoutManager == null)
            return;

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

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
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItem(new RecyclerItem<T>(false, null, true));
                    }
                });
            }

            loading = true;
        }
    }

    /**
     * Setter for the loading field, that determines if the list is loading or not
     *
     * @param loading the new value of the loading field
     */
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    /**
     * Setter for the limit_reached field, which determines if the limit of the list is reached or not
     *
     * @param limit_reached the new value of the limit_reached field
     */
    public void setLimit_reached(boolean limit_reached) {
        this.limit_reached = limit_reached;
    }

    /**
     * Setter for the previousTotal field, that keeps track of the last total of elements
     *
     * @param previousTotal the new value of the previousTotal field
     */
    public void setPreviousTotal(int previousTotal) {
        this.previousTotal = previousTotal;
    }

    /**
     * Abstract method that has to be implemented to determine the actions to do when the list is scrolled until the end
     */
    public abstract void load();

}
