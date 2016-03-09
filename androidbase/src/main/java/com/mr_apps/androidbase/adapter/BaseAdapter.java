package com.mr_apps.androidbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by mattia on 09/03/2016.
 *
 * @author Mattia Ruggiero
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final ArrayList<T> items = new ArrayList<>();

    protected final Context context;
    protected final RecyclerViewListener recyclerViewListener;

    public BaseAdapter(Context context, RecyclerViewListener recyclerViewListener) {
        this.context = context;
        this.recyclerViewListener = recyclerViewListener;
    }

    public T getItem(int position) {
        if (items != null && items.size() > position)
            return items.get(position);
        else
            return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void modifyItem(T item, int i) {
        items.set(i, item);
        notifyItemChanged(i);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addItemInPosition(T item, int i) {
        items.add(i, item);
        notifyItemInserted(i);
    }

    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addItems(ArrayList<T> itemsToAdd, int positionToAdd) {
        items.addAll(positionToAdd, itemsToAdd);
        notifyDataSetChanged();
    }
}
