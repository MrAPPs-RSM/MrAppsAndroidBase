package com.mr_apps.androidbase.tutorial;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Class that manages the adapter for the BaseTutorialActivity
 *
 * @author Denis Brandi
 */
public class TutorialAdapter extends FragmentStatePagerAdapter {

    Context context;
    final ItemTutorial[] tutorials;

    /**
     * Constructor that takes the fragment manager, the context and an optional parameter for the tutorial items
     *
     * @param fm        the fragment manager
     * @param context   the context
     * @param tutorials an optional parameter contains a variable number of tutorial items
     */
    public TutorialAdapter(FragmentManager fm, Context context, ItemTutorial... tutorials) {
        super(fm);
        this.context = context;
        this.tutorials = tutorials;
    }

    /**
     * Constructor that takes the fragment manager, the context and an optional parameter for the tutorial items
     *
     * @param fm        the fragment manager
     * @param context   the context
     * @param tutorials a list containing the tutorial items
     */
    public TutorialAdapter(FragmentManager fm, Context context, List<ItemTutorial> tutorials) {
        super(fm);
        this.context = context;

        this.tutorials = new ItemTutorial[tutorials.size()];

        for (int i = 0; i < tutorials.size(); i++) {
            this.tutorials[i] = tutorials.get(i);
        }
    }


    @Override
    public Fragment getItem(int position) {

        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(TutorialFragment.Field_Tutorial, tutorials[position]);
        tutorialFragment.setArguments(bundle);

        return tutorialFragment;
    }

    @Override
    public int getCount() {
        return tutorials == null ? 0 : tutorials.length;
    }
}
