package com.example.gulshngill.flickrbrowser;


/**
 * Created by gulshngill on 31/03/2016.
 */

import java.util.ArrayList;
import java.util.List;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetFlickrJsonData extends GetRawData {

    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> photos; //creates a list of photos
    private Uri destinationUri;


    public GetFlickrJsonData(String searchCriteria, boolean matchall) {
        super(null);
        createAndUpdateUri(searchCriteria, matchall);
        photos = new ArrayList<Photo>();
    }

    public boolean createAndUpdateUri(String searchCriteria, boolean matchall) { //the json url
        //constants
        final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAGMODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String NO_JSON_CALLBACK_PARAM = "nojsoncallback";

        destinationUri = Uri.parse(FLICKR_API_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM, searchCriteria)
                .appendQueryParameter(TAGMODE_PARAM, matchall ? "ALL" : "ANY")
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM, "1")
                .build();

        return destinationUri != null;
    }

    public void execute() {
        super.setRawUrl(destinationUri.toString()); //set url
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built URI = " + destinationUri.toString());
        downloadJsonData.execute(destinationUri.toString()); //run background task
    }

    //executed last; properties of object is stored in photo object
    public void processResult() { //parse the json received
        if(getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error downloading raw file");
            return;
        }
        //objects within json array
        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTO_URL = "m";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";
        final String FLICKR_LINK = "link";
        final String FLICKR_TAGS = "tags";

        try {
            JSONObject jsonData = new JSONObject(getRawData());
            JSONArray itemsArray = jsonData.getJSONArray(FLICKR_ITEMS); //contains list of all photos

            for(int i=0; i<itemsArray.length(); i++) {
                JSONObject jsonPhoto = itemsArray.getJSONObject(i);

                //get value from the following fields
                String title = jsonPhoto.getString(FLICKR_TITLE);
                String author = jsonPhoto.getString(FLICKR_AUTHOR);
                String authorId = jsonPhoto.getString(FLICKR_AUTHOR_ID);
                String link = jsonPhoto.getString(FLICKR_LINK);
                String tags = jsonPhoto.getString(FLICKR_TAGS);

                JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                String photoUrl = jsonMedia.getString(FLICKR_PHOTO_URL);

                //store data in photo object
                Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);

                this.photos.add(photoObject); //add to array list

            }

            for(Photo singlePhoto: photos) {
                Log.v(LOG_TAG, singlePhoto.toString());
            }

        } catch(JSONException jsone) {
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
        }
    }
    public class DownloadJsonData extends DownloadRawData {
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }


    }

}
