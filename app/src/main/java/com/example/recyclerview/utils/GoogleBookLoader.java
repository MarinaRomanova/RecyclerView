package com.example.recyclerview.utils;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.recyclerview.Book;

import java.util.ArrayList;
import java.util.List;

public class GoogleBookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String TAG = GoogleBookLoader.class.getSimpleName();
    private String mQueryString;

    public GoogleBookLoader(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        HttpHandler handler = new HttpHandler();
        String response = handler.fetchData(handler.buildUrl(mQueryString));
        if(response!=null)
            return HttpHandler.extractFromJson(response);
        else return null;
    }
}
