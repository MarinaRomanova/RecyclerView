package com.example.recyclerview.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.recyclerview.Book;

import java.util.List;

@Dao
public interface BookDao {
    //Default strategy is abort so that item with the same primary key could not be inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book book);

    @Delete
    void delete(Book book);

    @Query("DELETE FROM book_table")
    void deleteAll();

    @Query("SELECT * from book_table ORDER BY title ASC")
    LiveData<List<Book>> getAllBooks(); //LiveData, a lifecycle library class for data observation
}
