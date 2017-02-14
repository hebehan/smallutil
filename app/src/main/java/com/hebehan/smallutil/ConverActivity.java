package com.hebehan.smallutil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

public class ConverActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText addressET;
    private Button rtmp;
    private Button flv;
    private Button m3u8;
    private Button cleanpaste;
    private Button startscan;
    private CheckBox autoopen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conver);
        addressET = (EditText) findViewById(R.id.converaddress_et);
        rtmp = (Button) findViewById(R.id.rtmp);
        m3u8 = (Button) findViewById(R.id.m3u8);
        flv = (Button) findViewById(R.id.flv);
        cleanpaste = (Button) findViewById(R.id.cleanpaste);
        startscan = (Button) findViewById(R.id.startscan);
        autoopen = (CheckBox) findViewById(R.id.autoopen);
        rtmp.setOnClickListener(this);
        m3u8.setOnClickListener(this);
        flv.setOnClickListener(this);
        cleanpaste.setOnClickListener(this);
        startscan.setOnClickListener(this);
        autoopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cleanpaste:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                try {
                    addressET.setText(cm.getText().toString().trim());
                    addressET.setSelection(cm.getText().toString().trim().length());
                }catch (Exception e){
                    showmsg("剪切板为空");
                }
                break;
            case R.id.startscan:
                Intent intent = new Intent(ConverActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            default:
                getAddress(((Button)view).getText().toString().trim(),addressET.getText().toString().trim());
                break;
        }
    }

    public void getAddress(String type,String address){
        String converaddress = null;
        if (address.isEmpty()){
            showmsg("地址为空，转换失败");
            return;
        }else {
            try {
                converaddress=address.split("//")[1];
                if (address.endsWith("m3u8")||address.endsWith("flv")){
                    converaddress=converaddress.substring(0,converaddress.lastIndexOf("."));
                }
            }catch (Exception e){
                showmsg("地址不合法，转换失败");
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

        setText(converaddress);
    }

    public void showmsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    showmsg("解析二维码失败");
                }
            }
        }
    }

    public void saveaddress(String address){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("address",address).commit();
        sp = null;
    }
    public String getaddress(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String add = sp.getString("address","");
        sp = null;
        return add;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressET.setText(getaddress());
        addressET.setSelection(getaddress().length());
    }

    public void setText(String result){
        addressET.setText(result);
        addressET.setSelection(result.length());
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(result);
        showmsg("已将地址复制到剪切板");
        saveaddress(result);
        if (autoopen.isChecked()){
            openPackage(this,"com.pili.pldroid.playerdemo");
        }
    }

    public static boolean openPackage(Context context, String packageName){
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (pkgContext != null && intent != null) {
            pkgContext.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean openPackageInCurrentPackage(Context context, String packageName){
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (context != null && intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    public static Intent getAppOpenIntentByPackageName(Context context,String packageName){
        // MainActivity完整名
        String mainAct = null;
        // 根据包名寻找MainActivity
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK);

        List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }
}
