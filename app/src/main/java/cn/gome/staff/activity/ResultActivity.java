package cn.gome.staff.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.gome.staff.R;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent data = new Intent();
        data.putExtra("from", "A string from ResultActivity");

        setResult(RESULT_OK, data);
        finish();
    }
}
