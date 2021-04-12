package com.example.coronatrackingapp.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "country_table")
public class Country {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String country;


    public Country(String country) {
        this.country = country;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }
}
