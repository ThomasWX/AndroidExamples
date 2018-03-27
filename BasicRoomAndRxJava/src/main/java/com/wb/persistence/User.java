package com.wb.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Immutable model class for a User
 */


@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "userId")
    private String mId;

    @ColumnInfo(name = "userName")
    private String mUserName;

    @Ignore
    public User(String userName) {
        mId = UUID.randomUUID().toString();
        mUserName = userName;
    }

    public User(String id,String userName){
        mId = id;
        mUserName = userName;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public String getUserName() {
        return mUserName;
    }
}
