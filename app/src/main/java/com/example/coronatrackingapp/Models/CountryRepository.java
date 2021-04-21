package com.example.coronatrackingapp.Models;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.coronatrackingapp.Activities.MainActivity;
import com.example.coronatrackingapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryRepository<T> {
    private CountryDao countryDao;
    private LiveData<List<Country>> allCountries;

    private LiveData<List<Country>> favouriteCountries;
    MyApi myApi = SingletonRetrofit.getRetrofit().create(MyApi.class);


    private void updateDB() {

        myApi.getAllCountriesAndRegions().enqueue(new Callback<Map<String, Map<String, Country>>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Map<String, Country>>> call, @NonNull Response<Map<String, Map<String, Country>>> response) {
                Map<String, Map<String, Country>> mapAllCountries = response.body();
                //countries = mapAllCountries.keySet().toArray(new String[0]);
                List<Country> countryDBList = new ArrayList<>();
                for (Map.Entry<String, Map<String, Country>> drzava : mapAllCountries.entrySet()) {
                    Map<String, Country> childMap = drzava.getValue();
                    for (Map.Entry<String, Country> region : childMap.entrySet()) {
                        if (region.getValue() != null) {
                            countryDBList.add(region.getValue());
                            insertOrUpdate(region.getValue());
                        } else Log.i("REGION_NULL", region.getKey());

                    }
                }
                //setUpAutoComplete();
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Map<String, Country>>> call, @NonNull Throwable t) {
                //Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public CountryRepository(Application application) {
        CountryDatabase database = CountryDatabase.getInstance(application);
        countryDao = database.countryDao();
        allCountries = countryDao.getAllNotes();
        favouriteCountries = countryDao.getAllFavourite();
    }

    public void insert(T country) {
        new InsertCountryAsyncTask(countryDao).execute(country);

    }

    public void update(Country country) {
        int countryID = countryDao.getCountryId(country.getCountryName());
        new UpdateCountryAsyncTask(countryDao, countryID).execute(country);

    }

    public void delete(Country country) {
        new DeleteCountryAsyncTask(countryDao).execute(country);

    }

    public void updateFavourite(Country country) {
        new UpdateFavouriteAsyncTask(countryDao).execute(country);
    }

    public void insertOrUpdate(Country country) {

        new InsertOrUpdateAsyncTask(countryDao);
    }

    public void deleteAllCountries() {
        new DeleteAllCountriesAsyncTask(countryDao).execute();
    }

    public LiveData<List<Country>> getAllCountries() {
        return allCountries;
    }

    public LiveData<List<Country>> getFavouriteCountries() {
        return favouriteCountries;
    }


    private static class InsertCountryAsyncTask<T> extends AsyncTask<T, Void, Void> {
        private CountryDao countryDao;

        private InsertCountryAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(T... params) {
            if (params[0] instanceof Country) {
                countryDao.insert((Country) params[0]);

            } else {
                countryDao.insert((List<Country>) params[0]);
            }
            return null;
        }
    }


    private static class InsertOrUpdateAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDao countryDao;

        private InsertOrUpdateAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            Country c = countries[0];
            if (c != null) {
                int countryId = countryDao.getCountryId(c.getCountryName());
                boolean countryExist = countryId > -1;
                if (countryExist) {
                    countryDao.update(c.getConfirmed(), c.getRecovered(), c.getDeaths(), countryId);
                } else {
                    countryDao.insert(c);
                }
            }
            return null;
        }
    }


    private static class UpdateCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDao countryDao;
        private int countryId;

        private UpdateCountryAsyncTask(CountryDao countryDao, int countryId) {
            this.countryDao = countryDao;
            this.countryId = countryId;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            for (Country c :
                    countries) {
                countryDao.update(c.getConfirmed(), c.getRecovered(), c.getDeaths(), countryId);
            }
            return null;
        }
    }

    private static class DeleteCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDao countryDao;

        private DeleteCountryAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDao.delete(countries[0]);
            return null;
        }
    }

    private static class UpdateFavouriteAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDao countryDao;

        private UpdateFavouriteAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDao.updateFavourite(countries[0]);
            return null;
        }
    }

    private static class DeleteAllCountriesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CountryDao countryDao;

        private DeleteAllCountriesAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            countryDao.deleteAllCountries();
            return null;
        }
    }

}
