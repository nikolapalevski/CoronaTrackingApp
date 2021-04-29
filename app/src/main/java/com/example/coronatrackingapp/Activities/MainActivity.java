package com.example.coronatrackingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.coronatrackingapp.Helpers.NotificationHelper;
import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.Models.CountryViewModel;
import com.example.coronatrackingapp.Models.MyApi;
import com.example.coronatrackingapp.Models.SingletonRetrofit;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.Constants;
import com.example.coronatrackingapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private MyApi myApi;
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
        networkCondition();

    }

    private void networkCondition() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        setupRetrofit();
        loadAllCountries();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupRetrofit() {
        myApi = SingletonRetrofit.getRetrofit().create(MyApi.class);
    }

    public void searchFunc(View view) {
        String currCountry = binding.editTextCountry.getText().toString();
        List<Country> searchCountries = new ArrayList<>();
        countryViewModel.getAllCountries().observe(this, (Observer<List<Country>>) countries -> searchCountries.addAll(countries));

        if (!searchCountries.stream().anyMatch(item -> item.getCountryName().toLowerCase().contains(currCountry.toLowerCase()))) {
            Toast.makeText(MainActivity.this, getString(R.string.enter_valid_country), Toast.LENGTH_SHORT).show();
            return;
        }

        binding.editTextCountry.setText("");
        hideKeyboard();
        Country filteredCountry = searchCountries.stream().filter(item -> item.getCountryName().contains(currCountry)).findFirst().orElse(null);
        Intent intent = new Intent(this, SingleCountryActivity.class);
        intent.putExtra(Constants.COUNTRY_EXTRA, filteredCountry);
        startActivity(intent);
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(binding.editTextCountry.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void loadAllCountries() {


        List<Country> favouriteCountryList = new ArrayList<>();
        countryViewModel.getFavouriteCountries().observe(this, (Observer<List<Country>>) countriesFavourite -> favouriteCountryList.addAll(countriesFavourite));
        myApi.getAllCountriesAndRegions().enqueue(new Callback<Map<String, Map<String, Country>>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Map<String, Country>>> call, @NonNull Response<Map<String, Map<String, Country>>> response) {
                Map<String, Map<String, Country>> mapAllCountries = response.body();
                // get keys from MAP (each key is name of country)
                setUpAutoComplete(mapAllCountries.keySet().toArray(new String[0]));
                for (Map.Entry<String, Map<String, Country>> currentCountry : mapAllCountries.entrySet()) {
                    if (currentCountry.getValue() != null) {
                        Map<String, Country> childMap = currentCountry.getValue();
                        for (Map.Entry<String, Country> region : childMap.entrySet()) {
                            Country country = region.getValue();
//                            // add regions to the list but uncomment check for name bellow
//                            if (country.getCountryName() == null) {
//                                country.setCountryName(region.getKey());
//                            }
                            if (country != null) { // this no need if we have the code above
//                              // update or insert data in DB
                                countryViewModel.insertOrUpdate(country);
                                // check if we have favourite items
                                if (favouriteCountryList.size() > 0) {
                                    sendNotification(country, favouriteCountryList);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Map<String, Country>>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(Country country, List<Country> favouriteCountryList) {
        Country favCountry = favouriteCountryList.stream().filter(item -> item.getCountryName().equals(country.getCountryName())).findAny().orElse(null);
        if (favCountry != null) {
            if (Integer.parseInt(country.getConfirmed()) - Integer.parseInt(favCountry.getConfirmed()) > 3000) {
                notificationHelper.sendHighPriorityNotification(favCountry.getCountryName(), Integer.parseInt(country.getConfirmed()) - Integer.parseInt(favCountry.getConfirmed()) + getString(R.string.new_cases), FavouriteCountriesTestActivity.class, favCountry.getId());
            }
            if (Integer.parseInt(country.getDeaths()) - Integer.parseInt(favCountry.getDeaths()) > 100) {
                notificationHelper.sendHighPriorityNotification(favCountry.getCountryName(), Integer.parseInt(country.getDeaths()) - Integer.parseInt(favCountry.getDeaths()) + getString(R.string.new_deaths), FavouriteCountriesTestActivity.class, -favCountry.getId());
            }
        }
    }

    public void startAllCountriesActivity(View view) {
        startActivity(new Intent(this, AllCountriesDatabaseActivity.class));
    }

    public void setUpAutoComplete(String[] countries) {
        binding.editTextCountry.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries));
    }

    public void startFavouriteCountries(View view) {
        startActivity(new Intent(this, FavouriteCountriesTestActivity.class));
    }
}