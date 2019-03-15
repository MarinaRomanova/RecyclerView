package com.example.recyclerview.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.recyclerview.Book;
import com.example.recyclerview.BuildConfig;
import com.example.recyclerview.modelJson.GoogleBooksApi.BookJson;
import com.example.recyclerview.modelJson.GoogleBooksApi.VolumeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

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


    public HttpHandler() {
    }

    public String fetchData(URL url) {
        String response = null;
        StringBuilder output = new StringBuilder();
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.getRequestMethod();
            conn.connect();
            Log.v(TAG, conn.getRequestMethod());
            Log.v(TAG, "Response code: " + conn.getResponseCode());
            inputStream = conn.getInputStream();
            // read the response
            if (conn.getResponseCode() == 200) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
                response = output.toString();
            } else {
                Log.e(TAG, "Error response code: " + conn.getResponseCode());
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.v(TAG, "Response from API: " + response);
            return response;
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
                    .appendQueryParameter(MAX_RESULTS, "30")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();
            url = new URL(builtUri.toString());
            //Log.v(TAG, "Built URI " + builtUri.toString());
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
            Log.v(TAG, "inside extract from json" +  jsonResponse);
            for (int n=0; n<b.items.size()-1; n++){
                VolumeInfo volumeInfo = b.items.get(n).volumeInfo;
                String title = volumeInfo.getTitle();
                Log.v(TAG, "title: " +  title);
                List<String> authors = volumeInfo.getAuthors();
                String image = "http://i.imgur.com/sJ3CT4V.gif";
                try{
                    image = volumeInfo.getImageLinks().getThumbnail();
                } catch (Exception err){
                    Log.v(TAG, err.getMessage());
                }
                //String image = volumeInfo.getImageLinks().getThumbnail();

                String description = volumeInfo.getDescription();
                String publishedDate = volumeInfo.getPublishedDate();
                if (publishedDate!=null){
                    if(publishedDate.length()>10)
                        publishedDate = publishedDate.substring(0,10);
                    else if (publishedDate.length()==4)
                        publishedDate = publishedDate.substring(0,4);
                }
                Book book = new Book(title, description, publishedDate, image, authors);
                book.setCategories(volumeInfo.getCategories());
                book.setLanguage(volumeInfo.getLanguage());
                book.setPageCount(volumeInfo.getPageCount());
                book.setPreviewLink(volumeInfo.getPreviewLink());
                book.setPublisher((volumeInfo.getPublisher()));
                bookList.add(book);
                Log.v(TAG, "book: "+ book.toString());
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookList;
    }
}