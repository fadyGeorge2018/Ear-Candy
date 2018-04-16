package com.example.android.earcandy.models.timelineHandler;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.example.android.earcandy.FloatNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mario on 8/1/2017.
 */

class MediaPlayerQueuer {
    private static final int PREPARE_KEY = 5;
    private ArrayList<MediaPlayer> mMediaPlayerList;
    private final int FADE_IN = 1;
    private final int FADE_OUT = -1;
    private final int PAUSE_METHOD_KEY = 0;
    private final int RELEASE_METHOD_KEY = 1;
    private final int RESET_METHOD_KEY = 2;
    private final int STOP_METHOD_KEY = 3;
    private final int ON_COMPLETION_LISTENER_KEY = 4;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.release();
        }
    };
    private float masterVolume;


    public MediaPlayerQueuer() {
        mMediaPlayerList = new ArrayList<>();
        masterVolume = 1f;
    }

    /**
     * Creates a mediaplayer object for the track and stores it.
     *
     * @param context the acitivity context
     * @param resId   the track id in resources folder.
     * @return the track id in the media queuer class.
     */
    public int load(Context context, int resId) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        mMediaPlayerList.add(mp);
        return mMediaPlayerList.size() - 1;
    }

    public void prepare() {
        doForAllMediaPlayers(PREPARE_KEY);
    }

    public int load(Context context, Uri uri) {
        MediaPlayer mp = MediaPlayer.create(context, uri);
        mMediaPlayerList.add(mp);
        return mMediaPlayerList.size() - 1;
    }

    public int load(Context context, String url) {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayerList.add(mp);
        return mMediaPlayerList.size() - 1;
    }

    public void play(final IScheduledPlayable track) {
        final MediaPlayer mediaPlayer = mMediaPlayerList.get(track.getMediaQueuerId());

        float volume = track.getVolume();
        float finalVolume = volume * masterVolume;

        mediaPlayer.setVolume(finalVolume, finalVolume);

        mediaPlayer.seekTo(track.getSkippedFromStart());

        if (track.getSkippedFromEnd() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.pause();
                }
            }, mediaPlayer.getDuration() - track.getSkippedFromEnd() - track.getSkippedFromStart());
        }

        if (track.isFadeIn()) {
            fade(mediaPlayer, track, FADE_IN);
        } else {
            mediaPlayer.start();
        }

        if (track.isFadeOut()) {
            fade(mediaPlayer, track, FADE_OUT, fadeOutDelay(track));
        }

    }

    /**
     * Calculates the Fade out delay for the timer.
     *
     * @param track track to fade out.
     * @return delay in ms.
     */
    private int fadeOutDelay(IScheduledPlayable track) {
        return track.getDuration() - track.getSkippedFromStart() - track.getSkippedFromEnd() - track.getmFadeOutDuration();
    }

    private void fade(final MediaPlayer mediaPlayer, final IScheduledPlayable track, final int fadeKey) {
        fade(mediaPlayer, track, fadeKey, 0);
    }

    /**
     * Creates a Fade effect for the given track.
     * The fade in or the fade out is detected with the key.
     * It creates a timer which starts the mediaPlayer with an initial volume and get called periodically
     * until it reaches the target volume.
     * Each call for the timerTask increments or decrements the volume level of the track depending on the key.
     *
     * @param mediaPlayer mediaPlayer which contains the track
     * @param track       the track to fade.
     * @param fadeKey     the fade key. 1 if fade in, -1 if fade out
     * @param delay       the delay time in ms to invoke the timer task.
     */
    private void fade(final MediaPlayer mediaPlayer, final IScheduledPlayable track, final int fadeKey, final int delay) {
        final Timer timer = new Timer(true);

        final float maxVolume = track.getVolume() * masterVolume;
        final FloatNumber currentVolume = new FloatNumber((maxVolume + fadeKey * 0.01f) % maxVolume); // 0.01 in the fade in state, maxVolume-0.01 in the fadeOut state
        final float targetVolume = fadeKey == FADE_IN ? maxVolume : 0f; // maxVolume if fade in, 0 if fade out

        TimerTask fadeTask = new TimerTask() {
            @Override
            public void run() {
                if (currentVolume.compareTo(targetVolume) * fadeKey > 0) {
                    //the current volume is larger than target in the fade in case.
                    //the current volume is smaller than target in the fade out case.
                    mediaPlayer.setVolume(targetVolume, targetVolume);
                    timer.cancel();
                    timer.purge();
                    return;
                }

                mediaPlayer.setVolume(currentVolume.getFloat(), currentVolume.getFloat());

                if (!mediaPlayer.isPlaying()) { //first call in the fade in case
                    mediaPlayer.start();
                }

                currentVolume.setFloat(updateVolume(fadeKey, currentVolume.getFloat()));
            }
        };

        int period = (int) (track.getmFadeInDuration() / (maxVolume * 100f));
        timer.schedule(fadeTask, delay, period);

    }

    private float updateVolume(int fadeKey, float volume) {
        return volume + 0.01f * fadeKey;
    }


    public void seek(int soundPoolId, int skippingTime) {
        mMediaPlayerList.get(soundPoolId).seekTo(skippingTime);
    }

    public void pause() {
        doForAllMediaPlayers(PAUSE_METHOD_KEY);
    }


    public void pause(int trackId) {
        mMediaPlayerList.get(trackId).pause();
    }

    public void release() {
        doForAllMediaPlayers(RELEASE_METHOD_KEY);
    }

    public void release(int trackId) {
        mMediaPlayerList.get(trackId).release();
        //reset,stop,onCompletionListener
    }

    public void reset() {
        doForAllMediaPlayers(RESET_METHOD_KEY);
    }

    public void reset(int trackId) {
        mMediaPlayerList.get(trackId).reset();
    }

    /**
     * Sets all the tracks to a specific volume, overriding any previous volume settings.
     *
     * @param volume
     */
    public void changeMasterVolume(float volume) {
        for (MediaPlayer mediaPlayer : mMediaPlayerList) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    public void changeTrackVolume(int trackId, float volume) {
        mMediaPlayerList.get(trackId).setVolume(volume, volume);
    }


    public void doForAllMediaPlayers(int methodKey) {
        for (MediaPlayer mediaPlayer : mMediaPlayerList) {
            switch (methodKey) {
                case PAUSE_METHOD_KEY:
                    mediaPlayer.pause();
                    break;
                case RELEASE_METHOD_KEY:
                    mediaPlayer.release();
                    break;
                case RESET_METHOD_KEY:
                    mediaPlayer.reset();
                    break;
                case STOP_METHOD_KEY:
                    mediaPlayer.stop();
                    break;
                case ON_COMPLETION_LISTENER_KEY:
                    mediaPlayer.setOnCompletionListener(this.onCompletionListener);
                    break;
                case PREPARE_KEY:
                    mediaPlayer.prepareAsync();
                    break;

            }
        }
    }

    public void cutFromEnd(IScheduledPlayable track, int time) {
        track.setSkippedFromEnd(time);
    }

    public void cutFromStart(IScheduledPlayable track, int time) {
        track.setSkippedFromStart(time);
    }

    public void setSartPosition(IScheduledPlayable track, int startTime) {
        track.setStartTime(startTime);
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
    }

//    @Deprecated
//    public void play(int id, int volume) {
//        play(id, volume, 0, 0);
//    }
//
//    @Deprecated
//    public void play(int id, int volume, int skipFromStart, int skipFromEnd) {
//        final MediaPlayer mediaPlayer = mMediaPlayerList.get(id);
//        float finalVolume = volume * masterVolume;
//        mediaPlayer.setVolume(finalVolume, finalVolume);
//        mediaPlayer.seekTo(skipFromStart);
//        if (skipFromEnd > 0) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mediaPlayer.pause();
//                }
//            }, mediaPlayer.getDuration() - skipFromEnd - skipFromStart);
//        }
//
//        mediaPlayer.start();
//    }
//
//    private void fadeOut(final MediaPlayer mediaPlayer, final IScheduledPlayable track) {
//        final Handler fadeHandler = new Handler();
//        final float finalVolume = track.getVolume() * masterVolume;
//        final FloatNumber currentVolume = new FloatNumber(finalVolume);
//        fadeHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (currentVolume.isSmallerThan(0f)) {
//                    mediaPlayer.setVolume(0f, 0f);
//                    return;
//                } else {
//                    mediaPlayer.setVolume(currentVolume.getFloat(), currentVolume.getFloat());
//                    currentVolume.setFloat(currentVolume.getFloat() * 0.95f);
//                    fadeHandler.postDelayed(this, 100);
//
//                }
//            }
//        }, 0);
//    }
//
//    private void fadeIn(final MediaPlayer mediaPlayer, final IScheduledPlayable track) {
//        final Handler fadeHandler = new Handler();
//        final FloatNumber currentVolume = new FloatNumber(0.01f);
//        final float finalVolume = track.getVolume() * masterVolume;
//        fadeHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (currentVolume.isLargerThan(finalVolume)) {
//                    mediaPlayer.setVolume(track.getVolume(), track.getVolume());
//                    return;
//                } else {
//                    mediaPlayer.setVolume(currentVolume.getFloat(), currentVolume.getFloat());
//                    if(currentVolume.isEqualsTo(0.01f)){
//                        mediaPlayer.start();
//                    }
//                    currentVolume.setFloat(currentVolume.getFloat() * 1.1f);
//                    fadeHandler.postDelayed(this, 100);
//
//                }
//            }
//        }, 0);
//    }
}
