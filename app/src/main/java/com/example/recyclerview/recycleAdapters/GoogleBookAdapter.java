package com.example.recyclerview.recycleAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recyclerview.Book;
import com.example.recyclerview.BookDetailActivity;
import com.example.recyclerview.GoogleBookDetail;
import com.example.recyclerview.R;

import java.util.ArrayList;

public class GoogleBookAdapter extends RecyclerView.Adapter<GoogleBookAdapter.ViewHolder> {

    private static final String LOG_TAG = GoogleBookAdapter.class.getSimpleName();
    private ArrayList<Book> mBooksData;
    private Context mContext;

    /**
     * Constructor that passes in the books data and the context.
     */
    public GoogleBookAdapter(Context context, ArrayList<Book> data) {
        this.mBooksData = data;
        this.mContext = context;

    }

    public void setmBooksData(ArrayList<Book> mBooksData) {
        Log.v(LOG_TAG, "data received in Adapter: " + mBooksData);
        this.mBooksData = mBooksData;
    }

    public ArrayList<Book> getmBooksData() {
        return mBooksData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(LOG_TAG, "in oncreateviewolder");
        return new GoogleBookAdapter.ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.google_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book currentBook = mBooksData.get(position);
        // Populate the textviews with data.
        holder.mTitleTextView.setText(currentBook.getTitle());
        holder.mDescriptionTextView.setText(currentBook.getDescription());
        Glide.with(mContext).load(currentBook.getThumbnailUrl()).into(holder.mBooksImageView);
        Log.v(LOG_TAG, "current book: " + currentBook.toString());
    }

    @Override
    public int getItemCount() {
        return mBooksData.size();
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private ImageView mBooksImageView;
//        private TextView mAuthorsTextView;
//        private TextView mDateTextView;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         *
         * @param itemView The rootview of the list_item.xml layout file.
         */
        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mDescriptionTextView = itemView.findViewById(R.id.tv_description_google);
            mTitleTextView = itemView.findViewById(R.id.tv_title_google);
            mBooksImageView = itemView.findViewById(R.id.thumbnail_image_google);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Book currentBook = mBooksData.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, GoogleBookDetail.class);
            detailIntent.putExtra("title", currentBook.getTitle());
            detailIntent.putExtra("image",
                    currentBook.getThumbnailUrl());
            detailIntent.putExtra("description", currentBook.getDescription());
            String authorsString = "";

            if(currentBook.getAuthors() !=null){
                for (String author : currentBook.getAuthors()){
                    authorsString = authorsString + author + ", ";
                    detailIntent.putExtra("authors", authorsString.substring(0, authorsString.length()-2));
                }
            } else {
                detailIntent.putExtra("authors", currentBook.getAuthor());
            }

            detailIntent.putExtra("publisher", currentBook.getPublisher());
            detailIntent.putExtra("publishedDate", currentBook.getDate());
            detailIntent.putExtra("pageCount", currentBook.getPageCount());
            detailIntent.putExtra("languages", currentBook.getLanguage());
            detailIntent.putExtra("previewLink", currentBook.getPreviewLink());

            detailIntent.putExtra("isFavorite", currentBook.getAddedToFavorite());

            String categoriesStr = "";
             if(currentBook.getCategories()!=null){
                 for (String category: currentBook.getCategories()) {
                     categoriesStr = categoriesStr + category + ", ";
                 }
                 detailIntent.putExtra("categories", categoriesStr.substring(0, categoriesStr.length()-2));
             } else{
                 Log.v(LOG_TAG, "currentBook.getCategory() " + currentBook.getCategories() );
                 detailIntent.putExtra("categories", currentBook.getCategory());
             }

            Log.v(LOG_TAG, "lINK" + currentBook.getPreviewLink());
            mContext.startActivity(detailIntent);

        }
    }

}

