package com.example.recyclerview;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.recyclerview.DAO.BookViewModel;
import com.example.recyclerview.recycleAdapters.GoogleBookAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FavoritesFragment extends Fragment {
    BookViewModel bookViewModel;

    private static final String LOG = FavoritesFragment.class.getSimpleName();
    private static final int LOADER_ID = 3;
    private RecyclerView mRecyclerView;
    private ArrayList<Book> mBooksData;
    View rootView;
    GoogleBookAdapter googleBookAdapter;

    RelativeLayout emptyViews;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        emptyViews = rootView.findViewById(R.id.empty_view);

        mRecyclerView = rootView.findViewById(R.id.favorites);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rootView.findViewById((R.id.empty_view)).setVisibility(View.VISIBLE);

        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        bookViewModel.getmAllBooks().observe(getActivity(), new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {

                if (books != null) {
                    rootView.findViewById((R.id.empty_view)).setVisibility(View.GONE);
                }
                mBooksData = (ArrayList<Book>) books;

                googleBookAdapter = new GoogleBookAdapter(getContext(), (ArrayList<Book>) books);
                mRecyclerView.setAdapter(googleBookAdapter);
            }
        });


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
                        googleBookAdapter.notifyItemMoved(from, to);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                        bookViewModel.delete(mBooksData.get(viewHolder.getAdapterPosition()));
                        mBooksData.remove(viewHolder.getAdapterPosition());
                        //to allow the RecyclerView to animate the deletion properly, you must also call notifyItemRemoved()
                        googleBookAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                }));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(getContext(), GoogleBookActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
