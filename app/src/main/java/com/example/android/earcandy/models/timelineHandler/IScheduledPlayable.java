package com.example.android.earcandy.models.timelineHandler;

import com.example.android.earcandy.models.IPlayable;

/**
 * Created by Mario on 8/1/2017.
 */

public interface IScheduledPlayable extends IPlayable {
    int getStartTime(); //in milliseconds

    Runnable getQueuer();

    int getMediaQueuerId();

    void setMediaQueuerId(int id);

    void setStartTime(int startTime);

    void setSkippedFromStart(int time);

    void setSkippedFromEnd(int time);

    int getSkippedFromStart();

    int getSkippedFromEnd();

    void setmFadeInDuration(int mFadeInDuration);

    void setmFadeOutDuration(int mFadeOutDuration);

    int getmFadeInDuration();

    int getmFadeOutDuration();
}
