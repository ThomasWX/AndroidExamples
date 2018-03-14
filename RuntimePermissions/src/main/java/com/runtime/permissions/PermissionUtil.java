package com.runtime.permissions;

/**
 * Created by wb on 18-2-5.
 */

import android.content.pm.PackageManager;

/**
 * Utility class that wraps access to the runtime permissions API in M and provides basic helper
 * methods.
 */
public abstract class PermissionUtil {

    /**
     * 验证是否授予了所有给定的权限
     *
     * see... Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions/*verify:校验*/(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        // 校验每个已被授予的权限
        for (int result :
                grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
