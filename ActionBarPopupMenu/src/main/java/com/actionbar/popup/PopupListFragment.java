package com.actionbar.popup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by wb on 18-2-26.
 */

/**
 * This ListFragment displays a list of cheeses, with a clickable view on each item whichs displays
 * a {@link android.support.v7.widget.PopupMenu PopupMenu} when clicked, allowing the user to
 * remove the item from the list.
 */
public class PopupListFragment extends ListFragment implements View.OnClickListener {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        // We want to allow modifications to the list so copy the dummy data array into an ArrayList
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < Cheeses.CHEESES.length; i++) {
            list.add(Cheeses.CHEESES[i]);
        }

        // Set the ListAdapter
        setListAdapter(new PopupAdapter(list));

        super.onActivityCreated(savedInstanceState);
    }

    // Item 上的菜单被点击
    @Override
    public void onClick(final View v) {
        // We need to post a Runnable to show the popup to make sure that the PopupMenu is
        // correctly positioned. The reason being that the view may change position before the
        // PopupMenu is shown.
        v.post(new Runnable() {
            @Override
            public void run() {
                showPopupMenu(v);
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String item = (String) l.getItemAtPosition(position);
        Toast.makeText(getActivity(), "点击：" + item, Toast.LENGTH_SHORT).show();
    }

    /**
     * A simple array adapter that creates a list of cheeses.
     */

    class PopupAdapter extends ArrayAdapter<String> {
        PopupAdapter(ArrayList<String> items) {
            super(getActivity(), R.layout.list_item, android.R.id.text1, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Let ArrayAdapter inflate the layout and set the text
            View view = super.getView(position, convertView, parent);

            // BEGIN_INCLUDE(button_popup)
            View popupBtn = view.findViewById(R.id.button_popup);
            popupBtn.setTag(getItem(position)); // 绑定 内容String
            popupBtn.setOnClickListener(PopupListFragment.this);
            // END_INCLUDE(button_popup)

            // Finally return the view to be displayed
            return view;
        }
    }

    private void showPopupMenu(View v) {
        final PopupAdapter adapter = (PopupAdapter) getListAdapter();

        // 解绑数据
        final String item = (String) v.getTag();

        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(getActivity(), v);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_remove:
                        // Remove the item from the adapter
                        adapter.remove(item);
                        return true;
                }

                return false;
            }
        });

        // Finally show the PopupMenu
        popup.show();
    }

}
