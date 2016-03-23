package com.mr_apps.androidbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattia on 09/03/2016.
 *
 * @author Mattia Ruggiero
 */
public abstract class AbstractBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final ArrayList<T> items = new ArrayList<>();

    protected AbstractBaseActivity activity;
    protected BaseFragment fragment;

    private final Context context;

    public AbstractBaseAdapter(AbstractBaseActivity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public AbstractBaseAdapter(BaseFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    public Context getContext() {
        return context;
    }

    public T getItem(int position) {
        if (items.size() > position)
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

    public void addItems(List<T> itemsToAdd, int positionToAdd) {
        items.addAll(positionToAdd, itemsToAdd);
        notifyDataSetChanged();
    }
}
