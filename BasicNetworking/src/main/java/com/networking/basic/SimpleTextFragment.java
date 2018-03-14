package com.networking.basic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wb on 18-2-10.
 */

public class SimpleTextFragment extends Fragment {
    private static final String TAG = "SimpleTextFragment";
    // Fragment 中显示的字符串
    String mText;
    int mTextId = -1;
    // 用于在 setArguments中存储或取回 文本
    public static final String TEXT_KEY = "text";
    public static final String TEXT_ID_KEY = "text_id";
    // 为了在Runtime修改文本, exposing（暴露） the TextView.
    private TextView mTextView;

    public SimpleTextFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 在初始化textView之前，检查是否通过setArguments提供了任何参数。
        processArguments();

        // Create a new TextView and set its text to whatever was provided.
        mTextView = new TextView(getActivity());
        mTextView.setGravity(Gravity.CENTER);

        if (mText != null) {
            mTextView.setText(mText);
            Log.d(TAG, mText);
        }


        return mTextView;
    }


    public TextView getTextView() {
        return mTextView;
    }

    public void setText(int stringId) {
        // 原：  getTextView().setText(getActivity().getString(stringId));
        getTextView().setText(stringId);
    }


    private void processArguments() {
        if (getArguments() != null) {
            Bundle args = getArguments();
            if (args.containsKey(TEXT_KEY)) {
                mText = args.getString(TEXT_KEY);
                Log.d(TAG, "Constructor Added Text.");
            } else if (args.containsKey(TEXT_ID_KEY)) {
                mTextId = args.getInt(TEXT_ID_KEY);
                mText = getString(mTextId);
            }
        }

    }
}
