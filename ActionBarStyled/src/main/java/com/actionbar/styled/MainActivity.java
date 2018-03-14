package com.actionbar.styled;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Action Bar to use tabs for navigation
        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); // ActionBar已过时，应使用Toolbar

        // Add three tabs to the Action Bar for display
        ab.addTab(ab.newTab().setText("1").setTabListener(this));
        ab.addTab(ab.newTab().setText("2").setTabListener(this));
        ab.addTab(ab.newTab().setText("3").setTabListener(this));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // This is called when a tab is selected.
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // This is called when a previously selected tab is unselected.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // This is called when a previously selected tab is selected again.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 初始化菜单
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
