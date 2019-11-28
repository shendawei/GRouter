package com.gome.mobile.frame.router;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

/**
 * Created by chenbaocheng on 16/11/24.
 * Updated by chenbaocheng on 19/11/20
 */
public class RouteView extends FrameLayout implements OnClickListener {
    private static String TAG = "RouteView";

    @Nullable
    private OnClickListener onClickListener = null;

    @Nullable
    protected String onClickUri = null;

    @Nullable
    protected String routeUri;

    protected int absentVisibility = View.GONE;

    public RouteView(Context context) {
        super(context);
    }

    public RouteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RouteView);
        if (a.hasValue(R.styleable.RouteView_onClickUri)) {
            onClickUri = a.getString(R.styleable.RouteView_onClickUri);
        }
        if (a.hasValue(R.styleable.RouteView_routeUri)) {
            routeUri = a.getString(R.styleable.RouteView_routeUri);
        }
        if (a.hasValue(R.styleable.RouteView_absentVisibility)) {
            absentVisibility = a.getInt(R.styleable.RouteView_absentVisibility, View.GONE);
        }
        a.recycle();
    }

    protected void onStatusChange() {
        if (getVisibility() != View.VISIBLE) {
            return;
        }

        if (routeUri == null) {
            return;
        }

        if (onClickUri != null) {
            super.setOnClickListener(this);
        }

        Object value = GRouter.getInstance()
                .build(routeUri)
                .withMethod(RequestMethod.Get)
                .request(this, null);

        onRouteComplete(value);
    }

    protected void onRouteComplete(Object value) {
        if (value instanceof View) {
            onRouteSuccess((View) value);
        } else {
            onRouteError(value);
        }
    }

    protected void onRouteSuccess(View view) {
        Log.d(TAG, "Route success with " + view.toString());
        removeAllViews();
        addView(view);
    }

    protected void onRouteError(Object value) {
        Log.d(TAG, "Route failed.");
        setVisibility(absentVisibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onStatusChange();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onClickUri != null) {
            GRouter.getInstance().build(onClickUri).request(v);
        }

        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    public void setRouteUri(@Nullable String uri) {
        this.routeUri = uri;
        onStatusChange();
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setAbsentVisibility(int absentVisibility) {
        this.absentVisibility = absentVisibility;
        onStatusChange();
    }

    public int getAbsentVisibility() {
        return absentVisibility;
    }
}
