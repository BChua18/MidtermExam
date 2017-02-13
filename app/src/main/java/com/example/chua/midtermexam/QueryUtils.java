package com.example.chua.midtermexam;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Chua on 2/13/2017.
 */

public class QueryUtils {
    public QueryUtils() {
    }

    public static ArrayList<Album> fetchAlbumData(String requestUrl, int requestCode){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        ArrayList<Album> albums = new ArrayList<>();
        try{
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(requestCode == 1) {
            albums = extractFeatureFromJson(jsonResponse);
        }
        else if(requestCode == 2)
        {
            albums = extractFeatureFromJson2(jsonResponse);
        }
        return albums;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Album> extractFeatureFromJson(String albumJson){
        if(TextUtils.isEmpty(albumJson)){
            return null;
        }

        ArrayList<Album> albums = new ArrayList<>();
        Log.d("log",albumJson);

        try{
            JSONObject baseJsonResponse = new JSONObject(albumJson);
            JSONObject albumArray = baseJsonResponse.getJSONObject("topalbums");
            JSONArray secondArray = albumArray.getJSONArray("album");

            for (int i = 0; i < secondArray.length(); i++) {
                JSONObject currentAlbum = secondArray.getJSONObject(i);

                String name = currentAlbum.getString("name");
                String artist = currentAlbum.getJSONObject("artist").getString("name");
                String url = currentAlbum.getString("url");
                String image = currentAlbum.getJSONArray("image").getJSONObject(2).getString("#text");

                Album album = new Album(name, artist, url, image);
                albums.add(album);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }

    private static ArrayList<Album> extractFeatureFromJson2(String albumJson){
        if(TextUtils.isEmpty(albumJson)){
            return null;
        }

        ArrayList<Album> albums = new ArrayList<>();
        Log.d("log",albumJson);
        try{
            JSONObject baseJsonResponse = new JSONObject(albumJson);
            JSONObject albumArray = baseJsonResponse.getJSONObject("results");
            JSONObject secondArray = albumArray.getJSONObject("albummatches");
            JSONArray thirdArray = secondArray.getJSONArray("album");

            for (int i = 0; i < thirdArray.length(); i++) {
                JSONObject currentAlbum = thirdArray.getJSONObject(i);

                String name = currentAlbum.getString("name");
                String artist = currentAlbum.getString("artist");
                String url = currentAlbum.getString("url");
                String image = currentAlbum.getJSONArray("image").getJSONObject(2).getString("#text");

                Album album = new Album(name, artist, url, image);
                albums.add(album);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }
}
