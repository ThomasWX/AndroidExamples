package com.transition.basic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by 王斌 on 2018/3/11.
 */

public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs,
                             int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    // 重写onMeasure 方法

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     *
     *
     * 1. onMeasure什么时候会被调用
     *   onMeasure方法的作用时测量控件的大小。
     *  创建一个View(执行构造方法)的时候不需要测量控件的大小，只有将这个view放入一个容器（父控件）中的时候，才需要测量：
     *
     *  父控件即会调用子控件的onMeasure(widthMeasureSpec,heightMeasureSpec) ,让子控件自测。
     *  (这两个参数就是父控件告诉子控件可获得的空间以及关于这个空间的约束条件。)
     *
     * 2. onMeasure方法执行流程
     *   ViewGroup提供了三个测量子控件相关的方法(measuireChildren\measuireChild\measureChildWithMargins)，只是在ViewGroup中没有调用它们，所以它本身不具备为子控件测量大小的能力。
     *  测量的时候父控件的onMeasure方法会遍历他所有的子控件，挨个调用子控件的measure方法，measure方法会调用onMeasure，然后会调用setMeasureDimension方法保存测量的大小，
     * 一次遍历下来，第一个子控件以及这个子控件中的所有子控件都会完成测量工作；然后开始测量第二个子控件…；最后父控件所有的子控件都完成测量以后会调用setMeasureDimension方法保存自己的测量大小。
     * 值得注意的是，这个过程不只执行一次，也就是说有可能重复执行，因为有的时候，一轮测量下来，父控件发现某一个子控件的尺寸不符合要求，就会重新测量一遍。
     *
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSize == 0 && heightSize == 0) {
            // If there are no constraints on size, let FrameLayout measure 如果对大小没有限制，则让FrameLayout进行度量
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Now use the smallest of the measured dimensions for both dimensions 使用当前宽高的较小值
            final int minSize = Math.max(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(minSize, minSize);
            return;
        }

        final int size;
        if (widthSize == 0 || heightSize == 0) {
            // If one of the dimensions has no restriction on size, set both dimensions to be the on that does
            // 如果其中一个维度对大小没有限制，则将这两个维度都设置为该维度
            size = Math.max(widthSize, heightSize);
        } else {
            // Both dimensions have restrictions on size, set both dimensions to be the smallest of the two
            // 两个尺寸对尺寸都有限制，将两个尺寸设置为两者中最小的尺寸
            size = Math.min(widthSize, heightSize);
        }

        final int newMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(newMeasureSpec, newMeasureSpec);
    }
}
