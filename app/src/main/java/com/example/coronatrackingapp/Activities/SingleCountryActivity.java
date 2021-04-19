package com.example.coronatrackingapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.Models.CountryViewModel;
import com.example.coronatrackingapp.Models.MyApi;
import com.example.coronatrackingapp.Models.SingletonRetrofit;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.OnCountryDBClickListener;
import com.example.coronatrackingapp.databinding.ActivitySingleCountryBinding;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleCountryActivity extends AppCompatActivity  {
    ActivitySingleCountryBinding binding;
    private MyApi myApi;
    private String currCountry;
    private String recovered;
    private String confirmed;
    private String deaths;
    private Boolean isFavourite;
    private CountryViewModel countryViewModel;
    Country currentCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleCountryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        currCountry = getIntent().getStringExtra("country");
        binding.textViewCurrCountry.setText(currCountry);

        setupDataFromDB();

        countryViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CountryViewModel.class);

        //setupRetrofit();
        //apiCallForCountry();
    }

    private void setupDataFromDB(){
        recovered = getIntent().getStringExtra("recovered");
        confirmed = getIntent().getStringExtra("confirmed");
        deaths = getIntent().getStringExtra("deaths");
        isFavourite = getIntent().getBooleanExtra("favourite", false);

        StringBuilder finalResult = new StringBuilder("\n\n");
        finalResult.append("Confirmed: ").append(confirmed).append("\n")
                .append("Recovered: ").append(recovered).append("\n")
                .append("Deaths: ").append(deaths).append("\n");
        binding.textViewInfo.setText(finalResult);
    }

    private void setupRetrofit() {
        myApi = SingletonRetrofit.getRetrofit().create(MyApi.class);
    }

    private void apiCallForCountry() {

        Call<Map<String, Country>> call = myApi.getSpecificCountry(currCountry);
        call.enqueue(new Callback<Map<String, Country>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Country>> call, @NonNull Response<Map<String, Country>> response) {
                Map<String, Country> mapRegions = response.body();
                StringBuilder finalResult = new StringBuilder("\n\n");

                currentCountry = mapRegions.get("All");

                binding.textViewInfo.setText(currCountry);

            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Country>> call, @NonNull Throwable t) {
                Toast.makeText(SingleCountryActivity.this, "Error loading!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}