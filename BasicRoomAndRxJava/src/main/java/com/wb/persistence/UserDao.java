package com.wb.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;

/**
 * Created by wb on 18-3-27.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    Flowable<User> getUser();

    /**
     *  Insert a user in the database. If the user already exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("DELETE FROM users")
//    @Delete("DELETE FROM users")
    void deleteAllUsers();
}
