package com.gome.mobile.frame.router;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.gome.mobile.frame.router.intf.NavigationCallback;
import com.gome.mobile.frame.router.utils.JSONUtil;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * 路由数据传递对象
 *
 * @author luciuszhang
 * @date 2018/05/09
 */
public class Postcard {


    private String mPath;
    private Bundle mBundle;
    /**
     * Activity Flags
     */
    private int mFlags = Integer.MAX_VALUE;
    private Bundle optionsCompat;
    private int enterAnim = -1;
    private int exitAnim = -1;
    private NavigationCallback mCallback;

    public Postcard(String path) {
        this.mPath = path;
        this.mBundle = new Bundle();
    }

    public String getPath() {
        return mPath;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public NavigationCallback getCallback() {
        return mCallback;
    }

    public Postcard withCallback(NavigationCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public void navigation(Activity activity) {
        navigation(activity, -1);
    }

    public void navigation(Activity activity, int requestCode) {
        GRouter.getInstance().navigation(activity, this,  requestCode);
    }

    public void navigation(Fragment fragment) {
        navigation(fragment, -1);
    }

    public void navigation(Fragment fragment, int requestCode) {
        GRouter.getInstance().navigation(fragment, this, requestCode);
    }

    /**
     * BE ATTENTION TO THIS METHOD WAS <P>SET, NOT ADD!</P>
     */
    public Postcard with(Bundle bundle) {
        mBundle = bundle;
        return this;
    }

    @IntDef({
            Intent.FLAG_ACTIVITY_SINGLE_TOP,
            Intent.FLAG_ACTIVITY_NEW_TASK,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            Intent.FLAG_DEBUG_LOG_RESOLUTION,
            Intent.FLAG_FROM_BACKGROUND,
            Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT,
            Intent.FLAG_ACTIVITY_CLEAR_TASK,
            Intent.FLAG_ACTIVITY_CLEAR_TOP,
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS,
            Intent.FLAG_ACTIVITY_FORWARD_RESULT,
            Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY,
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK,
            Intent.FLAG_ACTIVITY_NO_ANIMATION,
            Intent.FLAG_ACTIVITY_NO_USER_ACTION,
            Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT,
            Intent.FLAG_ACTIVITY_TASK_ON_HOME,
            Intent.FLAG_RECEIVER_REGISTERED_ONLY
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlagInt {
    }

    public Postcard withFlags(@FlagInt int flag) {
        this.mFlags = flag;
        return this;
    }

    public int getFlags() {
        return mFlags;
    }

    public Postcard withObject(@Nullable String key, @Nullable Object value) {
        mBundle.putString(key, JSONUtil.object2Json(value));
        return this;
    }

    public Postcard withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }

    public Postcard withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public Postcard withShort(@Nullable String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }

    public Postcard withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public Postcard withLong(@Nullable String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }

    public Postcard withDouble(@Nullable String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public Postcard withByte(@Nullable String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }

    public Postcard withChar(@Nullable String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }

    public Postcard withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public Postcard withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        mBundle.putCharSequence(key, value);
        return this;
    }

    public Postcard withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    public Postcard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public Postcard withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public Postcard withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        mBundle.putSparseParcelableArray(key, value);
        return this;
    }

    public Postcard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public Postcard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public Postcard withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        mBundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public Postcard withSerializable(@Nullable String key, @Nullable Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public Postcard withByteArray(@Nullable String key, @Nullable byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }

    public Postcard withShortArray(@Nullable String key, @Nullable short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }

    public Postcard withCharArray(@Nullable String key, @Nullable char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }

    public Postcard withFloatArray(@Nullable String key, @Nullable float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }

    public Postcard withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        mBundle.putCharSequenceArray(key, value);
        return this;
    }

    public Postcard withBundle(@Nullable String key, @Nullable Bundle value) {
        mBundle.putBundle(key, value);
        return this;
    }

    public Postcard withTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    @RequiresApi(16)
    public Postcard withOptionsCompat(ActivityOptionsCompat compat) {
        if (null != compat) {
            this.optionsCompat = compat.toBundle();
        }
        return this;
    }


}
