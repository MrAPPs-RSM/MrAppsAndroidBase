package com.mr_apps.androidbase.tutorial;

import java.io.Serializable;

/**
 * Class that manages an item of the BaseTutorialActivity
 *
 * @author Denis Brandi
 */
public class ItemTutorial implements Serializable{

    private int imageResId;
    private String title;
    private String subTitle;

    /**
     * Constructor that takes the image of the item, the title and the subtitle
     *
     * @param imageResId the res Id of the image to show for this item
     * @param title the title of this item
     * @param subTitle the subtitle of this item
     */
    public ItemTutorial(int imageResId, String title, String subTitle) {
        this.imageResId=imageResId;
        this.title=title;
        this.subTitle=subTitle;
    }

    /**
     * Getter for the imageResId field
     *
     * @return the image of the item
     */
    public int getImageResId() {
        return imageResId;
    }

    /**
     * Setter for the imageResId field
     *
     * @param imageResId the new image for the item
     */
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    /**
     * Getter for the title field
     *
     * @return the title of the item
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title field
     *
     * @param title the new title for the item
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the subtitle field
     *
     * @return the subtitle of the item
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * Setter for the subtitle field
     *
     * @param subTitle the new subtitle for the item
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
