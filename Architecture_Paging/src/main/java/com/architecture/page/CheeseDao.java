package com.architecture.page;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Database Access Object for the Cheese database.
 */

@Dao
public interface CheeseDao {
    /*
    Room knows how to return a LivePagedListProvider, from which we can get a LiveData and serve
    it back to UI via ViewModel.

    @Query("SELECT * FROM Cheese ORDER BY name COLLATE NOCASE ASC")
    fun allCheesesByName(): DataSource.Factory<Int, Cheese>

    @Insert
    fun insert(cheeses: List<Cheese>)

    @Insert
    fun insert(cheese: Cheese)

    @Delete
    fun delete(cheese: Cheese)*/

    @Query("SELECT * FROM Cheese ORDER BY name COLLATE NOCASE ASC")
    public DataSource.Factory<Integer, Cheese> allCheesesByName();

    @Insert
    public void insert(List<Cheese> cheeses);

    @Insert
    public void insert(Cheese cheese);

    @Delete
    public void delete(Cheese cheese);
}
