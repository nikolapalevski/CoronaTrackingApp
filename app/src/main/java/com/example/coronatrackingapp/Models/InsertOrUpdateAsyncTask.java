package com.example.coronatrackingapp.Models;

import android.os.AsyncTask;

public class InsertOrUpdateAsyncTask extends AsyncTask<Country, Void, Void> {
    private CountryDao countryDao;

    InsertOrUpdateAsyncTask(CountryDao countryDao) {
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