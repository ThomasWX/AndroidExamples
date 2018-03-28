package com.wb.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wb.Injection;
import com.wb.persistence.R;

import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mUserName;

    private EditText mUserNameInput;

    private Button mUpdateButton;

    private ViewModelFactory mViewModelFactory;

    private UserViewModel mViewModel;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserName = findViewById(R.id.user_name);
        mUserNameInput = findViewById(R.id.user_name_input);
        mUpdateButton = findViewById(R.id.update_user);

        mViewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(UserViewModel.class);
        mUpdateButton.setOnClickListener(v -> updateUserName());
    }

    private void updateUserName() {
        String userName = mUserNameInput.getText().toString();
        // Disable the update button until the user name update has been done
        mUpdateButton.setEnabled(false);
        mDisposable.add(mViewModel.updateUserName(userName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(() ->
                                mUpdateButton.setEnabled(true),
                        throwable -> Log.e(TAG, "Unable to update userName", throwable)
                ));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Subscribe to the emissions of the user name from the view model.
        // Update the user name text view, at every onNext emission.
        // In case of error, log the exception.

        mDisposable.add(mViewModel.getUserName().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(userName -> mUserName.setText(userName), throwable -> Log.e(TAG, "Unable to update userName", throwable))
        );
    }

    @Override
    protected void onStop() {

        super.onStop();
        // clear all the subscriptions
        mDisposable.clear();
    }
}
