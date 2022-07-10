package com.kruceo.listaapp;

import android.graphics.drawable.Drawable;

public class appObj {
    String name,pkgName;
    Drawable icon;

    public appObj(String name, String pkgName, Drawable icon)
    {
        this.name = name;
        this.pkgName = pkgName;
        this.icon = icon;
    }
}
