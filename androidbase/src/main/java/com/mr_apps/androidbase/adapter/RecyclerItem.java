package com.mr_apps.androidbase.adapter;

/**
 * Created by mattia on 09/03/2016.
 *
 * @author Mattia Ruggiero
 */
public class RecyclerItem<T> {

    boolean header;
    T item;
    boolean footer;

    public RecyclerItem(boolean header, T item, boolean footer) {
        this.header = header;
        this.item = item;
        this.footer = footer;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }
}
