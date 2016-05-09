package com.example.gulshngill.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gulshngill on 07/05/2016.
 */
public class GetJsonData extends GetRawData {
    private final String LOG_TAG = GetJsonData.class.getSimpleName();
    private Uri destinationUri;
    List<Photo> photoList = new ArrayList<>();


    public GetJsonData(String searchCriteria) {
        super(null);
        createAndUpdateUri(searchCriteria);
        Log.v(LOG_TAG, "URI: " + destinationUri.toString());

    }

    public void createAndUpdateUri(String searchCriteria) {
        final String BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS = "tags";
        final String FORMAT = "format";
        final String NOJSONCALLBACK = "nojsoncallback";
        final String TAGMODE = "tagmode";

        destinationUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(TAGS, searchCriteria)
                .appendQueryParameter(FORMAT, "json")
                .appendQueryParameter(NOJSONCALLBACK, "1")
                .appendQueryParameter(TAGMODE, "ANY")
                .build();
    }

    public void execute() {
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        downloadJsonData.execute(destinationUri.toString());

    }
    //parse json
    public void processData(String rawData) {
        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_LINK = "link";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTOURL = "m";
        final String FLICKR_DESCRIPTION = "description";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";

        try {
            //convert rawData to jsonObject
            JSONObject jsonObject = new JSONObject(rawData);

            //get items array
            JSONArray jsonArray = jsonObject.getJSONArray(FLICKR_ITEMS);

            //loop through array
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonPhoto = jsonArray.getJSONObject(i);

                String title = jsonPhoto.getString(FLICKR_TITLE);
                String link = jsonPhoto.getString(FLICKR_LINK);

                JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                String photoUrl = jsonMedia.getString(FLICKR_PHOTOURL);

                String description = jsonPhoto.getString(FLICKR_DESCRIPTION);
                String author = jsonPhoto.getString(FLICKR_AUTHOR);
                String authorId = jsonPhoto.getString(FLICKR_AUTHOR_ID);

                //store info in photo object
                Photo photoItem = new Photo(title, link, photoUrl, description, author, authorId);
                Log.v(LOG_TAG, photoItem.toString());

                //add to list
                photoList.add(photoItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public class DownloadJsonData extends DownloadRawData {
        @Override
        protected String doInBackground(String... params) {
            String[] par = { destinationUri.toString()};
            return super.doInBackground(par);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            processData(s);
        }
    }
}
