package com.example.coronatrackingapp.Models;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Country country);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Country> countries);

    @Update
    void update(Country country);

    @Delete
    void delete(Country country);

    @Query("DELETE FROM country")
    void deleteAllCountries();

    @Query("SELECT * FROM country where countryName is not null")
    LiveData<List<Country>> getAllNotes();

    @Query("SELECT * FROM country where isFavourite=1 and countryName is not null ORDER BY id DESC")
    LiveData<List<Country>> getAllFavourite();


}
