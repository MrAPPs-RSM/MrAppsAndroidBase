package com.mr_apps.androidbase.tutorial;

import java.io.Serializable;

/**
 * Created by denis on 19/04/16.
 */
public class ItemTutorial implements Serializable{

    private int imageResId;
    private String title;
    private String subTitle;

    public ItemTutorial(int imageResId, String title, String subTitle) {
        this.imageResId=imageResId;
        this.title=title;
        this.subTitle=subTitle;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
