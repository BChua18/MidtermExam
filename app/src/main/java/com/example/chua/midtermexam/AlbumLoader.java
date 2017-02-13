package com.example.chua.midtermexam;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Chua on 2/13/2017.
 */

public class AlbumLoader extends AsyncTaskLoader<ArrayList<Album>> {

    private String mUrl;
    private int requestCode;

    public AlbumLoader(Context context, String mUrl, int requestCode) {
        super(context);
        this.mUrl = mUrl;
        this.requestCode = requestCode;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Album> loadInBackground() {
        if(mUrl == null)
            return null;

        return QueryUtils.fetchAlbumData(mUrl,requestCode);
    }
}
