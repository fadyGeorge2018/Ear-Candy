package com.example.android.earcandy.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mario on 8/1/2017.
 */

public class SubCategory implements Serializable{
    private String name;
    private ArrayList<Track> tracks;
    private boolean isClickedFlag;

    public SubCategory(){
        setClicked(false);
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isClicked() {
        return isClickedFlag;
    }

    public void setClicked(boolean isClickedFlag) {
        this.isClickedFlag = isClickedFlag;
    }
}
