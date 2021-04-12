package com.example.coronatrackingapp.Models;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CountryDao {

    @Insert
    void insert(Country country);

    @Update
    void update(Country country);

    @Delete
    void delete(Country country);

    @Query("DELETE FROM country_table")
    void deleteAllCountries();

    @Query("SELECT * FROM country_table ORDER BY id DESC")
    LiveData<List<Country>> getAllNotes();

}
