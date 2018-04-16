package com.example.android.earcandy.models;

/**
 * Created by Mario on 8/1/2017.
 */

public interface IPlayable {
    int getResId();
    float getVolume();
    boolean isFadeIn();
    boolean isFadeOut();
    int getDuration();
    SubCategory getSubCategory();
    String getName();
    void setFadeIn(boolean fadeIn);
    void setFadeOut(boolean fadeOut);
}
