package com.tech.integer.testkt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gome.mobile.frame.router.annotation.IActivity


@IActivity(value = "/test/ktx")
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testkt_activity_test)
    }
}
