package com.design.FloatingActionButton;

/**
 * Created by wb on 18-3-16.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 设计目的：We want to extend this behavior to signal that we wish to handle scroll events in the vertical direction:
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {
    
    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


    /**
     * Because scrolling will be handled by this class, a separate method onNestedScroll() will be called.
     * We can check the Y position and determine whether to animate in or out the button.
     */
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child,
                               @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if (dyConsumed >0 && child.getVisibility() == View.VISIBLE){
            child.hide();
        } else if (dyConsumed <0 && child.getVisibility() != View.VISIBLE){
            child.show();
        }

    }
}