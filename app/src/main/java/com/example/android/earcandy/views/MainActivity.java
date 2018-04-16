package com.example.android.earcandy.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.earcandy.R;
import com.example.android.earcandy.models.ReaderAndWriter;
import com.example.android.earcandy.models.timelineHandler.Utils;
import com.example.android.earcandy.presenters.ITimelinePresenter;
import com.example.android.earcandy.presenters.ITracksPresenter;
import com.example.android.earcandy.presenters.TimelinePresenter;
import com.example.android.earcandy.presenters.TracksPresenter;
import com.example.android.earcandy.models.Category;
import com.example.android.earcandy.models.Track;
import com.example.android.earcandy.models.timelineHandler.IScheduledPlayable;
import com.example.android.earcandy.models.SubCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView, SearchView.OnQueryTextListener {


//    private ListView listView;
//    private ITimelinePresenter timelinePresenter;
    private ITracksPresenter tracksPresenter;
    private RecyclerView horizontal_recycler_view;
    private HorizontalAdapter horizontalAdapter;
    private Category[] categories;
    private List<Track> recent = new ArrayList<>();
    private List<Track> fav = new ArrayList<>();
    private ReaderAndWriter readerAndWriter = new ReaderAndWriter();
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load recent tracks from shared preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("recent", null);
        Type type = new TypeToken<List<Track>>() {}.getType();
        if(gson.fromJson(json, type) != null){
            recent = gson.fromJson(json, type);
        }

        //load fav tracks from shared preferences
        SharedPreferences sharedPrefs2 = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson2 = new Gson();
        String json2 = sharedPrefs2.getString("fav", null);
        Type type2 = new TypeToken<List<Track>>() {}.getType();
        if(gson2.fromJson(json2, type2) != null){
            fav = gson2.fromJson(json2, type2);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String path = Environment.getExternalStorageDirectory().toString();
        String dirPath = path + "/Ear Candy/";
        File dirFile = new File(dirPath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }

        dirPath = path + "/Ear Candy/Data/";
        dirFile = new File(dirPath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }

//        timelinePresenter = new TimelinePresenter(this);
//        listView = (ListView) findViewById(R.id.timeline_list);
//        ArrayList<IScheduledPlayable> list = new ArrayList<>();
//        listView.setAdapter(new TimelineAdapter(this, R.layout.timeline_layer, list));
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                timelinePresenter.timelineItemClicked(adapterView, view, i, l);
//            }
//        });

        seekBar = (SeekBar)findViewById(R.id.seekBar1);
        seekBar.setEnabled(false);
        this.tracksPresenter = new TracksPresenter(this, seekBar);
        tracksPresenter.initiate();
        categories = tracksPresenter.getCategories();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        // exit app
        tracksPresenter.stopTrack();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourites) {
            tracksPresenter.stopTrack();
            Intent intent = new Intent(getApplicationContext(), Favourites.class);
            intent.putExtra("fav", (Serializable) fav);
            startActivity(intent);
        } else if (id == R.id.nav_load) {

        } else if (id == R.id.nav_my_tracks) {

        } else if (id == R.id.nav_recent) {
            tracksPresenter.stopTrack();
            Intent intent = new Intent(getApplicationContext(), Recent.class);
            intent.putExtra("recent", (Serializable) recent);
            startActivity(intent);
        } else if (id == R.id.nav_theme) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            // set title
            alertDialogBuilder.setTitle("Choose Your Theme");
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Default(Blue)", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Utils.changeToTheme(MainActivity.this, Utils.THEME_DEFAULT);
                        }
                    })
                    .setNegativeButton("Red", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Utils.changeToTheme(MainActivity.this, Utils.THEME_RED);
                        }
                    })
                    .setNeutralButton("Yellow", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Utils.changeToTheme(MainActivity.this, Utils.THEME_YELLOW);
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, Pop.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showTracks(final Category[] categories) {
        LinearLayout mLinearListView = (LinearLayout) findViewById(R.id.linear_listview);
        if(mLinearListView.getChildCount() > 0) {
            mLinearListView.removeAllViews();
        }
        for (int i = 0; i < categories.length; i++) {
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mLinearView = inflater.inflate(R.layout.row_first, null);
            final TextView mProductName = (TextView) mLinearView.findViewById(R.id.textViewName);
            final ImageView mImageArrowFirst = (ImageView)mLinearView.findViewById(R.id.imageFirstArrow);
            final LinearLayout mLinearScrollSecond = (LinearLayout)mLinearView.findViewById(R.id.linear_scroll_second);
            RelativeLayout mLinearFirst = (RelativeLayout)mLinearView.findViewById(R.id.linearFirst);
            mLinearFirst.setBackgroundColor(Color.parseColor(categories[i].getColor()));

            if(!categories[i].isClicked()){
                mLinearScrollSecond.setVisibility(View.GONE);
                mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
            }
            else{
                mLinearScrollSecond.setVisibility(View.VISIBLE);
                mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
            }

            final int catPos = i;

            mImageArrowFirst.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    tracksPresenter.imagerowFirstTouched(mLinearView, categories, catPos);
                    return false;
                }
            });

            final String name = categories[i].getName();
            mProductName.setText(name);

            for (int j = 0; j < categories[i].getSubCategories().size(); j++) {
                LayoutInflater inflater2 = null;
                inflater2 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mLinearView2 = inflater2.inflate(R.layout.row_second, null);

                TextView mSubItemName = (TextView) mLinearView2.findViewById(R.id.textViewTitle);
                final ImageView mImageArrowSecond = (ImageView)mLinearView2.findViewById(R.id.imageSecondArrow);
                final LinearLayout mLinearScrollThird = (LinearLayout)mLinearView2.findViewById(R.id.linear_scroll_third);

                if(!categories[i].getSubCategories().get(j).isClicked()){
                    mLinearScrollThird.setVisibility(View.GONE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
                }
                else{
                    mLinearScrollThird.setVisibility(View.VISIBLE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.arw_down);
                }
                final int subCatPos = j;
                mImageArrowSecond.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        tracksPresenter.secondRowTouched(mLinearView2, categories[catPos].getSubCategories().get(subCatPos), categories, catPos, subCatPos);
                        return false;
                    }
                } );
                final String catName = categories[i].getSubCategories().get(j).getName();
                mSubItemName.setText(catName);

                mLinearScrollSecond.addView(mLinearView2);
            }
            mLinearListView.addView(mLinearView);
        }
    }

    @Override
    public void hideSecondScroll(View v, final Category[] categories, int i) {
        categories[i].setClicked(false);
        ImageView mImageArrowFirst = (ImageView)v.findViewById(R.id.imageFirstArrow);
        LinearLayout mLinearScrollSecond = (LinearLayout)v.findViewById(R.id.linear_scroll_second);
        mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
        mLinearScrollSecond.setVisibility(View.GONE);
    }

    @Override
    public void showSecondScroll(View v, final Category[] categories, int i) {
        categories[i].setClicked(true);
        ImageView mImageArrowFirst = (ImageView)v.findViewById(R.id.imageFirstArrow);
        LinearLayout mLinearScrollSecond = (LinearLayout)v.findViewById(R.id.linear_scroll_second);
        mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
        mLinearScrollSecond.setVisibility(View.VISIBLE);
    }

    @Override
    public void showThirdScroll(View v, ArrayList<Track> tracks, final Category[] categories, int i, int j) {
        categories[i].getSubCategories().get(j).setClicked(true);

        ImageView imageSecondArrow = (ImageView)v.findViewById(R.id.imageSecondArrow);
        LinearLayout linearScrollThird = (LinearLayout)v.findViewById(R.id.linear_scroll_third);

        imageSecondArrow.setBackgroundResource(R.drawable.arw_down);
        linearScrollThird.setVisibility(View.VISIBLE);

        LayoutInflater inflater3 = null;
        inflater3 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mLinearView3 = inflater3.inflate(R.layout.row_third, null);

        horizontal_recycler_view = (RecyclerView) mLinearView3.findViewById(R.id.horizontal_recycler_view);

        linearScrollThird.removeAllViews();

        horizontalAdapter = new HorizontalAdapter(tracks);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        linearScrollThird.addView(mLinearView3);
        registerForContextMenu(mLinearView3);
    }

    @Override
    public void hideThirdScroll(View v, final Category[] categories, int i, int j) {
        categories[i].getSubCategories().get(j).setClicked(false);
        ImageView mImageArrowSecond = (ImageView)v.findViewById(R.id.imageSecondArrow);
        LinearLayout mLinearScrollThird = (LinearLayout)v.findViewById(R.id.linear_scroll_third);
        mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
        mLinearScrollThird.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Category> tempList = new ArrayList<>();
        ArrayList<Track> tracks;
        ArrayList<SubCategory> subCategories;
        for(int i = 0; i<categories.length; i++){
            subCategories = new ArrayList<>();
            for(int j = 0; j<categories[i].getSubCategories().size(); j++){
                tracks = new ArrayList<>();
                for(int k = 0; k<categories[i].getSubCategories().get(j).getTracks().size(); k++){
                    String name = categories[i].getSubCategories().get(j).getTracks().get(k).getName();
                    if(name.contains(newText)){
                        tracks.add(categories[i].getSubCategories().get(j).getTracks().get(k));
                    }
                }
                if(!tracks.isEmpty()){
                    SubCategory subCategory = new SubCategory();
                    subCategory.setTracks(tracks);
                    subCategory.setName(categories[i].getSubCategories().get(j).getName());
                    subCategories.add(subCategory);
                }
            }
            if(!subCategories.isEmpty()){
                Category category = new Category();
                category.setName(categories[i].getName());
                category.setColor(categories[i].getColor());
                category.setSubCategories(subCategories);
                tempList.add(category);
            }
        }
        if(!tempList.isEmpty()){
            Category[] newCategories = changeListToArray(tempList);
            showTracks(newCategories);
        }else{
            Category[] newCategories ={};
            showTracks(newCategories);
        }
        return true;
    }

    private Category[] changeListToArray(List<Category> tempList){
        Category[] newCategories = new Category[tempList.size()];
        for(int i = 0; i<tempList.size(); i++){
            newCategories[i] = tempList.get(i);
        }
        return newCategories;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Fade in");
        menu.add(0, v.getId(), 0, "Fade out");
        menu.add(0, v.getId(), 0, "Repeat");
        menu.add(0, v.getId(), 0, "Add to timeline");
        menu.add(0, v.getId(), 0, "Add to favorites");
        menu.add(0, v.getId(), 0, "Edit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        String name = readerAndWriter.readFromFile("name.bin", "/Ear Candy/Data/");

        for(int i = 0; i < categories.length; i++){
            for(int j = 0; j < categories[i].getSubCategories().size(); j++){
                for(int k = 0; k < categories[i].getSubCategories().get(j).getTracks().size(); k++){
                    String trackName = categories[i].getSubCategories().get(j).getTracks().get(k).getName();
                    if(trackName.equals(name)){
                        if(item.getTitle() == "Fade in")
                        {
                            if(categories[i].getSubCategories().get(j).getTracks().get(k).isFadeIn()){
                                categories[i].getSubCategories().get(j).getTracks().get(k).setFadeIn(false);
                                Toast.makeText(this, trackName+" will not fade in", Toast.LENGTH_LONG).show();
                            }else{
                                categories[i].getSubCategories().get(j).getTracks().get(k).setFadeIn(true);
                                Toast.makeText(this, trackName+" will fade in", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(item.getTitle() == "Fade out")
                        {
                            if(categories[i].getSubCategories().get(j).getTracks().get(k).isFadeOut()){
                                categories[i].getSubCategories().get(j).getTracks().get(k).setFadeOut(false);
                                Toast.makeText(this, trackName+" will not fade out", Toast.LENGTH_LONG).show();
                            }else{
                                categories[i].getSubCategories().get(j).getTracks().get(k).setFadeOut(true);
                                Toast.makeText(this, trackName+" will fade out", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(item.getTitle() == "Repeat")
                        {
                            if(categories[i].getSubCategories().get(j).getTracks().get(k).isRepeat()){
                                categories[i].getSubCategories().get(j).getTracks().get(k).setRepeat(false);
                                Toast.makeText(this, trackName+" will not be repeated", Toast.LENGTH_LONG).show();
                            }else{
                                categories[i].getSubCategories().get(j).getTracks().get(k).setRepeat(true);
                                Toast.makeText(this, trackName+" will be repeated", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(item.getTitle() == "Add to timeline")
                        {
                            //TODO
                        }
                        else if(item.getTitle() == "Add to favorites")
                        {
                            if(isContains(categories[i].getSubCategories().get(j).getTracks().get(k).getName())){
                                removeTrack(categories[i].getSubCategories().get(j).getTracks().get(k).getName());
                            }
                            fav.add(categories[i].getSubCategories().get(j).getTracks().get(k));

                            // save fav tracks ins shared preferences
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(fav);
                            editor.putString("fav", json);
                            editor.apply();
                        }
                        else if(item.getTitle() == "Edit")
                        {
                            //TODO
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isContains(String name){
        for(int i = 0; i<fav.size(); i++){
            if(fav.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private void removeTrack(String name){
        for(int i = 0; i<fav.size(); i++){
            if(fav.get(i).getName().equals(name)){
                fav.remove(i);
                break;
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private ArrayList<Track> horizontalList;
        private Button play;

        HorizontalAdapter(ArrayList<Track> horizontalList) {
            this.horizontalList = horizontalList;
            play = (Button)findViewById(R.id.play1);
            seekBar = (SeekBar)findViewById(R.id.seekBar1);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView txtview;
            MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imageview);
                txtview = (TextView) view.findViewById(R.id.txtview);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final ReaderAndWriter readerAndWriter = new ReaderAndWriter();
            holder.imageView.setImageResource(R.drawable.circle);
            holder.txtview.setText(horizontalList.get(position).getName());

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tracksPresenter.onTrackClick(horizontalList.get(position), getContext(), getPackageName());
                    play.setBackgroundResource(R.drawable.pause);
                    if(recent.size() > 7){
                        recent.remove(0);
                        if(isContains(horizontalList.get(position).getName())){
                            removeTrack(horizontalList.get(position).getName());
                        }
                        recent.add(recent.size(), horizontalList.get(position));
                    }else{
                        if(isContains(horizontalList.get(position).getName())){
                            removeTrack(horizontalList.get(position).getName());
                        }
                        recent.add(horizontalList.get(position));
                    }
                    // save recent tracks ins shared preferences
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(recent);
                    editor.putString("recent", json);
                    editor.apply();
                }
            });

            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String name = horizontalList.get(position).getName();
                    readerAndWriter.writeToFile(name, "name.bin", "/Ear Candy/Data/");
                    return false;
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tracksPresenter.isPlaying()){
                        play.setBackgroundResource(R.drawable.play);
                    }else{
                        play.setBackgroundResource(R.drawable.pause);
                    }
                    tracksPresenter.controlTrack();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return horizontalList.size();
        }

        private boolean isContains(String name){
            for(int i = 0; i<recent.size(); i++){
                if(recent.get(i).getName().equals(name)){
                    return true;
                }
            }
            return false;
        }

        private void removeTrack(String name){
            for(int i = 0; i<recent.size(); i++){
                if(recent.get(i).getName().equals(name)){
                    recent.remove(i);
                    break;
                }
            }
        }
    }
}