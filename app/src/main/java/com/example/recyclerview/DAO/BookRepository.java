package com.example.recyclerview.DAO;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.recyclerview.Book;

import java.util.List;

public class BookRepository {

    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    public BookRepository(Application application) {
        BookRoomDatabase db = BookRoomDatabase.getDatabase(application);
        this.mBookDao = db.bookDao();
        this.mAllBooks = mBookDao.getAllBooks();
    }

    LiveData<List<Book>> getmAllBooks() {
        return mAllBooks;
    }

    public void insert (Book book) {
        new insertAsyncTask(mBookDao).execute(book);
    }

    public void delete (Book book) {
        new deleteAsyncTask(mBookDao).execute(book);
    }

    private static class insertAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao mAsyncTaskDao;

        insertAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Book... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao mAsyncTaskDao;

        deleteAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Book... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }



}
