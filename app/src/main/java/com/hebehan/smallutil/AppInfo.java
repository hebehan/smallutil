package com.hebehan.smallutil;

import android.graphics.drawable.Drawable;

/**
 * Created by Hebe on 16/8/30.
 */

public class AppInfo {

    private String appName;
    private String appPackage;
    private String appMain;
    private Drawable appIcon;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppMain() {
        return appMain;
    }

    public void setAppMain(String appMain) {
        this.appMain = appMain;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
