package cn.gome.staff.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gome.mobile.frame.router.GRouter;

import cn.gome.staff.R;
import cn.gome.staff.activity.TestFragmentActivity2Activity;

/**
 * Created by luciuszhang on 18-11-19.
 */

public class Test1Fragment extends Fragment {

    private static final String TAG = "Test1Fragment";

    private TextView mStartJump;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test1_layout, container, false);
        mStartJump = (TextView) view.findViewById(R.id.tv_fragment_name);
        mStartJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRouter.getInstance().build("/test2").navigation(Test1Fragment.this, 9000);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Test1Fragment requestCode = " + requestCode + "; resultCode = " + resultCode);
    }
}
