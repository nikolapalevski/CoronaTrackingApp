package com.example.coronatrackingapp.Models;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingletonRetrofit {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://covid-api.mmediagroup.fr/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
