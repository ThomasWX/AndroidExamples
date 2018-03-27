package com.wb;

import android.content.Context;

import com.wb.persistence.LocalUserDataSource;
import com.wb.persistence.UsersDatabase;
import com.wb.ui.ViewModelFactory;

/**
 * Enables injection of data sources.
 */

public class Injection {

    public static UserDataSource provideUserDataSource(Context context){
        UsersDatabase database = UsersDatabase.getInstance(context);
        return new LocalUserDataSource(database.userDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        UserDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
