package com.example.gulshngill.flickrbrowser;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PhotoRecyclerViewAdapter photoRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private TextView hintText;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String PHOTO_TRANSFER = "PHOTO_TRANSFER";
    private final String FLICKR_QUERY = "FLICKR_QUERY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hintText = (TextView) findViewById(R.id.hintText);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        photoRecyclerViewAdapter = new PhotoRecyclerViewAdapter(new ArrayList<Photo>(), MainActivity.this);
        recyclerView.setAdapter(photoRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListenter(this, recyclerView, new RecyclerItemClickListenter.OnItemCickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //create new intent from mainactivity to viewphotodetailsactivity
                Intent intent = new Intent(MainActivity.this, ViewPhotoDetailsActivity.class);
                intent.putExtra(PHOTO_TRANSFER, photoRecyclerViewAdapter.getPhoto(position));
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long Tap", Toast.LENGTH_SHORT).show();
            }
        }));
        //ProcessPhoto processPhoto = new ProcessPhoto("starcraft");
        //processPhoto.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //searchmanager
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //text listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String formattedQuery = query.replaceAll("\\s+",",");
                Log.v(LOG_TAG, formattedQuery);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPref.edit().putString(FLICKR_QUERY, formattedQuery).commit();

                hintText.setVisibility(View.GONE);
                ProcessPhoto processPhoto = new ProcessPhoto(formattedQuery);
                processPhoto.execute();

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        }
        );

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(photoRecyclerViewAdapter !=  null) {
            String query = getSavedPreferenceData(FLICKR_QUERY);

            if(query.length() > 0) {
                hintText.setVisibility(View.GONE);
                ProcessPhoto processPhoto = new ProcessPhoto(query);
                processPhoto.execute();
            }
        }


    }

    //get last searched string preference manager
    private String getSavedPreferenceData(String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPref.getString(key, "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //runs getJsonData binds photolist to viewadapter and sets chosen recyclerView's adapter to that
    public class ProcessPhoto extends GetJsonData {

        public ProcessPhoto(String searchCriteria) {
            super(searchCriteria);
        }

        public void execute() {
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                photoRecyclerViewAdapter.loadNewData(getPhotoList());

            }
        }
    }
}
