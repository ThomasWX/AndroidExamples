package com.runtime.permissions;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.runtime.permissions.camera.CameraPreviewFragment;
import com.runtime.permissions.contacts.ContactsFragment;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG = "MainActivity";
    // permission request id
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CONTACTS = 1;

    // Permissions
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

    // Root of the layout of this Activity.
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLayout = findViewById(R.id.sample_main_layout);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RuntimePermissionsFragment fragment = new RuntimePermissionsFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }


    /**
     * 按钮点击
     */
    public void showCamera(View view) {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        // BEGIN_INCLUDE(camera_permission)
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 当前没有相机权限
            requestCameraPermission();
        } else {
            // 有相机权限，展现相机预览
            Log.i(TAG, "CAMERA permission has already been granted. Displaying camera preview.");
            showCameraPreview();


        }
        // END_INCLUDE(camera_permission)
    }

    /**
     * 如果权限之前被拒绝，SnackBar会提示用户授予权限，否则直接请求。
     */
    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // 权限之前被拒绝，用SnackBar提示用户授予权限
            Snackbar.make(mLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {
            // 直接请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }


    private void showCameraPreview() {
        getSupportFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance()).addToBackStack("contacts").commit();

    }


    /**
     * 按钮点击
     */
    public void showContacts(View view) {
        Log.i(TAG, "Show contacts button pressed. Checking permissions.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED

                ) {
            Log.i(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
            requestContactsPermission();
        } else {
            Log.i(TAG,
                    "Contact permissions have already been granted. Displaying contact details.");
            showContactDetails();
        }
    }

    private void requestContactsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
            Log.i(TAG, "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, R.string.permission_contacts_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(MainActivity.this, PERMISSIONS_CONTACT,
                                            REQUEST_CONTACTS);
                        }
                    })
                    .show();

        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }

    }

    private void showContactDetails() {
        getSupportFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, ContactsFragment.newInstance()).addToBackStack("contacts").commit();
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == REQUEST_CAMERA) {
            Log.i(TAG, "Received response for Camera permission request.");
            // Check if the only required permission has been granted
            //检查是否授予了唯一所需的权限

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
                Snackbar.make(mLayout, R.string.permission_available_camera, Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "CAMERA permission was NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_CONTACTS) {
            Log.i(TAG, "Received response for contact permissions request.");
            // We have requested multiple permissions for contacts, so all of them need to be checked.
            // 我们已经要求多个权限的联系人，所以他们都需要检查。
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment. 所有的权限都被授予
                Snackbar.make(mLayout, R.string.permission_available_contacts, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onBackClick(View view) {
        getSupportFragmentManager().popBackStack();
    }
}
