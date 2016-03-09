package com.mr_apps.androidbase.gallery;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.share.GlobalShare;

import java.util.ArrayList;

/**
 * Created by denis on 11/02/16.
 */
public class GalleryActivity extends AbstractBaseActivity {

    public static final String Field_ImagesPath="Field_ImagesPath";
    public static final String Field_Position="Field_Position";
    public static final String Field_PlaceholderId="Field_PlaceholderId";

    ArrayList<String> images;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setBackButton();
        setEmptyTitle();

        images=getIntent().getStringArrayListExtra(Field_ImagesPath);

        int position=getIntent().getIntExtra(Field_Position, 0);

        setBackButton();

        viewPager=(ViewPager)findViewById(R.id.pager);

        FullImageAdapter adapter;

        int placeholderId=getIntent().getIntExtra(Field_PlaceholderId, 0);

        adapter=new FullImageAdapter(this, placeholderId, images);

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater= getMenuInflater();

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

    private void shareImage()
    {
        Ion.with(this).load(images.get(viewPager.getCurrentItem())).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {

                GlobalShare.share(GalleryActivity.this, null, result);

            }
        });
    }

    @Override
    public void readStoragePermissionResult(boolean granted) {
        super.readStoragePermissionResult(granted);

        if(granted)
            shareImage();

    }

}
