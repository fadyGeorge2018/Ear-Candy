package com.example.android.earcandy.presenters;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Mario on 8/15/2017.
 */
public interface ITimelinePresenter {
    void timelineItemClicked(AdapterView<?> adapterView, View view, int i, long l);
}
