package com.mr_apps.androidbase.gallery;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mr_apps.androidbasegallery.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.share.GlobalShare;

import java.util.ArrayList;

/**
 * Class that manages the gallery activity, namely an activity that shows a gallery of images full screen
 *
 * @author Mattia Ruggiero
 */
public class GalleryActivity extends AbstractBaseActivity {

    public static final String Field_ImagesPath = "Field_ImagesPath";
    public static final String Field_Position = "Field_Position";
    public static final String Field_PlaceholderId = "Field_PlaceholderId";
    public static final String Field_CanShare = "Field_CanShare";

    ArrayList<String> images;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setBackButton();
        setEmptyTitle();

        images = getIntent().getStringArrayListExtra(Field_ImagesPath);

        int position = getIntent().getIntExtra(Field_Position, 0);

        setBackButton();

        viewPager = (ViewPager) findViewById(R.id.pager);

        FullImageAdapter adapter;

        int placeholderId = getIntent().getIntExtra(Field_PlaceholderId, 0);

        adapter = new FullImageAdapter(this, placeholderId, images);

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        if (getIntent().getBooleanExtra(Field_CanShare, false))
            menuInflater.inflate(R.menu.gallery_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.share) {
            if (checkOrRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, R.string.Titolo_permesso_obbligatorio, R.string.Messaggio_permesso_storage))
                shareImage();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens the chooser dialog where the user can select the method he likes to share the image
     */
    private void shareImage() {

        Glide.with(this).load(images.get(viewPager.getCurrentItem())).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                GlobalShare.share(GalleryActivity.this, null, resource);
            }
        });
    }

    @Override
    public void readStoragePermissionResult(boolean granted) {
        super.readStoragePermissionResult(granted);

        if (granted)
            shareImage();

    }

}
