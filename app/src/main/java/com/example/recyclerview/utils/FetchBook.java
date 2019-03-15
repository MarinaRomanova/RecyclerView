package com.example.recyclerview.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.recyclerview.Book;
import com.example.recyclerview.BuildConfig;
import com.example.recyclerview.modelJson.GoogleBooksApi.BookJson;
import com.example.recyclerview.modelJson.GoogleBooksApi.VolumeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchBook extends AsyncTask<String, Void, String> {
    //String because the query is a string,
    // Void because there is no progress indicator,
    // and String because the JSON response is a string.


    /**use WeakReference objects for these text views
     * (rather than actual TextView objects)
     * to avoid leaking context from the Activity
     * The weak references prevent memory leaks by allowing the object
     * held by that reference to be garbage-collected if necessary.*/

    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;
    private WeakReference<View> mLoadingIndicator;
    private WeakReference<RecyclerView> mRecyclerView;
    private WeakReference<Context> contextRef;

    //Tag for the log messages
    private static final String LOG_TAG = FetchBook.class.getSimpleName();
    private static final  String BASE_URL = BuildConfig.BASE_URL;

    // Parameter for the search string
    static final String QUERY_PARAM = "q";
    // Parameter to limit search results.
    static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type
    static final String PRINT_TYPE = "printType";

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public FetchBook(TextView titleText, TextView authorText, View  loadingIndicator, RecyclerView recyclerView, Context context ) {
        this.mTitleText = new WeakReference<>(titleText);
        this.mAuthorText = new WeakReference<>(authorText);
        this.mLoadingIndicator = new WeakReference<>(loadingIndicator);
        this.mRecyclerView = new WeakReference<>(recyclerView);
        this.contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(LOG_TAG, "do in background: " + strings[0]);
        HttpHandler handler = new HttpHandler();

        return  handler.fetchData(buildUrl(strings[0]));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ArrayList<Book> list = (ArrayList<Book>) extractFromJson(s);
        if(list !=null || !list.isEmpty()){
            Log.v(LOG_TAG, "Success!!!!" + list.get(0).toString());
            mLoadingIndicator.get().setVisibility(View.GONE);
            mAuthorText.get().setText(list.get(0).getAuthors().get(0));
            mTitleText.get().setText(list.get(0).getTitle());
        }
    }

    public URL buildUrl(String queryString){

        //If needed to get smth from shared preferences:
        //String sortBy = sharedPreferences.getString(getContext().getString(R.string.settings_sort_by_key),
        // getContext().getString(R.string.settings_sort_by_default));

        URL url = null;
        try {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    // Append query parameter and its value.
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "50")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();
            url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static List<Book> extractFromJson(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Book> bookList = new ArrayList<>();

        try {
            BookJson b = objectMapper.readValue(jsonResponse, BookJson.class);
            for (int n=0; n<b.items.size()-1; n++){
                VolumeInfo volumeInfo = b.items.get(n).volumeInfo;
                String title = volumeInfo.getTitle();
                List<String> authors = volumeInfo.getAuthors();
                String image = volumeInfo.getImageLinks().getThumbnail();
                String description = volumeInfo.getDescription();
                String publishedDate = volumeInfo.getPublishedDate().substring(0,4);

                Book book = new Book(title, description, publishedDate, image, authors);

                book.setCategories(volumeInfo.getCategories());
                book.setLanguage(volumeInfo.getLanguage());
                book.setPageCount(volumeInfo.getPageCount());
                book.setPreviewLink(volumeInfo.getPreviewLink());
                book.setPublisher((volumeInfo.getPublisher()));
                bookList.add(book);
                Log.v(LOG_TAG, "book: "+ book.toString());
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookList;
    }

}
