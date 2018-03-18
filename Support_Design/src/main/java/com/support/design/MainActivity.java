package com.support.design;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.design.FloatingActionButton.FloatingMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFeatureClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.floating_button:
                intent = new Intent(this, FloatingMainActivity.class);
                break;
        }

        if (intent != null && intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
