package com.runtime.permissions.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by wb on 18-2-5.
 * <p>
 * Using deprecated android.hardware.Camera in order to support {14 < API < 21}.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private int mDisplayOrientation;

    public CameraPreview(Context context) {
        this(context, null, null, 0);
    }

    public CameraPreview(Context context, Camera camera, Camera.CameraInfo cameraInfo, int displayOrientation) {
        super(context);
        // Do not initialise if no camera has been set
        // 如果没有设置相机，则不要初始化
        if (camera == null || cameraInfo == null) {
            return;
        }

        this.mCamera = camera;
        this.mCameraInfo = cameraInfo;
        this.mDisplayOrientation = displayOrientation;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.

        //安装一个SurfaceHolder.Callback，以便在底层surface被创建和销毁时得到通知。

        mHolder = getHolder();
        mHolder.addCallback(this);

    }

    /**
     * 计算屏幕上显示的预览(即preview)的正确方向。
     * 基于 Camera#setDisplayOrientation(int)中的示例代码
     */
    public static int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;//求余数
            result = (360 - result) % 360; // compensate（补偿） the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Surface已经被创建，现在告诉相机在哪里绘制预览。
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            Log.d(TAG, "Camera preview started.");
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        //如果您的预览可以更改或旋转，请在这里处理这些事件。
        //确保在调整大小或重新格式化之前停止预览。

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            Log.d(TAG, "Preview surface does not exist");
            return;
        }

        // stop preview before making changes

        try {
            mCamera.stopPreview();
            Log.d(TAG, "Preview stopped.");
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            // 忽略：试图阻止不存在的预览
            Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
        }

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            Log.d(TAG, "Camera preview started.");
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        // 注意在您的活动中释放相机预览。
    }
}
