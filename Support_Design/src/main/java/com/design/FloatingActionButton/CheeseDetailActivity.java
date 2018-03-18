package com.design.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.support.design.R;

/**
 * Created by wb on 18-3-16.
 */

public class CheeseDetailActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "cheese_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_activity_detail);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        /*
         TODO Toolbar Demo
         */
        // [Toolbar的使用方式示范 BEGIN]
        final Toolbar toolbar = findViewById(R.id.detail_collapsing_toolbar);
        setSupportActionBar(toolbar);
        // 设置返回按钮 Display Home As Up
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 设置标题
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(cheeseName);
        // [Toolbar的使用方式示范 END]

        // 显示大头像
        loadBackdrop();
    }

    private void loadBackdrop() {
        final ImageView imageView = findViewById(R.id.detail_backdrop);
        // 原Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
        imageView.setImageResource(Cheeses.getRandomCheeseDrawable());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.floating_sample_actions, menu);
        return true;
    }
}
