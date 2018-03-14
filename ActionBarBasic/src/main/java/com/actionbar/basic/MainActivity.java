package com.actionbar.basic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // BEGIN_INCLUDE(create_menu)

    /**
     * 如果已经添加了菜单并且想要显示菜单，您应该返回true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // It is also possible add items here. Use a generated id from
        // resources (ids.xml) to ensure that all menu ids are distinct.
        // 译文：这里也可以添加项目。 使用生成的ID资源（ids.xml）以确保所有菜单id不同。
        MenuItem locationItem = menu.add(0, R.id.menu_location, 0, R.string.menu_location);
        locationItem.setIcon(R.drawable.ic_action_location);

        // 原： MenuItemCompat.setShowAsAction(locationItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        // Need to use MenuItemCompat methods to call any action item related methods 需要使用MenuItemCompat方法来调用任何操作项相关的方法
        locationItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    // END_INCLUDE(create_menu)

    // BEGIN_INCLUDE(menu_item_selected)

    /**
     * 如果你处理选择，您应该返回true.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_location:
                // Here we might call LocationManager.requestLocationUpdates()
                return true;
            case R.id.menu_refresh:
                // Here we might start a background refresh task
                return true;
            case R.id.menu_settings:
                // Here we would open up our settings activity
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // END_INCLUDE(menu_item_selected)
}
