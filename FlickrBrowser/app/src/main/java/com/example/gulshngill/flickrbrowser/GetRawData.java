package com.example.gulshngill.flickrbrowser;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

/**
 * Created by gulshngill on 15/02/2016.
 */
enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED,FAILED_OR_EMPTY, OK }


public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName(); //get name of class
    private String mRawUrl; //url of api
    private String mData; //downloaded data from url
    private DownloadStatus mDownloadStatus; //displays download status from enum

    public GetRawData(String mRawUrl) { //constructor; if it receives a String as an argument
        this.mRawUrl = mRawUrl; //store argument into mRawUrl variable
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset() {  //called after action is completed
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawUrl = null;
        this.mData = null;
    }

    public void setmRawUrl(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public void execute() {
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawUrl);
    }

    public class DownloadRawData extends AsyncTask<String, Void, String> { //async class must be subclassed

        protected void onPostExecute(String webData) { //update download data
            mData = webData; //save data
            Log.v(LOG_TAG, "Returned Data" + mData);

            if(mData == null) {
                if(mRawUrl == null) {
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                mDownloadStatus = DownloadStatus.OK; //Success
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null; //Reads text from a character-input stream

            if(params == null)
                return null;

            try {
                URL url = new URL(params[0]); //convert string to url object

                urlConnection = (HttpURLConnection) url.openConnection(); //open the connection and pass it to url connection object
                urlConnection.setRequestMethod("GET"); //set http request method
                urlConnection.connect(); //connect to url

                InputStream inputStream = urlConnection.getInputStream(); //create inputstream to get incoming data from urlConnection

                if(inputStream == null) //return null if there's noting in input stream
                    return null;

                StringBuffer buffer = new StringBuffer(); //like a String, but can be modified

                reader = new BufferedReader(new InputStreamReader(inputStream)); //reader(bufferedReader), reads from the inputstream

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }
    }

}
