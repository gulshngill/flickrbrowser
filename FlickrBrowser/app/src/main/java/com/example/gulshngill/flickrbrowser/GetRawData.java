package com.example.gulshngill.flickrbrowser;

import android.annotation.TargetApi;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;

import android.os.Build;
import android.renderscript.ScriptGroup;
import android.util.Log;

/**
 * Created by gulshngill on 15/02/2016.
 */
enum DownloadStatus{OK, NOT_INITIALISED, IDLE, PROCESSING, FAILED_OR_EMPTY}

public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String rawUrl; //url of api
    private String rawData; //downloaded data
    private DownloadStatus downloadStatus; //displays download status

    public GetRawData(String rawUrl) { //constructor; receives a url-string as an argument
        this.rawUrl = rawUrl; //store argument into mRawUrl variable
        this.downloadStatus = DownloadStatus.IDLE;
    }

    //set rawUrl
    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    //get rawData
    public String getRawData() {
        return rawData;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public void execute() {
        this.downloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(rawUrl);
    }

    public void reset() {  //called after action is completed
        this.downloadStatus = DownloadStatus.IDLE;
        this.rawUrl = null;
        this.rawData = null;
    }




    public class DownloadRawData extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String webData) {
            rawData = webData;
            Log.v(LOG_TAG, "Returned Data" + rawData);

            if(rawData == null) {
                if (rawUrl == null) {
                    downloadStatus = DownloadStatus.NOT_INITIALISED;
                } else {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            }
            else {
                downloadStatus = DownloadStatus.OK; //Success
            }
        }

        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null; //send and receive data over the web
            BufferedReader reader = null; //reads content of inputstream

            if(params == null)
                return null;

            try {
                URL url = new URL(params[0]); //convert url-string to url object

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect(); //connect to url

                if(urlConnection.getInputStream() == null)
                    return null;

                StringBuffer buffer = new StringBuffer(); //stores string from bufferedreader

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));



                String line; //stores the line from the BufferedReader
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                return buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if(urlConnection != null) { //disconnect urlconnection
                    urlConnection.disconnect();
                }
                if(reader != null) { //close bufferedreader
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
