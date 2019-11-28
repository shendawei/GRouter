package com.gome.mobile.frame.router;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class RouteLayout extends RouteView {
    private static String TAG = "RouteLayout";

    @Nullable
    protected String conditionUri = null;

    @IdRes
    protected int slotId = 0;

    public RouteLayout(Context context) {
        super(context);
    }

    public RouteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RouteLayout);
        if (a.hasValue(R.styleable.RouteLayout_conditionUri)) {
            conditionUri = a.getString(R.styleable.RouteLayout_conditionUri);
        }
        if (a.hasValue(R.styleable.RouteLayout_slotId)) {
            slotId = a.getResourceId(R.styleable.RouteLayout_slotId, slotId);
        }
        a.recycle();
    }

    @Override
    protected void onStatusChange() {
        super.onStatusChange();

        if (getVisibility() != View.VISIBLE) {
            return;
        }

        if (getChildCount() > 2) {
            throw new RuntimeException();
        }

        if (conditionUri != null && !GRouter.getInstance().exists(conditionUri)) {
            setVisibility(absentVisibility);
        }
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

    @Nullable
    public String getConditionUri() {
        return conditionUri;
    }

    public void setConditionUri(@Nullable String conditionUri) {
        this.conditionUri = conditionUri;
        onStatusChange();
    }

    @Nullable
    public String getOnClickUri() {
        return onClickUri;
    }

    public void setOnClickUri(@Nullable String onClickUri) {
        this.onClickUri = onClickUri;
        onStatusChange();
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
        onStatusChange();
    }
}
