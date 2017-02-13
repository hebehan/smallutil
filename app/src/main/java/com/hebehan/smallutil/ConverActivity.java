package com.hebehan.smallutil;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConverActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText addressET;
    private Button rtmp;
    private Button flv;
    private Button m3u8;
    private Button cleanpaste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conver);
        addressET = (EditText) findViewById(R.id.converaddress_et);
        rtmp = (Button) findViewById(R.id.rtmp);
        m3u8 = (Button) findViewById(R.id.m3u8);
        flv = (Button) findViewById(R.id.flv);
        cleanpaste = (Button) findViewById(R.id.cleanpaste);
        rtmp.setOnClickListener(this);
        m3u8.setOnClickListener(this);
        flv.setOnClickListener(this);
        cleanpaste.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cleanpaste){
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            addressET.setText(cm.getText().toString().trim());
        }else {
            getAddress(((Button)view).getText().toString().trim(),addressET.getText().toString().trim());
        }
    }

    public void getAddress(String type,String address){
        String converaddress = null;
        if (address.isEmpty()){
            Toast.makeText(this,"地址为空，转换失败",Toast.LENGTH_SHORT).show();
            return;
        }else {
            try {
                converaddress=address.split("//")[1];
                if (address.endsWith("m3u8")||address.endsWith("flv")){
                    converaddress=converaddress.substring(0,converaddress.lastIndexOf("."));
                }
            }catch (Exception e){
                Toast.makeText(this,"地址不合法，转换失败",Toast.LENGTH_SHORT).show();
                return;
            }

        }
        switch (type){
            case "rtmp":
                converaddress="rtmp://"+converaddress;
                break;
            case "m3u8":
                converaddress="http://"+converaddress+".m3u8";
                break;
            case "flv":
                converaddress="http://"+converaddress+".flv";
                break;

        }

        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(converaddress);
        Toast.makeText(this,"已将地址复制到剪切板",Toast.LENGTH_SHORT).show();
        addressET.setText(converaddress);
    }
}
