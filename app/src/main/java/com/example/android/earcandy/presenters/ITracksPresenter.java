package com.example.android.earcandy.presenters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;

import com.example.android.earcandy.models.Category;
import com.example.android.earcandy.models.SubCategory;
import com.example.android.earcandy.models.Track;

/**
 * Created by Mario on 8/14/2017.
 */
public interface ITracksPresenter {
    void initiate();

    Category[] getCategories();

    void imagerowFirstTouched(View v, final Category[] categories, int i);

    void secondRowTouched(View view, SubCategory subCategory, final Category[] categories, int i, int j);

    void onTrackClick(Track track, Context context, String pac);

    void controlTrack();

    void stopTrack();

    boolean isPlaying();
}
