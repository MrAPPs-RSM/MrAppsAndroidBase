package com.mr_apps.androidbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the abstract base adapter, namely the adapter that every other "standard" adapter should extend.
 * It provides method to add, remove and modify items from the list, and it also is generic so it is extensible by any type of adapter
 *
 * @author Mattia Ruggiero
 */
public abstract class AbstractBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final ArrayList<T> items = new ArrayList<>();

    protected AbstractBaseActivity activity;
    protected BaseFragment fragment;

    private final Context context;

    /**
     * Constructor that should be used when this adapter is used from an activity that extends the AbstractBaseActivity
     *
     * @param activity the instance of the AbstractBaseActivity
     */
    public AbstractBaseAdapter(AbstractBaseActivity activity) {
        this.activity = activity;
        this.context = activity;
    }

    /**
     * Constructor that should be used when this adapter is used from a fragment that extends the BaseFragment
     *
     * @param fragment the instance of the BaseFragment
     */
    public AbstractBaseAdapter(BaseFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    /**
     * Gets the context associate to this class
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Generic method that gets an item from the list
     *
     * @param position the position of the item
     * @return the item located at the given position
     */
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

    /**
     * Generic method that gets all the items from the list
     *
     * @return an arrayList containing all the items of the list
     */
    public ArrayList<T> getItems() {
        return items;
    }

    /**
     * Generic method that removes an item from the list
     *
     * @param position the index of the item to remove
     */
    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Generic method that replace an item of the list
     *
     * @param item the new item
     * @param i    the index of the item to be replaced
     */
    public void modifyItem(T item, int i) {
        items.set(i, item);
        notifyItemChanged(i);
    }

    /**
     * Deletes all the elements of the list
     */
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    /**
     * Generic method that adds an item at the given position
     *
     * @param item the item to add
     * @param i    the position of the new item
     */
    public void addItemInPosition(T item, int i) {
        items.add(i, item);
        notifyItemInserted(i);
    }

    /**
     * Generic method that adds an item to the end of the list
     *
     * @param item the item to add
     */
    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    /**
     * Generic method that adds a list of items at the given position
     *
     * @param itemsToAdd    the list of items to add
     * @param positionToAdd the position where the items have to be added
     */
    public void addItems(List<T> itemsToAdd, int positionToAdd) {
        items.addAll(positionToAdd, itemsToAdd);
        notifyDataSetChanged();
    }
}
