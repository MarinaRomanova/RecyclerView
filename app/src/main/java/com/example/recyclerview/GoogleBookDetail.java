package com.example.recyclerview;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recyclerview.DAO.BookViewModel;
import com.example.recyclerview.modelJson.GoogleBooksApi.BookJson;
import com.example.recyclerview.modelJson.GoogleBooksApi.VolumeInfo;
import com.example.recyclerview.recycleAdapters.GoogleBookAdapter;
import com.example.recyclerview.utils.GoogleBookLoader;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleBookDetail extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = GoogleBookDetail.class.getSimpleName();
    TextView titleTextView;
    TextView authorsTextView;
    TextView publisherTextView;
    TextView publishedDateTextView;
    TextView pageCountTextView;
    TextView languageTextView;
    TextView previewLinkTextView;
    TextView descriptionTextView;
    TextView categoriesTextView;
    ImageView imageView;
    Button buttonAddToFav;

    Book book;

    private BookViewModel bookViewModel;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_book_detail);
        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        final Intent intent = getIntent();
        buttonAddToFav = findViewById(R.id.btn_add_to_fav);
        titleTextView = findViewById(R.id.tv_title_google_detail);
        authorsTextView = findViewById(R.id.tv_authors_google_detail);
        publisherTextView = findViewById(R.id.tv_publisher_google_detail);
        publishedDateTextView = findViewById(R.id.tv_published_date_google_detail);
        pageCountTextView = findViewById(R.id.tv_pages_google_detail);
        languageTextView = findViewById(R.id.tv_language_google_detail);
        previewLinkTextView = findViewById(R.id.tv_previewLink_google_detail);
        descriptionTextView = findViewById(R.id.tv_description_google_detail);
        categoriesTextView = findViewById(R.id.tv_categories_google_detail);
        imageView = findViewById(R.id.thumbnail_image_google_detail);

        if(intent.getExtras().getBoolean("isFavorite")){
            buttonAddToFav.setText(getString(R.string.added_to));
            buttonAddToFav.setBackground(ContextCompat.getDrawable(this, R.drawable.button_shape_disabled));
            buttonAddToFav.setEnabled(false);
        }

        if (intent.getStringExtra("queryString") != null) {
            searchBook(intent.getStringExtra("queryString"));

        } else {
            book = new Book();
            book.setTitle(intent.getStringExtra("title"));
            titleTextView.setText(intent.getStringExtra("title"));
            book.setAuthor(intent.getStringExtra("authors"));
            authorsTextView.setText(intent.getStringExtra("authors"));
            book.setPublisher(intent.getStringExtra("publisher"));
            publisherTextView.setText(getString(R.string.publisher) + " " + intent.getStringExtra("publisher"));
            book.setDate(intent.getStringExtra("publishedDate"));
            publishedDateTextView.setText(getString(R.string.published_at) + " " + intent.getStringExtra("publishedDate"));
            book.setPageCount(intent.getIntExtra("pageCount", 0));
            pageCountTextView.setText(getString(R.string.pages) + " " + intent.getIntExtra("pageCount", 0));
            book.setLanguage(intent.getStringExtra("languages"));
            languageTextView.setText(getString(R.string.language) + " " + intent.getStringExtra("languages"));
            book.setPreviewLink(intent.getStringExtra("previewLink"));
            previewLinkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("previewLink")));
                    startActivity(websiteIntent);

                }
            });
            book.setDescription(intent.getStringExtra("description"));
            descriptionTextView.setText(intent.getStringExtra("description"));
            book.setCategory(intent.getStringExtra("categories"));
            categoriesTextView.setText(intent.getStringExtra("categories"));

            book.setThumbnailUrl(intent.getStringExtra("image"));
            Glide.with(this).load(intent.getStringExtra("image")).into(imageView);
        }
    }

    public void searchBook(String queryString) {

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
            getSupportLoaderManager().restartLoader(2, null, this);
        }
    }


    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        return new GoogleBookLoader(this, getIntent().getStringExtra("queryString"));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> list) {

        if (!list.isEmpty()) {
            book = list.get(0);
            Log.v(LOG_TAG, book.toString());
            titleTextView.setText(book.getTitle());
            authorsTextView.setText(book.getAuthors().get(0));
            publisherTextView.setText(getString(R.string.publisher) + " " + book.getPublisher());
            publishedDateTextView.setText(getString(R.string.published_at) + " " + book.getDate());
            pageCountTextView.setText(getString(R.string.pages) + " " + book.getPageCount());
            languageTextView.setText(getString(R.string.language) + " " + book.getLanguage());
            previewLinkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getPreviewLink()));
                    startActivity(websiteIntent);

                }
            });
            descriptionTextView.setText(book.getDescription());
            categoriesTextView.setText(book.getCategories().get(0));
            Glide.with(this).load(book.getThumbnailUrl()).into(imageView);

        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addToFav(View view) {
        bookViewModel.insert(book);
        book.setAddedToFavorite(true);
        Log.v(LOG_TAG, "inserted book: " + book.toString());
        Toast.makeText(this, "Book successfully added to your favories!", Toast.LENGTH_SHORT).show();
        buttonAddToFav.setText(getString(R.string.added_to));
        buttonAddToFav.setBackground(ContextCompat.getDrawable(this, R.drawable.button_shape_disabled));
        buttonAddToFav.setEnabled(false);
    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, GoogleBookActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
