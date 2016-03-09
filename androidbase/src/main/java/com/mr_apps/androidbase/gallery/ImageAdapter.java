package com.mr_apps.androidbase.gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by denis on 11/02/16.
 */
public class ImageAdapter extends PagerAdapter {

    Context context;
    final ArrayList<String> imagesPath;
    private int placeholderResId;

    public ImageAdapter(Context context, int placeholderResId, ArrayList<String> images) {
        this.context = context;
        this.imagesPath = images;
        this.placeholderResId=placeholderResId;
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

        DrawableTypeRequest<String> request=Glide.with(context).load(imagesPath.get(position));

        if(placeholderResId!=0)
            request.error(placeholderResId).placeholder(placeholderResId);

        request.into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GalleryActivity.class);

                intent.putStringArrayListExtra(GalleryActivity.Field_ImagesPath, imagesPath);
                intent.putExtra(GalleryActivity.Field_Position, position);

                context.startActivity(intent);
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
