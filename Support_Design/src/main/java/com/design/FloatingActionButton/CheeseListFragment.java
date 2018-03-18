package com.design.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.support.design.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wb on 18-3-16.
 */

public class CheeseListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.floating_fragment_cheese_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(getActivity(), getRandomSublist(Cheeses.sCheeseStrings, 30)));

        return view;
    }

    /**
     * @param amount 中文意思为"量"
     */
    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]); // 从数组中随机取amount个元素加入集合中
        }
        return list;
    }

    public static class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder> {
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                mImageView = itemView.findViewById(R.id.avatar);
                mTextView = itemView.findViewById(android.R.id.text1);
            }


            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleRecyclerViewAdapter(Context context, List<String> items) {
            // selectableItemBackground为app compat 中的主题
            // TODO 理解resolveAttribute的使用
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.floating_list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CheeseDetailActivity.class);
                    intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
                    if (intent.resolveActivity(context.getPackageManager()) != null) { // 自己加的
                        context.startActivity(intent);
                    }
                }
            });

            // 为Holder中的ImageView设置图片
            // Glide.with(holder.mImageView.getContext()).load(Cheeses.getRandomCheeseDrawable()).fitCenter().into(holder.mImageView);
            // 由于是显示本地的图片，考虑替代Glide
            holder.mImageView.setImageResource(Cheeses.getRandomCheeseDrawable());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
