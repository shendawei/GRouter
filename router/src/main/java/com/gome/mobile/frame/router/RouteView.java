package com.gome.mobile.frame.router;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by chenbaocheng on 16/11/24.
 * Updated by chenbaocheng on 19/11/20
 */

public class RouteView extends FrameLayout {
    private static String TAG = "RouteView";

    private Uri uri;
    private int absentVisibility = View.GONE;

    public RouteView(Context context) {
        super(context);
    }

    public RouteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        String uriString = null;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RouteView);
        if (a.hasValue(R.styleable.RouteView_routeUri)) {
            uriString = a.getString(R.styleable.RouteView_routeUri);
        }
        if (a.hasValue(R.styleable.RouteView_absentVisibility)) {
            absentVisibility = a.getInt(R.styleable.RouteView_absentVisibility, View.GONE);
        }
        a.recycle();

        if (uriString != null) {
            setRouteUri(uriString);
        }
    }

    public void setRouteUri(Uri uri) {
        this.uri = uri;
    }

    public void setRouteUri(String uriString) {
        setRouteUri(Uri.parse(uriString));
    }

    public Uri getRouteUri() {
        return uri;
    }

    public void setAbsentVisibility(int absentVisibility) {
        this.absentVisibility = absentVisibility;
        routeContentView();
    }

    public int getAbsentVisibility() {
        return absentVisibility;
    }

    private void routeContentView() {
        if (uri == null) {
            return;
        }

        Object value = GRouter.getInstance()
                .build(uri)
                .withMethod(RequestMethod.Get)
                .request(this, null);
        if (value instanceof View) {
            Log.d(TAG, "Route success with " + value.toString());
            removeAllViews();
            addView((View) value);
        } else {
            Log.d(TAG, "Route failed.");
            setVisibility(absentVisibility);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        routeContentView();
        GRouter.getInstance().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        GRouter.getInstance().unregister(this);
    }
}
