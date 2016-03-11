package com.mr_apps.androidbase.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mr_apps.androidbase.R;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by denis on 11/02/16.
 */
public class FullImageAdapter extends PagerAdapter{

    Context context;
    final List<String> imagesPath;

    private int placeholderResId=-1;

    public FullImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.imagesPath = images;
    }

    public FullImageAdapter(Context context, int placeholderResId, List<String> images) {
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

        //View view= LayoutInflater.from(context).inflate(R.layout.image_touch, null);

        final ImageView imageView = new ImageView(context, null);//(ImageViewTouch)view.findViewById(R.id.image);//new ImageViewTouch(context);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        final PhotoViewAttacher mAttacher=new PhotoViewAttacher(imageView);

        imageView.setImageResource(placeholderResId);

        mAttacher.update();

        BitmapTypeRequest<String> request=Glide.with(context).load(imagesPath.get(position)).asBitmap();

        if(placeholderResId!=-1)
            request.error(placeholderResId).placeholder(placeholderResId);

        request.into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                imageView.setImageBitmap(resource);
                mAttacher.update();
                super.setResource(resource);
            }
        });

        //Glide.with(context).load(imagesPath.get(position)).error(R.drawable.placeholder_3x).placeholder(R.drawable.placeholder_3x).into(imageView);


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
