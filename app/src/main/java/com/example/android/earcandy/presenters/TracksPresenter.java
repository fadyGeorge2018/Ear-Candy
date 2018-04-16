package com.example.android.earcandy.presenters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.android.earcandy.models.Category;
import com.example.android.earcandy.models.SubCategory;
import com.example.android.earcandy.models.Track;
import com.example.android.earcandy.views.MainView;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mario on 8/14/2017.
 */

public class TracksPresenter implements ITracksPresenter {
    private MainView mainView;
    private Category[] categories;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;
    private SeekBar seekBar;

    public TracksPresenter(MainView mainView, SeekBar seekBar) {
        this.mainView = mainView;
        mediaPlayer = new MediaPlayer();
        this.seekBar = seekBar;
        handler = new Handler();
    }

    public TracksPresenter(SeekBar seekBar) {
        mediaPlayer = new MediaPlayer();
        this.seekBar = seekBar;
        handler = new Handler();
    }

    public void initiate() {
        Gson jsonHandler = new Gson();
        InputStreamReader reader = getJsonStreamReader();
        categories = jsonHandler.fromJson(reader, Category[].class);
        mainView.showTracks(categories);
    }

    public Category[] getCategories(){
        return categories;
    }

    @Override
    public void imagerowFirstTouched(View v, final Category[] categories, int i) {
        if (!categories[i].isClicked()) {
            mainView.showSecondScroll(v, categories, i);
        } else {
            mainView.hideSecondScroll(v, categories, i);
        }
    }

    @Override
    public void secondRowTouched(View view, SubCategory subCategory, final Category[] categories, int i, int j) {
        if (categories[i].getSubCategories().get(j).isClicked()) {
            mainView.hideThirdScroll(view, categories, i, j);
        } else {
            mainView.showThirdScroll(view, subCategory.getTracks(), categories, i, j);
        }
    }

    @Override
    public void onTrackClick(Track track, Context context, String pac) {
//        if (!track.getUri().equals("")) {
//            playLocalTrack(track.getUri(), context);
//        } else if (!track.getUrl().equals("")) {
//            DownloadTrack downloadTask = new DownloadTrack();
//            downloadTask.setPlayer(this);
//            downloadTask.execute(track);
//            Uri uri = Uri.parse(track.getUrl());
//            playOnlineTrack(uri);
//        }
        playLocalTrack(context, pac, track.getName());
    }

    @Override
    public void controlTrack(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }

    @Override
    public void stopTrack(){
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private void playLocalTrack(Context context, String pac, String name) {
        mediaPlayer.stop();
        mediaPlayer.reset();
        String uri = "android.resource://" + pac + "/raw/" + name;
        try { mediaPlayer.setDataSource(context ,Uri.parse(uri)); } catch (Exception e) {}
        try { mediaPlayer.prepare(); } catch (Exception e) {}

        mediaPlayer.start();
        // Initialize the seek bar
        initializeSeekBar();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b){
                    mediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void initializeSeekBar(){
        seekBar.setEnabled(true);
        seekBar.setMax(mediaPlayer.getDuration()/1000);

        runnable = new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000; // In milliseconds
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(runnable,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }


    private void playOnlineTrack(Uri uri) {
        final MediaPlayer mp = MediaPlayer.create(mainView.getContext(), uri);
        try {
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.release();
                }
            });

            mp.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStreamReader getJsonStreamReader() {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(mainView.getContext().getAssets().open("tracks.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private class DownloadTrack extends AsyncTask<Track, Void, String> {
        TracksPresenter player;
        @Override
        protected String doInBackground(Track... tracks) {
            String url = tracks[0].getUrl();
            HttpURLConnection urlConnection = null;
            String trackUri = "";
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL urlObject = new URL(url);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int contentLength = urlConnection.getContentLength();

                DataInputStream stream = new DataInputStream(urlObject.openStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();

                trackUri = Environment.getExternalStorageDirectory() + tracks[0].getName();
                File trackFile = new File(trackUri);


                DataOutputStream fos = new DataOutputStream(new FileOutputStream(trackFile));
                fos.write(buffer);
                fos.flush();
                fos.close();


            } catch (MalformedURLException e) {}
            catch (IOException ee) {}
            return trackUri;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("") ){
                player.playOnlineTrack(Uri.parse(s));
                Toast.makeText(mainView.getContext(),"Playing Track",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(mainView.getContext(),"Unable to Play Track",Toast.LENGTH_SHORT).show();
            }
        }

        public void setPlayer(TracksPresenter player) {
            this.player = player;
        }
    }
}
