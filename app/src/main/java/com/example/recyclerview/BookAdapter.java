package com.example.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>  {

    private ArrayList<Book> mBooksData;
    private Context mContext;

    /**
     * Constructor that passes in the books data and the context.
     */
    public BookAdapter(Context context, ArrayList<Book> data) {
        this.mBooksData = data;
        this.mContext = context;
    }

    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent The ViewGroup into which the new View will be added
     *               after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false));
    }

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder,
                                 int position) {
        Book currentBook = mBooksData.get(position);
        // Populate the textviews with data.
        holder.mTitleText.setText(currentBook.getTitle() + " - " + currentBook.getAuthor());
        holder.mInfoText.setText(currentBook.getDescription());
        Glide.with(mContext).load(currentBook.getImageResource()).into(holder.mBooksImage);
    }

    /**
     * Required method for determining the size of the data set.
     *
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return mBooksData.size();
    }


    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mBooksImage;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         *
         * @param itemView The rootview of the list_item.xml layout file.
         */
        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.title);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mBooksImage = itemView.findViewById(R.id.bookImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*
            Book currentBook = mBooksData.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, BookDetailActivity.class);
            detailIntent.putExtra("title",currentBook.getTitle() + " - " + currentBook.getAuthor());
            detailIntent.putExtra("image_resource",
                    currentBook.getImageResource());
            detailIntent.putExtra("description", currentBook.getDescription());
            mContext.startActivity(detailIntent);
            */
            Book currentBook = mBooksData.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, GoogleBookDetail.class);
            detailIntent.putExtra("queryString", currentBook.getTitle());
            mContext.startActivity(detailIntent);

        }
    }
}
