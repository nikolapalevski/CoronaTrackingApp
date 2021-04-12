package com.example.coronatrackingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.coronatrackingapp.Models.All;
import com.example.coronatrackingapp.Models.MyApi;
import com.example.coronatrackingapp.Models.SingletonRetrofit;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.databinding.ActivityMainBinding;
import com.example.coronatrackingapp.databinding.ActivitySingleCountryBinding;

import java.util.Map;

public class SingleCountry extends AppCompatActivity {
    ActivitySingleCountryBinding binding;
    private MyApi myApi;
    private String currCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleCountryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        currCountry = getIntent().getStringExtra("country");

        setupRetrofit();
        apiCallForCountry();
    }

    private void setupRetrofit() {
        myApi = SingletonRetrofit.getRetrofit().create(MyApi.class);
    }
    private void apiCallForCountry(){

        Call<Map<String, All>> call = myApi.getSpecificCountry(currCountry);
        call.enqueue(new Callback<Map<String, All>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, All>> call, @NonNull Response<Map<String, All>> response) {
                Map<String, All> mapRegions = response.body();
                StringBuilder finalResult = new StringBuilder(currCountry + "\n\n");
                for (String keys : mapRegions.keySet()) {

                    finalResult.append(keys)
                            .append("\nConfirmed: ").append(mapRegions.get(keys).getConfirmed())
                            .append("\nRecovered: ").append(mapRegions.get(keys).getRecovered())
                            .append("\nDeaths: ").append(mapRegions.get(keys).getDeaths()).append("\n\n");
                }
                binding.textViewInfo.setText(finalResult);
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, All>> call, @NonNull Throwable t) {
                Toast.makeText(SingleCountry.this, "Error loading!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}