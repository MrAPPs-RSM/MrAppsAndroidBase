package com.mr_apps.androidbase.adapter;

import android.support.v7.widget.RecyclerView;

import com.mr_apps.androidbase.activity.AbstractBaseActivity;

import java.util.ArrayList;

/**
 * Created by mattia on 09/03/2016.
 *
 * @author Mattia Ruggiero
 */
public abstract class AbstractHeaderFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER = 1;
    protected static final int TYPE_ITEM = 2;
    protected static final int TYPE_FOOTER = 3;

    protected final ArrayList<RecyclerItem> items = new ArrayList<>();

    protected final AbstractBaseActivity context;
    protected final RecyclerViewListener recyclerViewListener;

    public AbstractHeaderFooterAdapter(AbstractBaseActivity context, RecyclerViewListener recyclerViewListener) {
        this.context = context;
        this.recyclerViewListener = recyclerViewListener;
    }

    public RecyclerItem getItem(int position) {
        if (items != null && items.size() > position)
            return items.get(position);
        else
            return null;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isHeader() ? TYPE_HEADER : items.get(position).isFooter() ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<RecyclerItem> getItems() {
        return items;
    }

    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void modifyItem(RecyclerItem item, int i) {
        items.set(i, item);
        notifyItemChanged(i);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addItemInPosition(RecyclerItem item, int i) {
        items.add(i, item);
        notifyItemInserted(i);
    }

    public void addItem(RecyclerItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addItems(ArrayList<RecyclerItem> itemsToAdd, int positionToAdd) {
        items.addAll(positionToAdd, itemsToAdd);
        notifyDataSetChanged();
    }
}
