package com.example.systemwidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class TopBarTest extends AppCompatActivity {

    private TopBar mTopbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bar_test);
        mTopbar = findViewById(R.id.topBar);

        mTopbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(TopBarTest.this,
                        "right", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void rightClick() {
                Toast.makeText(TopBarTest.this,
                        "left", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        // 控制topbar上组件的状态
        mTopbar.setButtonVisiable(0, true);
        mTopbar.setButtonVisiable(1, true);
    }
}
