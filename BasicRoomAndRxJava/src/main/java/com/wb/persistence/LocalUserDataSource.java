package com.wb.persistence;

/**
 * Created by wb on 18-3-27.
 */

import com.wb.UserDataSource;

import io.reactivex.Flowable;

/**
 * Using the Room database as a data source.
 */
public class LocalUserDataSource implements UserDataSource {
    private final UserDao mUserDao;

    public LocalUserDataSource(UserDao userDao) {
        mUserDao = userDao;
    }


    @Override
    public Flowable<User> getUser() {
        return mUserDao.getUser();
    }

    @Override
    public void insertOrUpdateUser(User user) {
        mUserDao.insertUser(user);
    }

    @Override
    public void deleteAllUsers() {
        mUserDao.deleteAllUsers();
    }
}
