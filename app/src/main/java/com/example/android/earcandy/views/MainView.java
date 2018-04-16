package com.example.android.earcandy.views;

import android.content.Context;
import android.view.View;

import com.example.android.earcandy.models.Category;
import com.example.android.earcandy.models.Track;

import java.util.ArrayList;

/**
 * Created by Mario on 8/13/2017.
 */

public interface MainView {

    Context getContext();

    void showTracks(Category[] categories);

    void hideSecondScroll(View v, final Category[] categories, int i);

    void showSecondScroll(View v, final Category[] categories, int i);

    void showThirdScroll(View view, ArrayList<Track> tracks, final Category[] categories, int i, int j);

    void hideThirdScroll(View view, final Category[] categories, int i, int j);
}
