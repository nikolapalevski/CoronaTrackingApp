package com.example.coronatrackingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.coronatrackingapp.Helpers.NotificationHelper;
import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.Models.CountryRepository;
import com.example.coronatrackingapp.Models.CountryViewModel;
import com.example.coronatrackingapp.Models.MyApi;
import com.example.coronatrackingapp.Models.SingletonRetrofit;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.Constants;
import com.example.coronatrackingapp.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private MyApi myApi;
    private String[] countries;
    private CountryViewModel countryViewModel;
    NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        countryViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CountryViewModel.class);

        notificationHelper = new NotificationHelper(this);

        setupRetrofit();

        if (isNetworkAvailable()) {
            loadAllCountries();
        } else {
            Toast.makeText(this, "No internet available.", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupRetrofit() {
        myApi = SingletonRetrofit.getRetrofit().create(MyApi.class);
    }

    public void searchFunc(View view) {
        String currCountry = binding.editTextCountry.getText().toString();

        if (Arrays.asList(countries).contains(currCountry)) {
            countryViewModel.getAllCountries().observe(this, (Observer<List<Country>>) countries -> {
                for (int i = 0; i < countries.size(); i++) {
                    Country currCountryDb = countries.get(i);
                    if (currCountryDb.getCountryName().equals(currCountry)) {
                        Intent intent = new Intent(this, SingleCountryActivity.class);
                        intent.putExtra(Constants.COUNTRY_EXTRA, currCountryDb.getCountryName());
                        intent.putExtra(Constants.CONFIRMED_EXTRA, currCountryDb.getConfirmed());
                        intent.putExtra(Constants.RECOVERED_EXTRA, currCountryDb.getRecovered());
                        intent.putExtra(Constants.DEATHS_EXTRA, currCountryDb.getDeaths());
                        intent.putExtra(Constants.FAVOURITE_EXTRA, currCountryDb.isFavourite());
                        startActivity(intent);
                    }
                }
            });
        } else Toast.makeText(MainActivity.this, getString(R.string.enter_valid_country), Toast.LENGTH_SHORT).show();

        binding.editTextCountry.setText("");
        hideKeyboard();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(binding.editTextCountry.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void loadAllCountries() {


        countryViewModel.getFavouriteCountries().observe(this, (Observer<List<Country>>) countriesFavourite -> {

            myApi.getAllCountriesAndRegions().enqueue(new Callback<Map<String, Map<String, Country>>>() {
                @Override
                public void onResponse(@NonNull Call<Map<String, Map<String, Country>>> call, @NonNull Response<Map<String, Map<String, Country>>> response) {
                    Map<String, Map<String, Country>> mapAllCountries = response.body();
                    countries = mapAllCountries.keySet().toArray(new String[0]);
                    for (Map.Entry<String, Map<String, Country>> drzava : mapAllCountries.entrySet()) {
                        Map<String, Country> childMap = drzava.getValue();
                        for (Map.Entry<String, Country> region : childMap.entrySet()) {
                            if (region.getValue() != null) {
                                countryViewModel.insertOrUpdate(region.getValue());

                                for (int i = 0; i < countriesFavourite.size(); i++) {
                                    if (countriesFavourite.get(i).getCountryName().equals(region.getValue().getCountryName())) {
                                        //Toast.makeText(MainActivity.this, region.getValue().getCountryName() + " uslov", Toast.LENGTH_SHORT).show();
                                        if (Integer.parseInt(region.getValue().getConfirmed()) - Integer.parseInt(countriesFavourite.get(i).getConfirmed()) > 0 ) {
                                            //Toast.makeText(MainActivity.this, countriesFavourite.get(i).getConfirmed() + "  confirmed vs.  " + region.getValue().getConfirmed(), Toast.LENGTH_SHORT).show();
                                            notificationHelper.sendHighPriorityNotification(countriesFavourite.get(i).getCountryName(), "Too many new cases registered!", MainActivity.class);
                                        }
                                        if (Integer.parseInt(region.getValue().getDeaths()) - Integer.parseInt(countriesFavourite.get(i).getDeaths()) > 0 ) {
                                            //Toast.makeText(MainActivity.this, countriesFavourite.get(i).getDeaths() + "  deaths vs.  " + region.getValue().getDeaths(), Toast.LENGTH_SHORT).show();
                                            notificationHelper.sendHighPriorityNotification(countriesFavourite.get(i).getCountryName(), "Too many victims today", MainActivity.class);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    setUpAutoComplete();
                }

                @Override
                public void onFailure(@NonNull Call<Map<String, Map<String, Country>>> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void startAllCountriesActivity(View view) {

        if (isNetworkAvailable()) {
            Intent intent = new Intent(this, AllCountriesActivity.class);
            intent.putExtra(Constants.COUNTRIES_EXTRA, countries);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AllCountriesDatabaseActivity.class);
            startActivity(intent);
        }
    }

    public void startFavouriteCountriesActivity(View view) {
        Intent intent = new Intent(this, AllCountriesDatabaseActivity.class);
        startActivity(intent);
    }

    public void setUpAutoComplete() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        binding.editTextCountry.setAdapter(adapter);
    }

    public void startAllCountryActivityFavCountries(View view) {

        Intent intent = new Intent(this, FavouriteCountriesTestActivity.class);
        startActivity(intent);
    }

    public void sendNotification(View view) {

        notificationHelper.sendHighPriorityNotification("Title text", "body text", MainActivity.class);
    }


}