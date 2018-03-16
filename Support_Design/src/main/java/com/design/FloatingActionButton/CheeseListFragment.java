package com.design.FloatingActionButton;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.support.design.R;

/**
 * Created by wb on 18-3-16.
 */

public class CheeseListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheese_list,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter());

        return view;
    }

    public static class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder>{
        public static class ViewHolder extends RecyclerView.ViewHolder{
            public String mBoundString;
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mImageView = itemView.findViewById(R.id.avatar);
            }
        }
    }
}
