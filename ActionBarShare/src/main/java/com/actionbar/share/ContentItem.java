package com.actionbar.share;

/**
 * Created by wb on 18-2-27.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * This class encapsulates a content item. Referencing the content's type, and the differing way
 * to reference the content (asset URI or resource id).
 * <p>
 * 这个类封装了一个内容项。（资源URI或资源ID）。
 */

public class ContentItem {
    // Used to signify（表示） an image content type
    public static final int CONTENT_TYPE_IMAGE = 0;
    // Used to signify a text/string content type
    public static final int CONTENT_TYPE_TEXT = 1;

    public final int contentType;
    public final int contentResourceId;
    public final String contentAssetFilePath;

    public ContentItem(int type, int resId) {
        contentType = type;
        contentResourceId = resId;
        contentAssetFilePath = null;
    }

    public ContentItem(int type, String path) {
        contentType = type;
        contentAssetFilePath = path;
        contentResourceId = 0;
    }

    public Uri getContentUri() {
        if (!TextUtils.isEmpty(contentAssetFilePath)) {
            // If this content has an asset, then return a AssetProvider Uri
            return Uri.parse("content://" + AssetProvider.CONTENT_URI + "/" + contentAssetFilePath);
        } else {
            return null;
        }
    }

    public Intent getShareIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        switch (contentType) {
            case CONTENT_TYPE_IMAGE:
                intent.setType("image/jpg");
                // Bundle the asset content uri as the EXTRA_STREAM uri
                intent.putExtra(Intent.EXTRA_STREAM, getContentUri());
                break;
            case CONTENT_TYPE_TEXT:
                intent.setType("text/plain");
                // Get the string resource and bundle it as an intent extra
                intent.putExtra(Intent.EXTRA_TEXT, context.getString(contentResourceId));
                break;
        }

        return intent;
    }
}
