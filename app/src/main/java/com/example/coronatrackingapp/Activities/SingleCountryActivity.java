package com.example.coronatrackingapp.Activities;

import android.os.Bundle;
import android.view.View;

import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.Utils.Constants;
import com.example.coronatrackingapp.databinding.ActivitySingleCountryBinding;

import androidx.appcompat.app.AppCompatActivity;

public class SingleCountryActivity extends AppCompatActivity {
    ActivitySingleCountryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleCountryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupDataFromDB();

    }

    private void setupDataFromDB() {

        Country currCountry = (Country) getIntent().getSerializableExtra(Constants.COUNTRY_EXTRA);
        assert currCountry != null;
        binding.textViewCurrCountry.setText(currCountry.getCountryName());

        StringBuilder confirmedBuilder = new StringBuilder();
        confirmedBuilder.append("Confirmed: ").append(currCountry.getConfirmed());
        StringBuilder recoveredBuilder = new StringBuilder();
        recoveredBuilder.append("Recovered: ").append(currCountry.getRecovered());
        StringBuilder deathsBuilder = new StringBuilder();
        deathsBuilder.append("Deaths: ").append(currCountry.getDeaths());

        binding.textViewConfirmed.setText(confirmedBuilder);
        binding.textViewRecovered.setText(recoveredBuilder);
        binding.textViewDeaths.setText(deathsBuilder);
    }

}