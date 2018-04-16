package com.example.android.earcandy.models.timelineHandler;

import android.content.Context;

import com.example.android.earcandy.models.IPlayable;
import com.example.android.earcandy.models.SubCategory;
import com.example.android.earcandy.models.Track;

/**
 * Created by Mario on 8/1/2017.
 */

public class TimelineItem extends Track implements IScheduledPlayable {
    private IPlayer mPlayer;
    private int mStartTime;
    private int mMediaQueuerId;
    private Context mContext;
    private int mSkippedFromStart;
    private int mSkippedFromEnd;
    private final int FADE_IN = 1;
    private final int FADE_OUT = -1;


    public int getmFadeInDuration() {
        return mFadeInDuration;
    }

    public int getmFadeOutDuration() {
        return mFadeOutDuration;
    }

    public void setmFadeOutDuration(int mFadeOutDuration) {
        if (isOverlapping(mFadeOutDuration,FADE_OUT)) {
            this.mFadeOutDuration = getMaxFadeOutDuration();
        }
        this.mFadeOutDuration = mFadeOutDuration;
    }

    /**
     * Calculates the default fade time duration.
     *
     * @return 2000 ms if 2 sec is less than 10% of the net duration of the track, else returns the 10% in ms.
     */
    @Override
    protected int defaultFadeDuration() {
        int netDuration = getDuration() - mSkippedFromStart - mSkippedFromEnd;
        return netDuration * 0.1 < 2000 ? (int) (netDuration * 0.1) : 2000;
    }


    public void setmFadeInDuration(int mFadeInDuration) {
        if (isOverlapping(mFadeInDuration,FADE_IN)) {
            this.mFadeInDuration = getMaxFadeInDuration();
        }
        this.mFadeInDuration = mFadeInDuration;
    }

    /**
     * Calculates the maximum fade out duration without overlapping with the fade in.
     *
     * @return integer representing max fade out duration in ms
     */
    private int getMaxFadeOutDuration() {
        return getDuration() - mSkippedFromEnd - mSkippedFromStart - mFadeInDuration;
    }
    /**
     * Calculates the maximum fade in duration without overlapping with the fade out.
     *
     * @return integer representing max fade in duration in ms
     */
    private int getMaxFadeInDuration() {
        return getDuration() - mSkippedFromEnd - mSkippedFromStart - mFadeOutDuration;
    }

    /**
     * Checks if the new fade duration will overlap with the other fade.
     * @param duration the new set fade duration.
     * @param key the new duration is for fade in or fade out
     * @return true if overlapping, false otherwise.
     */
    private boolean isOverlapping(int duration,int key) {
        int otherFadeDuration;
        if(key == FADE_IN){
            otherFadeDuration = mFadeOutDuration;
        }else{
            otherFadeDuration = mFadeInDuration;
        }
        return getDuration() - mSkippedFromEnd - mSkippedFromStart - otherFadeDuration - duration < 0;
    }


    public IPlayer getmPlayer() {
        return mPlayer;
    }

    @Override
    public void setMediaQueuerId(int id) {
        this.mMediaQueuerId = id;
    }

    @Override
    public void setStartTime(int startTime) {
        this.mStartTime = startTime;
    }

    @Override
    public void setSkippedFromStart(int time) {
        this.mSkippedFromStart = time;

    }

    @Override
    public void setSkippedFromEnd(int time) {
        this.mSkippedFromEnd = time;
    }

    @Override
    public int getSkippedFromStart() {
        return this.mSkippedFromStart;
    }

    @Override
    public int getSkippedFromEnd() {
        return this.mSkippedFromEnd;
    }

    public int getMediaQueuerId() {
        return this.mMediaQueuerId;
    }

    /**
     * Invoking this method will cause the current track to start playing.
     */
    private Runnable queueSong = new Runnable() {
        @Override
        public void run() {
            mPlayer.playNow(TimelineItem.this);

        }
    };

    public TimelineItem(int mResId, SubCategory mSubCategory, String mName, int mDuration, IPlayer mPlayer, int mStartTime, Context context) {
        super(mResId, mSubCategory, mName, mDuration);
        this.mPlayer = mPlayer;
        this.mContext = context;
        setStartTime(mStartTime);
        setSkippedFromEnd(0);
        setSkippedFromEnd(0);

    }

    public TimelineItem(IPlayable track, IPlayer mPlayer, int mStartTime, Context context) {
        this(track.getResId(), track.getSubCategory(), track.getName(), track.getDuration(), mPlayer, mStartTime, context);
    }

    public int getStartTime() {
        return this.mStartTime;
    }

    @Override
    public Runnable getQueuer() {
        return this.queueSong;
    }
}
