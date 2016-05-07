package com.mr_apps.androidbase.adapter;

/**
 * Generic class that models an item of the AbstractHeaderFooterAdapter
 *
 * @author Mattia Ruggiero
 * @author Denis Brandi
 */
public class RecyclerItem<T> {

    boolean header;
    T item;
    boolean footer;

    /**
     * Constructor that takes parameters to determine if this item is an header or a footer, or is a "standard" item with generic content
     *
     * @param header true if this item is an header, false otherwise
     * @param item   the content of the item
     * @param footer true if this item is a footer, false otherwise
     */
    public RecyclerItem(boolean header, T item, boolean footer) {
        this.header = header;
        this.item = item;
        this.footer = footer;
    }

    /**
     * Getter for the header
     *
     * @return true if this item is an header, false otherwise
     */
    public boolean isHeader() {
        return header;
    }

    /**
     * Setter for the header
     *
     * @param header the value for the header
     */
    public void setHeader(boolean header) {
        this.header = header;
    }

    /**
     * Generic getter method for the item content
     *
     * @return the generic item content
     */
    public T getItem() {
        return item;
    }

    /**
     * Generic setter method for the item content
     *
     * @param item the generic item content to set
     */
    public void setItem(T item) {
        this.item = item;
    }

    /**
     * Getter for the footer
     *
     * @return true if this item is a footer, false otherwise
     */
    public boolean isFooter() {
        return footer;
    }

    /**
     * Setter for the footer
     *
     * @param footer the value for the footer
     */
    public void setFooter(boolean footer) {
        this.footer = footer;
    }
}
