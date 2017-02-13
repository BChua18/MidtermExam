package com.example.chua.midtermexam;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chua on 2/13/2017.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Album>> {

    private static final String REQUEST_URL = "http://ws.audioscrobbler.com/2.0/?" ;

    private static final int ALBUM_LOADER_ID1 = 1;
    private static final int ALBUM_LOADER_ID2 = 2;
    private int searchBy = 1;
    private TextView mEmptyStateTextView;
    private TextView label;
    private TextView searchTxt;
    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private String search = "";
    private AlbumsAdapter albumsAdapter;
    private LoaderManager loaderManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Album Finder");

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#192ae7")));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        label = (TextView) findViewById(R.id.label);
        searchTxt = (TextView) findViewById(R.id.searchTxt);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        mEmptyStateTextView.setText(R.string.no_album_present);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {


        } else {
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    setSearch(searchTxt.getText().toString());
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public Loader<ArrayList<Album>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(id == ALBUM_LOADER_ID1) {
            uriBuilder.appendQueryParameter("method", "artist.gettopalbums");
            uriBuilder.appendQueryParameter("artist", getSearch()); // edit later
            uriBuilder.appendQueryParameter("api_key", "12696eb0c1e42d1b92a4293a54269236");
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }
        else if (id == ALBUM_LOADER_ID2){
            uriBuilder.appendQueryParameter("method", "album.search");
            uriBuilder.appendQueryParameter("album", getSearch()); // edit later
            uriBuilder.appendQueryParameter("api_key", "12696eb0c1e42d1b92a4293a54269236");
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }
        Log.d("logs",uriBuilder.toString());

        return new AlbumLoader(this,uriBuilder.toString(),id);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Album>> loader, ArrayList<Album> data) {
        recyclerView.setVisibility(View.VISIBLE);
        albumsAdapter = new AlbumsAdapter(this,data);
        if(data != null && !data.isEmpty()){
            mEmptyStateTextView.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(albumsAdapter);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        getLoaderManager().destroyLoader(searchBy);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Album>> loader) {
    }

    public void performSearch(){
        mEmptyStateTextView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(searchBy, null, this);
        hideSoftKeyboard(MainActivity.this);

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.clear){
            searchTxt.setText("");
            recyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        if(id == R.id.artistName){
            label.setText("Search album by artist");
            searchBy = 1;
        }
        if(id == R.id.albumName){
            label.setText("Search album by name");
            searchBy = 2;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
