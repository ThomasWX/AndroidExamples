package com.runtime.permissions;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wb on 18-2-7.
 */

public class RuntimePermissionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main,null);



        if (Build.VERSION.SDK_INT < 23) {
            // 联系人权限已经在AndroidManifest for Android M 及更高版本中声明。 它们在旧版平台上不可用，所以我们隐藏了访问联系人数据库的按钮。
            // 这显示了如何添加新的仅运行时权限，不适用于较旧的平台版本。 这对自动更新很有用，其中额外的权限可能会提示用户进行升级。
            root.findViewById(R.id.button_contacts).setVisibility(View.GONE);
        }


        return root;
    }
}
