package com.mr_apps.androidbaseproject;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.activity.CreditsActivity;
import com.mr_apps.androidbase.gallery.GalleryActivity;
import com.mr_apps.androidbaseutils.BitmapUtils;
import com.mr_apps.androidbaseutils.FileUtils;

import java.util.ArrayList;

public class MainActivity extends AbstractBaseActivity {

    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        preview = ((ImageView) findViewById(R.id.image));

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (preview.getDrawable() != null) {
                    ArrayList<String> paths = new ArrayList<>();

                    paths.add(path);

                    Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                    intent.putStringArrayListExtra(GalleryActivity.Field_ImagesPath, paths);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, getString(R.string.transition_gallery));
                    ActivityCompat.startActivity(MainActivity.this, intent, optionsCompat.toBundle());
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void credits(View v) {
        startActivity(new Intent(this, CreditsActivity.class));
    }

    public void login(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void tutorial(View v) {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    public void foto(View v) {
        manageCamera();
    }

    public void network(View v) {
        startActivity(new Intent(this, NetworkActivity.class));
    }

    private void manageCamera() {
        if (!checkOrRequestPermission(Manifest.permission.CAMERA, R.string.Titolo_permesso_obbligatorio, R.string.Messaggio_permesso_camera))
            return;

        showAlertChoice("Boia de", true, true, false, false);
    }

    @Override
    public void cameraPermissionResult(boolean granted) {
        super.cameraPermissionResult(granted);

        if (granted)
            manageCamera();
    }

    public void writeStoragePermissionResult(boolean granted) {
        super.writeStoragePermissionResult(granted);

        if (granted)
            manageCamera();

    }

    String path;

    @Override
    public void pickerResult(final String path, FileUtils.ElementType elementType, Bitmap bitmap) {
        super.pickerResult(path, elementType, bitmap);

        this.path = path;

        if (path == null || elementType == null)
            return;

        if (bitmap != null)
            preview.setImageBitmap(BitmapUtils.scaleBitmap(bitmap));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
