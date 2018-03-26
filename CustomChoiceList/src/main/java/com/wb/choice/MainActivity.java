package com.wb.choice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView textView = findViewById(R.id.intro);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(R.string.introduction);
            }
        });

        /*
            如果当前Activity extends ListActivity, 则直接 setListAdapter(new MyAdapter()); 而不需下面代码
         */
        ListView listView = findViewById(android.R.id.list);
        listView.setAdapter(new MyAdapter());
    }




    /**
     * A simple array adapter that creates a list of cheeses.
     */
    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Cheeses.CHEESES.length;
        }

        @Override
        public String getItem(int position) {
            return Cheeses.CHEESES[position];
        }

        @Override
        public long getItemId(int position) {
            return Cheeses.CHEESES[position].hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }

            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(getItem(position));
            return convertView;
        }
    }
}
