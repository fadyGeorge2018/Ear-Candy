package com.example.android.earcandy.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.android.earcandy.R;

/**
 * Created by Mark Mamdouh on 9/2/2017.
 */

public class Pop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));
    }
}
