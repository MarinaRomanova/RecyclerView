package com.example.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recyclerview.Book;
import com.example.recyclerview.BookAdapter;
import com.example.recyclerview.GoogleBookActivity;
import com.example.recyclerview.MainActivity;
import com.example.recyclerview.R;
import com.example.recyclerview.utils.NYTimesLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopTenBestsellersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<List<Book>>{

    View rootView;

    private static final String LOG = MainActivity.class.getSimpleName() ;
    private static final int LOADER_ID = 0;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<Book> mBooksData;
    private BookAdapter mAdapter;

    public TopTenBestsellersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_top_ten_bestsellers, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview);


        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the ArrayList that will contain the data.
        mBooksData = new ArrayList<>();
        // Get the data.
        initializeData();
        mAdapter = new BookAdapter(getActivity(), mBooksData);
        mRecyclerView.setAdapter(mAdapter);

        /*If you are only implementing swipe to dismiss at the moment, you should pass in 0
        for the supported move directions and ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        for the supported swipe directions*/
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();
                        Collections.swap(mBooksData, from, to);
                        mAdapter.notifyItemMoved(from, to);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                        mBooksData.remove(viewHolder.getAdapterPosition());
                        //to allow the RecyclerView to animate the deletion properly, you must also call notifyItemRemoved()
                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                }));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onRefresh() {
        initializeData();
        mSwipeRefreshLayout.setRefreshing(false);
    }
    private void initializeData(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        } else {
            Log.d(LOG, "connMngr is  null");
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            //new FetchBook(mTitleText, mAuthorText, loadingIndicator, mRecyclerView, this).execute(queryString);

            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }


    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v("Test", "onCreateLoader is called");
        return new NYTimesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> list) {
        if (!list.isEmpty() ) {
            TypedArray booksImageResources =
                    getResources().obtainTypedArray(R.array.books_images);

            for (Book book : list) {
                if (!mBooksData.contains(book)) {
                    mBooksData.add(book);
                    Log.v("Test", book.toString());
                }
            }


            for (int i = 0; i < mBooksData.size(); i++) {
                if(i<10)
                mBooksData.get(i).setImageResource(booksImageResources.getResourceId(i, 0));
                else mBooksData.get(i).setImageResource(R.drawable.library);
            }

            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        mRecyclerView.removeAllViewsInLayout();
    }


}
