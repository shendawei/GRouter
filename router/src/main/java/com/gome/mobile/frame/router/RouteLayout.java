package com.gome.mobile.frame.router;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class RouteLayout extends RouteView implements OnClickListener {
    private static String TAG = "RouteLayout";

    @Nullable
    private OnClickListener onClickListener = null;

    @Nullable
    private String onClickUri = null;

    @IdRes
    private int slotId = 0;

    public RouteLayout(Context context) {
        super(context);
    }

    public RouteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RouteLayout);
        if (a.hasValue(R.styleable.RouteLayout_onClickUri)) {
            onClickUri = a.getString(R.styleable.RouteLayout_onClickUri);
        }
        if (a.hasValue(R.styleable.RouteLayout_slotId)) {
            slotId = a.getResourceId(R.styleable.RouteLayout_slotId, slotId);
        }
        a.recycle();
    }

    @Override
    protected void onRouteSuccess(View value) {
        if (slotId == 0) {
            return;
        }

        View slotView = findViewById(slotId);
        if (slotView instanceof ViewGroup) {
            ((ViewGroup) slotView).addView(value);
        } else {
            throw new RuntimeException("Attribute 'slotId' MUST reference a ViewGroup");
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (onClickUri != null) {
            super.setOnClickListener(this);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        GRouter.getInstance().build(onClickUri).request(v);

        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }
}
