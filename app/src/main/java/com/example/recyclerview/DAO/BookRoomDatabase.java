package com.example.recyclerview.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.recyclerview.Book;

import java.util.List;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookRoomDatabase extends RoomDatabase {
    public abstract BookDao bookDao();

    /**
     * When you modify the database schema,
     * you'll need to update the version number
     * and define how to handle migrations.
     **/

    private static volatile BookRoomDatabase INSTANCE;

    static BookRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookRoomDatabase.class, "word_database.db")
                            .addMigrations(MIGRATION_1_2)

                            .build();
                }
            }
        }
        return INSTANCE;
    }



    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, LiveData<List<Book>>> {
        private final BookDao mDao;

        PopulateDbAsync(BookRoomDatabase db) {

            mDao = db.bookDao();
        }

        @Override
        protected LiveData<List<Book>> doInBackground(final Void... params) {
            //mDao.deleteAll();
            return mDao.getAllBooks();
        }

    }
}
