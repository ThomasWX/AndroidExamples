package com.runtime.permissions.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.runtime.permissions.R;


/**
 * Created by wb on 18-2-5.
 */

public class CameraPreviewFragment extends Fragment {

    private static final String TAG = "CameraPreviewFragment";

    /**
     * Id of the camera to access.
     * 0 is the first camera.
     */
    private static final int CAMERA_ID = 0;

    private CameraPreview mPreview;
    private Camera mCamera;

    public static CameraPreviewFragment newInstance() {
        return new CameraPreviewFragment();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    @Nullable
    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist) 如果是被占用，可使用重试
            Log.d(TAG, "Camera " + cameraId + " is not available: " + e.getMessage());
        }
        return c;// returns null if camera is unavailable
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Open an instance of the first camera and retrieve(取回、恢复) its info.
        mCamera = getCameraInstance(CAMERA_ID);
        Camera.CameraInfo cameraInfo = null;
        if (mCamera != null) {
            // Get camera info only if the camera is available
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }
        if (mCamera == null || cameraInfo == null) {
            // Camera is not available, display error message
            Toast.makeText(getActivity(), "Camera is not available.", Toast.LENGTH_SHORT).show();
            return inflater.inflate(R.layout.fragment_camera_unavailable, null);
        }

        View root = inflater.inflate(R.layout.fragment_camera, null);
        // Get the rotation of the screen to adjust the preview image accordingly.
        // 获取屏幕的方向以调整预览图像。
        final int displayRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();

        // Create the Preview view and set it as the content of this Activity.
        mPreview = new CameraPreview(getActivity(), mCamera, cameraInfo, displayRotation);
        FrameLayout preview = root.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop the camera access
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }
}
