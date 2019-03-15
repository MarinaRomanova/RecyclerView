package com.example.recyclerview.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.example.recyclerview.Book;
import com.example.recyclerview.BuildConfig;
import com.example.recyclerview.modelJson.GoogleBooksApi.VolumeInfo;
import com.example.recyclerview.modelJson.NYTimesApi.BookNY;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NYTimesLoader extends AsyncTaskLoader<List<Book>> {

    private static final String TAG = NYTimesLoader.class.getSimpleName();
    private static final String URL = BuildConfig.API_KEY_URL;

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public NYTimesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<Book> loadInBackground() {
        HttpHandler handler = new HttpHandler();
        List<Book> books = new ArrayList<>();
        try {
            books = extractFromJson(handler.fetchData(new URL(URL)));
            Log.v(TAG, "NY times: " + books);
        } catch (Exception err) {
            Log.e(TAG, err.getMessage());
        }

        return books;
    }



    private List<Book> extractFromJson(String jsonResponse) {
        Log.v(TAG, "json: " + jsonResponse);
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Book> bookList = new ArrayList<>();

        try {
            BookNY b = objectMapper.readValue(jsonResponse, BookNY.class);
            Log.v(TAG, b.toString());
            for (int n = 0; n < 10 - 1; n++) {
                String title = b.results.get(n).title;
                String description = b.results.get(n).description;
                String author = b.results.get(n).author;
                if(title!=null && description !=null && author !=null)
                bookList.add(new Book(title, description, author));
            }

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookList;
    }

}
