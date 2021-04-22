package com.example.coronatrackingapp.Activities;

import android.os.Bundle;
import android.view.View;

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
        String currCountry = getIntent().getStringExtra(Constants.COUNTRY_EXTRA);
        binding.textViewCurrCountry.setText(currCountry);

        setupDataFromDB();

    }

    private void setupDataFromDB() {
        String recovered = getIntent().getStringExtra(Constants.RECOVERED_EXTRA);
        String confirmed = getIntent().getStringExtra(Constants.CONFIRMED_EXTRA);
        String deaths = getIntent().getStringExtra(Constants.DEATHS_EXTRA);

        StringBuilder confirmedBuilder = new StringBuilder();
        confirmedBuilder.append("Confirmed: ").append(confirmed);
        StringBuilder recoveredBuilder = new StringBuilder();
        recoveredBuilder.append("Recovered: ").append(recovered);
        StringBuilder deathsBuilder = new StringBuilder();
        deathsBuilder.append("Deaths: ").append(deaths);

        binding.textViewConfirmed.setText(confirmedBuilder);
        binding.textViewRecovered.setText(recoveredBuilder);
        binding.textViewDeaths.setText(deathsBuilder);
    }

}