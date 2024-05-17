package com.yangyang.mobile.test.plugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(), OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*findViewById<Button>(R.id.btn_test).setOnClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_LONG).show()
        }*/

        findViewById<Button>(R.id.btn_test).setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_test -> {
                Toast.makeText(this, "test", Toast.LENGTH_LONG).show()
            }
        }
    }
}