package com.support.floating;

/**
 * Created by wb on 18-3-21.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.support.design.R;

public class FloatingMainActivity extends AppCompatActivity {

    public static final String TAG = "FloatingABActivity";
    private boolean mLogShown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FloatingActionButtonBasicFragment fragment = new FloatingActionButtonBasicFragment();
            transaction.replace(R.id.sample_content_fragment,fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fb_main,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_toggle_log){
//            mLogShown = ! mLogShown;
//            ViewAnimator output = findViewById(R.id.sample_output);
//        }
        return super.onOptionsItemSelected(item);
    }


}
