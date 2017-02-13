package com.hebehan.smallutil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView funclist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        funclist = (ListView) findViewById(R.id.funclist);
        funclist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[]{"APP列表","地址转换"}));
        funclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(MainActivity.this,AppListActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,ConverActivity.class));
                        break;
                }
            }
        });
    }
}
