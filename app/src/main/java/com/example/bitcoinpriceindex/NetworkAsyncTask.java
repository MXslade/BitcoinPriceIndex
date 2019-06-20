package com.example.bitcoinpriceindex;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkAsyncTask extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... url) {
        try {
            JSONObject json = new JSONObject(IOUtils.toString(new URL(url[0]), Charset.forName("UTF-8")));
            return json;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(JSONObject aVoid) {
        super.onPostExecute(aVoid);

    }
}
