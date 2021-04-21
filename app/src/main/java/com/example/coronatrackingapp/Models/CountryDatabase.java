package com.example.coronatrackingapp.Models;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Country.class}, version = 3)
public abstract class CountryDatabase extends RoomDatabase {

    private static CountryDatabase instance;

    public abstract CountryDao countryDao();

    public static synchronized CountryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CountryDatabase.class, "country_database")
                    .fallbackToDestructiveMigration() //todo replace with migration
                    .build();
        }
        return instance;
    }

}
