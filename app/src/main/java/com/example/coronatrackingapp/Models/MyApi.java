package com.example.coronatrackingapp.Models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApi {

//    @GET("/v1/cases?country=Belgium")
//    Call<Countries> getData();
//
//    @GET("/v1/cases?country=")
//    Call<Countries> getCountry(@Query("country") String country);

    @GET("/v1/cases?country=")
    Call<Map<String, Country>> getSpecificCountry(@Query("country") String country);

    @GET("/v1/cases")
    Call<Map<String, Country>> getAllCountries();

    @GET("/v1/cases")
    Call<Map<String,Map<String, Country>>> getAllCountriesAndRegions();
}
