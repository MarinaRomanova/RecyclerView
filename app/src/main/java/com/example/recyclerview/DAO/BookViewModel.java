package com.example.recyclerview.DAO;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.recyclerview.Book;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepository mRepository;

    private LiveData<List<Book>> mAllBooks;

    public BookViewModel( Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllBooks = mRepository.getmAllBooks();
    }

    public LiveData<List<Book>> getmAllBooks() {
        return mAllBooks;
    }

    public void insert(Book book){
        mRepository.insert(book);
    }

    public void delete(Book book){
        mRepository.delete(book);
    }
}
