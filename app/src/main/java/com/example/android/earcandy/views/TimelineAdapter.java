package com.example.android.earcandy.views;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.earcandy.R;
import com.example.android.earcandy.models.timelineHandler.IScheduledPlayable;

import java.util.List;

/**
 * Created by Mario on 8/13/2017.
 */

public class TimelineAdapter extends ArrayAdapter<IScheduledPlayable> {


    public TimelineAdapter(Context context, int resource, List<IScheduledPlayable> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timeline_layer, parent, false);
        }

        IScheduledPlayable track = getItem(position);

        final ImageView image = (ImageView) convertView.findViewById(R.id.track_image);
        //image.setMaxWidth(track.getDuration());

//        image.setOnTouchListener(new View.OnTouchListener() {
//            int _xDelta ;
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                final int X = (int) event.getRawX();
//                final int Y = (int) event.getRawY();
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN:
//                        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//                        _xDelta = X - lParams.leftMargin;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//                    case MotionEvent.ACTION_POINTER_DOWN:
//                        break;
//                    case MotionEvent.ACTION_POINTER_UP:
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//                        layoutParams.leftMargin = X - _xDelta;
//                        view.setLayoutParams(layoutParams);
//                        break;
//                }
//                view.invalidate();
//                return true;
//            }
//        });


        image.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(image);

                    image.startDrag(data, shadowBuilder, image, 0);
                    image.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });

        image.setOnDragListener(new View.OnDragListener() {
            RelativeLayout.LayoutParams layoutParams;
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);
                        v.setVisibility(View.VISIBLE);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        v.setVisibility(View.VISIBLE);

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        LinearLayout container = (LinearLayout) v;
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        return convertView;
    }
}
