package com.example.mylistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class FlexibleListViewTest extends AppCompatActivity {

    private FlexibleListView mFlexibleListView;
    private String[] data = new String[30];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexible_list_view_test);
        for (int i = 0; i < 30; i++) {
            data[i] = "" + i;
        }
        mFlexibleListView = findViewById(R.id.flexible_listview);
        mFlexibleListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data));

    }
}
