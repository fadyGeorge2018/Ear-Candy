package com.example.android.earcandy.views;

import android.content.Intent;
import android.os.Bundle;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mark Mamdouh on 8/29/2017.
 */

public class Recent extends AppCompatActivity {

    private List<Track> recent;
    private TracksPresenter tracksPresenter;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent);

        try {
            Intent i = getIntent();
            recent = (List<Track>) i.getSerializableExtra("recent");
            Collections.reverse(recent);
        }catch (Exception e){
            recent = new ArrayList<>();
        }

        seekBar = (SeekBar)findViewById(R.id.seekBar3);
        seekBar.setEnabled(false);
        tracksPresenter = new TracksPresenter(seekBar);

        ListView listView = (ListView)findViewById(R.id.recent);
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
            if(!recent.isEmpty()){
                return recent.size();
            }
        return 0;
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

            view = getLayoutInflater().inflate(R.layout.recent_item_view, null);

            TextView textView_name = (TextView)view.findViewById(R.id.item_txtMake);
            final Button playPause = (Button)findViewById(R.id.play3);

            textView_name.setText(recent.get(i).getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tracksPresenter.onTrackClick(recent.get(i), getApplicationContext(), getPackageName());
                    playPause.setBackgroundResource(R.drawable.pause);
                    Toast.makeText(Recent.this, recent.get(i).getName() + " is playing", Toast.LENGTH_LONG).show();
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
    }
}

