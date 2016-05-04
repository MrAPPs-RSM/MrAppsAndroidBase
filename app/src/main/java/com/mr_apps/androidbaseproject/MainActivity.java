package com.mr_apps.androidbaseproject;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.utils.FileUtils;

public class MainActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public void pickerResult(final String path, FileUtils.ElementType elementType, Bitmap bitmap) {
        super.pickerResult(path, elementType, bitmap);

        if (path == null || elementType == null)
            return;

        if (bitmap != null)
            ((ImageView) findViewById(R.id.image)).setImageBitmap(FileUtils.scaleBitmap(bitmap));
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
