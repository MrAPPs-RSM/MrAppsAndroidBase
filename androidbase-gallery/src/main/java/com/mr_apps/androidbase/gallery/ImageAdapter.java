package com.mr_apps.androidbase.gallery;

import android.content.Context;
import android.content.Intent;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.mr_apps.androidbasegallery.R;

import java.util.ArrayList;

/**
 * Class that manages an image adapter
 *
 * @author Denis Brandi
 */
public class ImageAdapter extends PagerAdapter {

    Context context;
    final ArrayList<String> imagesPath;
    boolean canShare;
    private int placeholderResId = -1;

    private boolean transition;

    /**
     * Constructor that takes the context, a list containing the images to show and a flag to set it the adapter can share images or not
     *
     * @param context  the context
     * @param images   the list containing the images to show
     * @param canShare a boolean value used to determine if the images are sharable or not
     */
    public ImageAdapter(Context context, ArrayList<String> images, boolean canShare) {
        this.context = context;
        this.imagesPath = images;
        this.canShare = canShare;
    }

    public ImageAdapter(Context context, ArrayList<String> images, boolean canShare, boolean transition) {
        this.context = context;
        this.imagesPath = images;
        this.canShare = canShare;
        this.transition = transition;
    }

    /**
     * Constructor that takes the context, the placeholder image to show when an image is not still loaded,
     * a list containing the images to show and a flag to set it the adapter can share images or not
     *
     * @param context          the context
     * @param placeholderResId the placeholder to show when an image is not still loaded
     * @param images           the list containing the images to show
     * @param canShare         a boolean value used to determine if the images are sharable or not
     */
    public ImageAdapter(Context context, int placeholderResId, ArrayList<String> images, boolean canShare) {
        this.context = context;
        this.imagesPath = images;
        this.placeholderResId = placeholderResId;
        this.canShare = canShare;
    }

    public ImageAdapter(Context context, int placeholderResId, ArrayList<String> images, boolean canShare, boolean transition) {
        this.context = context;
        this.imagesPath = images;
        this.placeholderResId = placeholderResId;
        this.canShare = canShare;
        this.transition = transition;
    }

    public void setTransition(boolean transition) {
        this.transition = transition;
    }

    @Override
    public int getCount() {
        return imagesPath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        ImageView imageView = new ImageView(context, null);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        DrawableTypeRequest<String> request = Glide.with(context).load(imagesPath.get(position));

        if (placeholderResId != -1)
            request.error(placeholderResId).placeholder(placeholderResId);

        request.into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GalleryActivity.class);

                intent.putStringArrayListExtra(GalleryActivity.Field_ImagesPath, imagesPath);
                intent.putExtra(GalleryActivity.Field_Position, position);
                intent.putExtra(GalleryActivity.Field_CanShare, canShare);

                if (transition) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity) context, v, context.getString(R.string.transition_gallery));
                    ActivityCompat.startActivity(context, intent, optionsCompat.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });

        container.addView(imageView);

        return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = (View) object;
        ((ViewPager) container).removeView((ImageView) object);
        view = null;
    }


}
