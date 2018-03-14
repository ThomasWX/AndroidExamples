package com.transition.basic;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by 王斌 on 2018/3/11.
 */

public class DetailActivity extends Activity {
    // Extra name for the ID parameter
    public static final String EXTRA_PARAM_ID = "detail:_id";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;
    private Item mItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        mItem = Item.getItem(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

        mHeaderImageView = findViewById(R.id.image_view_header);
        mHeaderTitle = findViewById(R.id.text_view_title);

        // BEGIN_INCLUDE(detail_set_view_name)
        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         * 使用上面的静态值设置将要转换到的视图的名称。 这可以在布局XML中完成，但通过静态变量公开它可以轻松地从其他活动查询
         */
        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);

        // END_INCLUDE(detail_set_view_name)
        loadItem();
    }

    private void loadItem() {
        // Set the title TextView to the item's name and author
        mHeaderTitle.setText(getString(R.string.image_header, mItem.getName(), mItem.getAuthor()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()){
            // If we're running on Lollipop and we have added a listener to the shared element transition, load the thumbnail.
            // The listener will load the full-size image when the transition is complete.
            //如果我们在Lollipop上运行，并且已经为共享元素过渡添加了一个侦听器，请加载缩略图。
            // 当过渡完成时，侦听器将加载全尺寸图像。
            loadThumbnail();
        } else {
            // If all other cases we should just load the full-size image now
            //如果所有其他情况，我们现在只需加载全尺寸图像
            loadFullSizeImage();
        }
    }

    private void loadThumbnail(){
        Picasso.with(mHeaderImageView.getContext())
                .load(mItem.getThumbnailUrl())
                .noFade()
                .into(mHeaderImageView);
    }
    private void loadFullSizeImage(){
        Picasso.with(mHeaderImageView.getContext())
                .load(mItem.getPhotoUrl())
                .noFade()
                .noPlaceholder()
                .into(mHeaderImageView);
    }

    /**
     * Try and add a {@link Transition.TransitionListener} to the entering shared element {@link Transition}.
     * We do this so that we can load the full-size image after the transition has completed.
     * @return
     */
    private boolean addTransitionListener(){
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null){
            // There is an entering shared element transition so add a listener to it
            // //有一个进入的共享元素过渡，所以添加一个监听器
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    // 随着过渡动画结束，我们便加载全尺寸图片
                    loadFullSizeImage();

                    // Make sure we remove ourselves as a listener 确保移除了自己(观察者)
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener 确保移除了自己(观察者)
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
            return true;
        }
        // If we reach here then we have not added a listener
        // 如果我们到达这里，我们没有添加一个监听器
        return false;
    }
}
