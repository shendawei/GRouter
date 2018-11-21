package cn.gome.staff.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gome.mobile.frame.router.annotation.IFragment;

import cn.gome.staff.R;

/**wo sh da sha bi jiu shi zhe me zhuaijis
 * Created by luciuszhang on 18-11-19.
 */

public class Test2Fragment extends Fragment  {

    private TextView mSetResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test2_layout, container, false);
        mSetResult = (TextView) view.findViewById(R.id.tv_fragment_name);
        mSetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(3000);
                getActivity().finish();
            }
        });
        return view;
    }

}
