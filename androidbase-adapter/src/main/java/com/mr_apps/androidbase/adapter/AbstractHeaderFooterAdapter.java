package com.mr_apps.androidbase.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages an abstract base adapter with header and footer, that every adapter can extend
 * It provides method to add, remove and modify items from the list, and it also is generic so it is extensible by any type of adapter
 *
 * @author Mattia Ruggiero
 * @author Denis Brandi
 */
public abstract class AbstractHeaderFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER = 1;
    protected static final int TYPE_ITEM = 2;
    protected static final int TYPE_FOOTER = 3;

    protected final ArrayList<RecyclerItem> items = new ArrayList<>();

    protected AbstractBaseActivity activity;
    protected BaseFragment fragment;

    private final Context context;

    /**
     * Constructor that should be used when this adapter is used from an activity that extends the AbstractBaseActivity
     *
     * @param activity the instance of the AbstractBaseActivity
     */
    public AbstractHeaderFooterAdapter(AbstractBaseActivity activity) {
        this.activity = activity;
        this.context = activity;
    }

    /**
     * Constructor that should be used when this adapter is used from a fragment that extends the BaseFragment
     *
     * @param fragment the instance of the BaseFragment
     */
    public AbstractHeaderFooterAdapter(BaseFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    /**
     * Gets an item from the list
     *
     * @param position the position of the item
     * @return the item located at the given position
     */
    public RecyclerItem getItem(int position) {
        if (items != null && items.size() > position)
            return items.get(position);
        else
            return null;
    }

    /**
     * Gets the context associate to this class
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isHeader() ? TYPE_HEADER : items.get(position).isFooter() ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Gets all the items from the list
     *
     * @return an arrayList containing all the items of the list
     */
    public ArrayList<RecyclerItem> getItems() {
        return items;
    }

    /**
     * Removes an item from the list
     *
     * @param position the index of the item to remove
     */
    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Replaces an item of the list
     *
     * @param item the new item
     * @param i    the index of the item to be replaced
     */
    public void modifyItem(RecyclerItem item, int i) {
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
     * Adds an item at the given position. If the position where the item should be added contains a footer, the footer is automatically deleted.
     *
     * @param item the item to add
     * @param i    the position of the new item
     */
    public void addItemInPosition(RecyclerItem item, int i) {

        if (items.size() > 0 && items.get(items.size() - 1).isFooter()) {
            items.remove(items.size() - 1);
            i--;
        }

        items.add(i, item);
        notifyItemInserted(i);
    }

    /**
     * Adds an item to the end of the list. If the last item of the list is a footer, the footer is automatically deleted.
     *
     * @param item the item to add
     */
    public void addItem(RecyclerItem item) {
        if (items.size() > 0 && items.get(items.size() - 1).isFooter()) {
            items.remove(items.size() - 1);
        }

        items.add(item);
        notifyDataSetChanged();
    }

    /**
     * Adds a list of items at the given position. If the position where the items should be added contains a footer, the footer is automatically deleted.
     *
     * @param itemsToAdd    the list of items to add
     * @param positionToAdd the position where the items have to be added
     */
    public void addItems(List<RecyclerItem> itemsToAdd, int positionToAdd) {

        if (items.size() > 0 && items.get(items.size() - 1).isFooter()) {
            items.remove(items.size() - 1);
            positionToAdd--;
        }

        items.addAll(positionToAdd, itemsToAdd);
        notifyDataSetChanged();
    }

    /**
     * Removes the footer of the list
     */
    public void removeFooterProgress() {
        if (items.size() > 0 && items.get(items.size() - 1).isFooter())
            items.remove(items.size() - 1);

        notifyDataSetChanged();
    }
}
