package com.example.gulshngill.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gulshngill on 07/05/2016.
 */
public class GetRawData {
    private final String LOG_TAG = GetRawData.class.getSimpleName();
    private String rawUrl;
    private String rawData;

    public GetRawData(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    public void execute() {
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(rawUrl);
    }

    public class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuffer stringBuffer = new StringBuffer();

            try {
                //convert string to url object
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if(urlConnection.getInputStream() == null) {
                    Log.v(LOG_TAG, "No incoming data");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch(final IOException e) {
                        Log.e(LOG_TAG,"Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            rawData = s;
            //Log.v(LOG_TAG, rawData);
        }
    }
}
