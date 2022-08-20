package com.cambrian.cambrianfinalproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyContactDao {
    @Insert
    void insert(Contact model);

    @Delete
    void delete(Contact model);

    @Update
    void update(Contact model);

    @Query("DELETE FROM contact")
    void deleteAll();

    @Query("SELECT * from contact ORDER BY name ASC")
    LiveData<List<Contact>> getContacts();
}
