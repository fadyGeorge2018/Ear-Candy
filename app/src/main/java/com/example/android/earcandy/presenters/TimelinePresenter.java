package com.example.android.earcandy.presenters;

import android.view.View;
import android.widget.AdapterView;

import com.example.android.earcandy.models.timelineHandler.ITimeLineHandler;
import com.example.android.earcandy.views.MainView;

/**
 * Created by Mario on 8/13/2017.
 */

public class TimelinePresenter implements ITimelinePresenter {
    private MainView view;
    private ITimeLineHandler mixer;

    public TimelinePresenter(MainView view) {
        this.view = view;
    }


    @Override
    public void timelineItemClicked(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
