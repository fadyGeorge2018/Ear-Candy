package com.example.android.earcandy.models;


import java.io.Serializable;

/**
 * Created by Mario on 8/1/2017.
 */

public class Track implements IPlayable, Serializable {
    protected int mResId; // Resource id in the raw folder
    private final float DEFAULT_VOLUME = 1f; //max volume
    private Category mCategory;
    private SubCategory mSubCategory;
    private int duration; //in milliseconds
    private boolean mFadeIn = false;
    private boolean mFadeOut = false;
    private boolean favourite = false;
    private boolean repeat = false;
    protected int mFadeInDuration = 0; //by default no fade in
    protected int mFadeOutDuration = 0;//by default no fade out
    private float volume;
    private String name;
    private String url;
    private String uri;
    private int image;


    /**
     * Calculates the default fade time duration.
     *
     * @return 2000 ms if 2 sec is less than 10% of the duration of the track, else returns the 10% in ms.
     */
    protected int defaultFadeDuration() {
        return getDuration() * 0.1 < 2000 ? (int) (getDuration() * 0.1) : 2000;
    }

    public void setFadeOut(boolean mFadeOut) {
        this.mFadeOut = mFadeOut;
        if (mFadeOut) {
            this.mFadeOutDuration = defaultFadeDuration();
        } else {
            this.mFadeOutDuration = 0;
        }
    }

    public void setFadeIn(boolean mFadeIn) {
        this.mFadeIn = mFadeIn;
        if (mFadeIn) {
            this.mFadeInDuration = defaultFadeDuration();
        } else {
            this.mFadeInDuration = 0;
        }
    }

    public void setFavourite(boolean favourite){
        this.favourite = favourite;
    }

    public int getResId() {
        return mResId;
    }

    public Category getCategory() {
        return mCategory;
    }

    public SubCategory getSubCategory() {
        return mSubCategory;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isFadeIn() {
        return mFadeIn;
    }

    public boolean isFadeOut() {
        return mFadeOut;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public float getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }


    public Track(int mResId, SubCategory mSubCategory, String name, int duration) {
        this.mResId = mResId;
        this.mSubCategory = mSubCategory;
        this.name = name;
        this.duration = duration;
        this.volume = DEFAULT_VOLUME;
    }

    public int getImage() {
        return image;
    }

    public String getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
