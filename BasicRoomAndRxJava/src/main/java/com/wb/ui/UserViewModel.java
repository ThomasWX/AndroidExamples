package com.wb.ui;

import android.arch.lifecycle.ViewModel;

import com.wb.UserDataSource;
import com.wb.persistence.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * View Model for the {@link MainActivity}
 */

public class UserViewModel extends ViewModel {

    private final UserDataSource mDataSource;

    private User mUser;

    public UserViewModel(UserDataSource dataSource) {
        mDataSource = dataSource;
    }

    /**
     * Get the user name of the user
     *
     * @return a {@link Flowable} that will emit every time the user name has been updated.
     */

    public Flowable<String> getUserName() {
        return mDataSource.getUser().map(user -> { // for every emission（排放、发行） of the user, get the user name
            mUser = user;
            return user.getUserName();
        });
    }

    /**
     * Update the user name.
     *
     * @param userName the new user name
     * @return a {@link Completable} that completes when the user name is updated 当用户名更新时，Completable完成
     */
    public Completable updateUserName(final String userName) {
        return Completable.fromAction(() -> {


            // if there's no use, create a new user.
            // if we already have a user, then, since the user object is immutable,
            // create a new user, with the id of the previous user and the updated user name.


            mUser = mUser == null ? new User(userName) : new User(mUser.getId(), userName);
            mDataSource.insertOrUpdateUser(mUser);
        });
    }


}
