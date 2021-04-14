package com.example.coronatrackingapp.Models;

import android.content.Context;
import android.os.AsyncTask;

import com.example.coronatrackingapp.Utils.OnFavouriteCountryClickListener;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Country.class}, version = 1)
public abstract class CountryDatabase extends RoomDatabase {

    private static CountryDatabase instance;

    public abstract CountryDao countryDao();

    public static synchronized CountryDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CountryDatabase.class, "country_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private CountryDao countryDao;
        private PopulateDbAsyncTask(CountryDatabase db){
            countryDao = db.countryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            countryDao.insert(new Country("Macedonia"));
            countryDao.insert(new Country("Spain"));
            countryDao.insert(new Country("France"));
            return null;
        }
    }

}
