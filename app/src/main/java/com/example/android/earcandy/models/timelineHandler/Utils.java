package com.example.android.earcandy.models.timelineHandler;

import android.app.Activity;
import android.content.Intent;

import com.example.android.earcandy.R;

/**
 * Created by Mark Mamdouh on 8/29/2017.
 */

public class Utils {

    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_RED = 1;
    public final static int THEME_YELLOW = 2;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_RED:
                activity.setTheme(R.style.Theme_App_Red);
                break;
            case THEME_YELLOW:
                activity.setTheme(R.style.Theme_App_Yellow);
                break;
        }
    }

}
