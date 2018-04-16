package com.example.android.earcandy.models.timelineHandler;

import android.content.Context;
import android.os.Handler;

import com.example.android.earcandy.models.IPlayable;

import java.util.ArrayList;

/**
 * Created by Mario on 8/1/2017.
 */

public class TimelineMixer implements IPlayer,ITimeLineHandler {
    private final int STANDARD_PRIORITY = 1;
    private Handler mHandler;
    private ArrayList<IScheduledPlayable> mPlayableItems;
    private Context mContext;
    private MediaPlayerQueuer mMediaQueuer;

    public TimelineMixer(Context context) {
        mMediaQueuer = new MediaPlayerQueuer();
        mPlayableItems = new ArrayList<>();
        mContext = context;
        mHandler = new Handler();
    }


    /**
     * This method controls the master volume of the timeline.
     * All the tracks volumes are changed proportionally.
     * @param volume the percentage of the master control sound.
     */
    public void setMasterVolume(float volume) {
        mMediaQueuer.setMasterVolume(volume);
    }

    /**
     * Adds to the timline a track, at a specific position.
     * @param track an object implementing the IPlayable interface.
     * @param startTime start position at the timeline
     * @return a ISchedueledPlayable object as stored in the timeline mixer.
     */
    public IScheduledPlayable addToTimeLine(IPlayable track, int startTime) {
        IScheduledPlayable timelineItem = new TimelineItem(track, this, startTime, mContext);
        mPlayableItems.add(timelineItem);

        int id = mMediaQueuer.load(mContext, track.getResId());
        timelineItem.setMediaQueuerId(id);
        return timelineItem;
    }

    /**
     * Start playing the tracks at the timeline from a specific second.
     * @param startTime the starting second to play from.
     */
    public void playAt(int startTime) {
        for (IScheduledPlayable track : mPlayableItems) {
            if (isAfter(startTime, track.getStartTime())) {
                mHandler.postDelayed(track.getQueuer(), timeInQueue(track.getStartTime(), startTime));

            } else if (isBefore(startTime, track.getStartTime() + track.getDuration())) {
                mMediaQueuer.seek(track.getMediaQueuerId(), skippingTime(track.getStartTime(), startTime));
                mHandler.postDelayed(track.getQueuer(), 0);
            }
        }
    }

    /**
     * Calculates the number of ms before the starting position of the timeline, which will be skipped.
     * @param trackStartTime the track start position on the timeline.
     * @param timelineStartTime the position from which the timeline is going to start playing.
     * @return int number of ms skipped from the track
     */
    private int skippingTime(int trackStartTime, int timelineStartTime) {
        return timelineStartTime - trackStartTime;
    }

    /**
     * Checks if the timeline start playing before the end of the track.
     * @param startTime timeline starting position.
     * @param trackEndTime the end position of the track
     * @return true if the timeline starts before the track ends,false otherwise
     */
    private boolean isBefore(int startTime, int trackEndTime) {
        return startTime < trackEndTime;
    }

    /**
     * Time for the track to wait before it gets played from the starting play time of the timeline.
     * @param trackStartTime the starting position of the track.
     * @param timelineStartTime the starting play time of the timeline.
     * @return the duration of the waiting
     */
    private int timeInQueue(int trackStartTime, int timelineStartTime) {
        return trackStartTime - timelineStartTime;
    }

    /**
     * Checks if the start of the track is after the timeline start time.
     * @param timelineStartTime timeline starting time.
     * @param trackStartTime the track starting time.
     * @return true if the track starting time is after the timeline starting time, false otherwise.
     */

    private boolean isAfter(int timelineStartTime, int trackStartTime) {
        return trackStartTime >= timelineStartTime;
    }

    /**
     * Call back method to start playing the track.
     * It delegates the operation of playing the track to the media Queuer.
     * @param track IScheduledPlayable object represents the track to play.
     */
    @Override
    public void playNow(IScheduledPlayable track) {

        //mMediaQueuer.play(track.getMediaQueuerId(), track.getVolume(), track.getSkippedFromStart(), track.getSkippedFromEnd());
        mMediaQueuer.play(track);
    }



    @Override
    public void setStartPosition(IScheduledPlayable track, int startTime) {
        mMediaQueuer.setSartPosition(track,startTime);
    }

    @Override
    public void remove(IScheduledPlayable track) {
        mPlayableItems.remove(track);

    }

    @Override
    public void setTrackVolume(IScheduledPlayable track, float volume) {
        mMediaQueuer.changeTrackVolume(track.getMediaQueuerId(),volume);
    }


    @Override
    public void setAllVolumes(float volume) {
        mMediaQueuer.changeMasterVolume(volume);
    }

    @Override
    public void cutFromStart(IScheduledPlayable track, int time) {
        track.setSkippedFromStart(time);
    }

    @Override
    public void cutFromEnd(IScheduledPlayable track, int time) {
        track.setSkippedFromEnd(time);
    }
}
