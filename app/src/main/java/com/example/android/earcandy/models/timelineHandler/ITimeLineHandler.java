package com.example.android.earcandy.models.timelineHandler;

import com.example.android.earcandy.models.IPlayable;

/**
 * Created by Mario on 8/2/2017.
 */

public interface ITimeLineHandler {
    IScheduledPlayable addToTimeLine(IPlayable track, int startTime);
    void setStartPosition(IScheduledPlayable track,int startTime);
    void remove(IScheduledPlayable track);
    void setTrackVolume(IScheduledPlayable track,float volume);
    void setMasterVolume(float volume);
    void setAllVolumes(float volume);
    void cutFromStart(IScheduledPlayable track, int time);
    void cutFromEnd(IScheduledPlayable track,int time);
}
