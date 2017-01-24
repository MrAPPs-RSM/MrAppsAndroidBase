package com.mr_apps.androidbase.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbaseutils.utils.Utils;

/**
 * CLass that manages a fragment of the tutorial
 *
 * @author Denis Brandi
 */
public class TutorialFragment extends Fragment {

    public static final String Field_Tutorial="Field_Tutorial";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.item_tutorial, container, false);

        ItemTutorial itemTutorial= (ItemTutorial) getArguments().getSerializable(Field_Tutorial);

        ImageView img= (ImageView) rootView.findViewById(R.id.image_tutorial);

        TextView title=(TextView) rootView.findViewById(R.id.title);

        TextView subtitle=(TextView) rootView.findViewById(R.id.subtitle);

        if(itemTutorial.getImageResId()!=0)
            img.setImageResource(itemTutorial.getImageResId());
        title.setText(itemTutorial.getTitle());
        subtitle.setText(itemTutorial.getSubTitle());

        title.setVisibility(Utils.isNullOrEmpty(itemTutorial.getTitle())?View.GONE:View.VISIBLE);

        subtitle.setVisibility(Utils.isNullOrEmpty(itemTutorial.getSubTitle())?View.GONE:View.VISIBLE);

        return rootView;
    }
}
