package com.majorproject.groceryshopping.shoppingapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shubhank Dubey on 19-03-2017.
 */

class IntroManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public IntroManager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences("First",0);
        editor = pref.edit();
    }

    protected void setFirst(boolean isFirst)
    {
        editor.putBoolean("check",isFirst);
        editor.commit();
    }

    protected boolean check()
    {
        return pref.getBoolean("check",true);
    }

}
