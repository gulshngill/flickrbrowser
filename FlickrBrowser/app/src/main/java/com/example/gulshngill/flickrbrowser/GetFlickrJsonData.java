package com.example.gulshngill.flickrbrowser;


/**
 * Created by gulshngill on 31/03/2016.
 */

import java.util.List;
import android.net.Uri;

public class GetFlickrJsonData extends GetRawData {

    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> photos;
    private Uri destinationUri;


    public GetFlickrJsonData(String searchCriteria, boolean matchall) {
        super(null);
        createAndUpdateUri(searchCriteria, matchall);
    }

    private boolean createAndUpdateUri(String searchCriteria, boolean matchall) {
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

}