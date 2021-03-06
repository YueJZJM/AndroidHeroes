package com.example.systemwidget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MyViewTest extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = getIntent().getIntExtra("flag",-1);
        switch (flag) {
            case 0:
                setContentView(R.layout.teaching);
                break;
            case 1:
                setContentView(R.layout.my_textview);
                break;
            case 2:
                setContentView(R.layout.shine_textview);
                break;
            case 3:
                setContentView(R.layout.circle_progress);
                CircleProgressView circle = (CircleProgressView) findViewById(R.id.circle);
                circle.setSweepValue(0);
                break;
            case 4:
                setContentView(R.layout.volume);
                break;
            case 5:
                setContentView(R.layout.my_scrollview);
                break;
        }
    }
}
