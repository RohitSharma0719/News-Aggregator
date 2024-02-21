package com.RohitSharma.myapplication;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class newsDataLoader implements Runnable {
    private static final String TAG = "CountryLoaderRunnable";
    private final MainActivity mainActivity;
    private  String apiKey="4ec5a0ab9967479fa7a9409374b8a9ea";
    private static String URL = "https://newsapi.org/v2/sources?apiKey=";
    //private static String DATA_URL=URL+"4ec5a0ab9967479fa7a9409374b8a9ea";
    private static String DATA_URL=URL+"4fd79640632949dea94f3378d8637bdb\n";
    private static RequestQueue queue;
    private News news;
    newsDataLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        queue = Volley.newRequestQueue(mainActivity);
        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();

        String urlToUse = buildURL.build().toString();
        StringBuilder sb = new StringBuilder();
        Response.Listener<JSONObject> listener =
                response -> handleResults(response.toString());
        Response.ErrorListener error =
                error1 -> {

                };

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers;
                    }
                };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


    }

    public void handleResults(String s) {

        if (s.equals("null")) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(mainActivity::downloadFailed);
            return;
        }

        final ArrayList<News> NewsList1 = parseJSON(s);
        if (NewsList1 == null) {
            mainActivity.runOnUiThread(mainActivity::downloadFailed);
            return;
        }
        mainActivity.runOnUiThread(() -> mainActivity.updateData(NewsList1));

    }
    private ArrayList<News> parseJSON(String s) {
        ArrayList<News> NewsList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            if(jObjMain.has("sources")){
            JSONArray jsonSources=jObjMain.getJSONArray("sources");
            for(int i=0;i<jsonSources.length();i++)
            {
                NewsList.add(new News(jsonSources.getJSONObject(i).getString("category"),jsonSources.getJSONObject(i).getString("id"),jsonSources.getJSONObject(i).getString("name"),"","","","","",""));
            }
//            Collections.sort(NewsList);
            return NewsList;}

        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
