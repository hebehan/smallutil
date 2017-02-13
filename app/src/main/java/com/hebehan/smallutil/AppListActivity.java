package com.hebehan.smallutil;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private ListView applist_listview;
    private ArrayList<AppInfo> appInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);
        applist_listview = (ListView) findViewById(R.id.applist_listview);
        appInfos = new ArrayList<AppInfo>();
        getAppinfo2();
        applist_listview.setAdapter(new ApplistAdapter());
        applist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    Intent intent = getPackageManager().getLaunchIntentForPackage(appInfos.get(i).getAppPackage());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AppListActivity.this,"打开失败",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void getAppinfo(){
        List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
        for (PackageInfo info : packageInfos){
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(info.applicationInfo.loadLabel(getPackageManager()).toString());
            appInfo.setAppPackage(info.packageName);
            if (info.activities != null && info.activities.length >0){//拿不到activity信息
                appInfo.setAppMain(info.activities[0].name);
            }
            appInfo.setAppIcon(info.applicationInfo.loadIcon(getPackageManager()));
            appInfos.add(appInfo);
        }
    }

    public void getAppinfo2(){
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getPackageManager();
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
        for(ResolveInfo info : resolveInfos){
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(info.loadLabel(pm).toString());
            appInfo.setAppPackage(info.activityInfo.packageName);
            appInfo.setAppMain(info.activityInfo.name);
            appInfo.setAppIcon(info.loadIcon(pm));
            appInfos.add(appInfo);
        }
    }

    private class ApplistAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return appInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(AppListActivity.this).inflate(R.layout.applist_item,null,false);
            ImageView appicon = (ImageView) view.findViewById(R.id.appicon);
            TextView appname = (TextView) view.findViewById(R.id.appname);
            TextView apppackage = (TextView) view.findViewById(R.id.apppackage);
            TextView appmain = (TextView) view.findViewById(R.id.appmain);

            appicon.setImageDrawable(appInfos.get(i).getAppIcon());
            appname.setText("app名: "+appInfos.get(i).getAppName());
            apppackage.setText("包名: "+appInfos.get(i).getAppPackage());
            appmain.setText("启动类: "+appInfos.get(i).getAppMain());
            return view;
        }
    }
}
