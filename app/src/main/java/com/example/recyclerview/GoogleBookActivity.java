package com.example.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.recyclerview.recycleAdapters.GoogleBookAdapter;
import com.example.recyclerview.utils.GoogleBookLoader;

import java.util.ArrayList;
import java.util.List;

public class GoogleBookActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER_ID = 1;
    private static final String LOG_TAG = GoogleBookActivity.class.getSimpleName();
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;
    private RecyclerView mRecyclerView;
    private View loadingIndicator;
    private GoogleBookAdapter googleBookAdapter;
    private ArrayList<Book> mBooksData;
    private LinearLayout results;
    private TextView resultsTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_book);
        mBooksData = new ArrayList<>();

        mBookInput = findViewById(R.id.bookInput);
        mTitleText = findViewById(R.id.titleText);
        mAuthorText = findViewById(R.id.authorText);
        loadingIndicator = findViewById(R.id.loading_indicator);
        mRecyclerView = findViewById(R.id.recyclerview_google);
        results = findViewById(R.id.results);
        resultsTextView = findViewById(R.id.resultsTextView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        googleBookAdapter = new GoogleBookAdapter(this, mBooksData);
        //mRecyclerView.setAdapter(googleBookAdapter);
    }

    public void searchBooks(View view) {
        // Get the search string from the input field.
        String queryString = mBookInput.getText().toString();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        } else {
            Log.d(LOG_TAG, "connMngr is  null");
        }
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            //new FetchBook(mTitleText, mAuthorText, loadingIndicator, mRecyclerView, this).execute(queryString);

            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(BOOK_LOADER_ID, queryBundle, this);

           // mAuthorText.setText("");
            //mTitleText.setText(getString(R.string.Loading));
            loadingIndicator.setVisibility(View.VISIBLE);
            resultsTextView.setText(R.string.results_search);
            resultsTextView.setVisibility(View.GONE);
        }

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        //The code hides the keyboard when the user taps the button.
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if (args != null) {
            queryString = args.getString("queryString");
        }

        return new GoogleBookLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> list) {

        loadingIndicator.setVisibility(View.GONE);
        results.setVisibility(View.VISIBLE);
        if (!list.isEmpty()){
            mBooksData.clear();

            for(Book book: list){
               mBooksData.add(book);
            }
            mRecyclerView.removeAllViewsInLayout();
            mRecyclerView.setAdapter(new GoogleBookAdapter(this, mBooksData));

        } else {
            resultsTextView.setVisibility(View.VISIBLE);
            resultsTextView.setText(R.string.not_found);
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        mRecyclerView.removeAllViewsInLayout();

    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            ;// One is your argument i
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("tab", 2);
            startActivity(intent);
            return true;
        }

        return false;
    }
}
