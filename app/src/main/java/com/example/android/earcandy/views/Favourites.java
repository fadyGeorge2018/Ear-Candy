package com.example.android.earcandy.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.earcandy.R;
import com.example.android.earcandy.models.Track;
import com.example.android.earcandy.presenters.TracksPresenter;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mark Mamdouh on 8/26/2017.
 */

public class Favourites extends AppCompatActivity {

    private List<Track> fav;
    private TracksPresenter tracksPresenter;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites);

        try {
            Intent i = getIntent();
            fav = (List<Track>) i.getSerializableExtra("fav");
            Collections.reverse(fav);
        }catch (Exception e){
            fav = new ArrayList<>();
        }

        seekBar = (SeekBar)findViewById(R.id.seekBar2);
        seekBar.setEnabled(false);
        tracksPresenter = new TracksPresenter(seekBar);

        ListView listView = (ListView)findViewById(R.id.fav);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(customAdapter);
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        tracksPresenter.stopTrack();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private class CustomAdapter extends BaseAdapter implements Serializable {

        @Override
        public int getCount() {
        return fav.size();
    }

        @Override
        public Object getItem(int position) {
        return null;
    }

        @Override
        public long getItemId(int position) {
        return 0;
    }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.favourite_item_view, null);

            TextView textView_name = (TextView)view.findViewById(R.id.item_txtMake);
            Button remove = (Button)view.findViewById(R.id.remove);
            final Button playPause = (Button)findViewById(R.id.play2);

            textView_name.setText(fav.get(i).getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tracksPresenter.onTrackClick(fav.get(i), getApplicationContext(), getPackageName());
                    playPause.setBackgroundResource(R.drawable.pause);
                    Toast.makeText(Favourites.this, fav.get(i).getName() + " is playing", Toast.LENGTH_LONG).show();
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tracksPresenter.stopTrack();
                    Toast.makeText(Favourites.this, fav.get(i).getName() + " Removed from favourites", Toast.LENGTH_LONG).show();
                    removeTrack(fav.get(i).getName());
                    // save fav tracks ins shared preferences
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(fav);
                    editor.putString("fav", json);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), Favourites.class);
                    intent.putExtra("fav", (Serializable) fav);
                    startActivity(intent);
                }
            });

            playPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tracksPresenter.isPlaying()){
                        playPause.setBackgroundResource(R.drawable.play);
                    }else{
                        playPause.setBackgroundResource(R.drawable.pause);
                    }
                    tracksPresenter.controlTrack();
                }
            });
            return view;
        }

        private void removeTrack(String name){
            for(int i = 0; i<fav.size(); i++){
                if(fav.get(i).getName().equals(name)){
                    fav.remove(i);
                    break;
                }
            }
        }
    }
}

