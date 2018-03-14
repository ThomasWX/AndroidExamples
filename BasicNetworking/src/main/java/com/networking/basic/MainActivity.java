package com.networking.basic;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NetworkingBasic";
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;

    private View mLayout;

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    // private LogFragment mLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLayout = getLayoutInflater().inflate(R.layout.activity_main,null);
        setContentView(mLayout);

        // Initialize text fragment that displays intro text.
        SimpleTextFragment introFragment = (SimpleTextFragment)
                getSupportFragmentManager().findFragmentById(R.id.intro_fragment);
        introFragment.setText(R.string.intro_message);
        introFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks TEST, display the connection status.
            case R.id.test_action:
                checkNetworkConnection();
                return true;
            // Clear the log view fragment.
            case R.id.clear_action:
                // mLogFragment.getLogView().setText("");
                return true;
        }
        return false;
    }


    private static final int REQUEST_ACCESS_NETWORK_STATE = 0;


    private void checkNetworkConnection() {
        // 权限检查与申请
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限
            requestNetworkPermission();
        } else {
            // 有权限
            accessNetwork();
        }

    }

    private void requestNetworkPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            // 如果权限之前被拒绝，用SnackBar提示用户授予权限
            Snackbar.make(mLayout, "Access Network State Permission is Needed.", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                    Manifest.permission.ACCESS_NETWORK_STATE
                            }, REQUEST_ACCESS_NETWORK_STATE);
                        }
                    }).show();
        } else {
            // 直接请求权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, REQUEST_ACCESS_NETWORK_STATE);
        }
    }

    private void accessNetwork() {
        // BEGIN_INCLUDE(connect)
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                Snackbar.make(mLayout,R.string.wifi_connection,Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, getString(R.string.wifi_connection));
            } else if (mobileConnected) {
                Snackbar.make(mLayout,R.string.mobile_connection,Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, getString(R.string.mobile_connection));
            }
        } else {
            Snackbar.make(mLayout,R.string.no_wifi_or_mobile,Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, getString(R.string.no_wifi_or_mobile));
        }
        // END_INCLUDE(connect)
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_ACCESS_NETWORK_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessNetwork();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
